package ru.itmo.server;

import ru.itmo.lab.common.commonNet.Response;
import ru.itmo.lab.common.myExceptions.BDException;
import ru.itmo.server.dao.CollectionLoader;
import ru.itmo.server.dao.StudyGroupDAO;
import ru.itmo.server.dao.UserDAO;
import ru.itmo.server.db.DatabaseHandler;
import ru.itmo.server.serverInterfaces.ExecuteResult;
import ru.itmo.server.serverInterfaces.InvokerActions;
import ru.itmo.server.ioHandlers.ServerConsoleHandler;
import ru.itmo.server.manager.collection.CollectionManager;
import ru.itmo.server.manager.serverLogic.*;
import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.server.serverNetManager.RequestReader;
import ru.itmo.server.serverNetManager.ResponseCompiler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ru.itmo.server.сommand.*;


public class Server
{
    public static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final int port = 6020;

    private static final ExecutorService readerPool = Executors.newCachedThreadPool();
    private static final ForkJoinPool processorPool = new ForkJoinPool();

    private static Invoker serverInvoker;
    private static boolean isRunning = true;
    /// Для гелиоса
    //private static String jdbcURL = "jdbc:postgresql://pg:5432/studs";
    /// Для отладки
    private static String jdbcURL = "jdbc:postgresql://localhost:2390/studs";

    public static void main(String[] args)
    {
        /// Серверный менеджер коллекцией
        Connection dbConnection = getDB_Connection();
        StudyGroupDAO dbManager;

        if( dbConnection == null )
            logger.error("БД не подключена. Сервер не запущен.");
        else
        {
            dbManager = new StudyGroupDAO(dbConnection);
            CollectionManager mainCollection = CollectionManager.createCollection();

            try
            {
                dbManager.loadEnumIDs();
            }
            catch( SQLException e )
            {
                logger.error("Не удалось импортировать константы из БД.");
            }
            new CollectionLoader(mainCollection, dbManager).loadCollection();

            Invoker invoker = new Invoker();
            registerClientCommands(invoker, mainCollection);

            serverInvoker = new Invoker();
            serverInvoker.addCommand("exit", new ExitCommand());

            ServerConsoleHandler console = new ServerConsoleHandler();

            Thread handleServerTerminal = new Thread(() -> {
                ServerConsoleHandler terminal = new ServerConsoleHandler();
                logger.info("Серверный терминал запущен.");

                while( true )
                {
                    String adminCommand = terminal.readline();
                    logger.info("Получена команда от Администратора сервера: " + adminCommand);
                    ExecuteResult serverCmdRes = serverInvoker.execute(new ServerCommandArgs(adminCommand.toLowerCase().trim()));
                    if( serverCmdRes.isSuccess() )
                        logger.info(serverCmdRes.getMessage());
                    else
                        logger.error("Ошибка обработки команды администратора: " + serverCmdRes.getMessage());
                }
            });
            handleServerTerminal.setDaemon(true);
            handleServerTerminal.start();

            /// Сеть
            logger.info("Сервер запустился. Порт: " + port);
            try( ServerSocket serverSocket = new ServerSocket(port) )
            {
                while( isRunning )
                {
                    logger.info("Ожидание подключения клиента...");
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Клиент подключен: " + clientSocket.getInetAddress());
                    readerPool.submit(() -> handleClient(clientSocket, invoker));
                }
            }
            catch( BDException e )
            {
                logger.error(e.getMessage());
            }
            catch( IOException e )
            {
                logger.error("Ошибка сервера: " + e.getMessage());
            }
        }
    }

    public static void handleClient( Socket clientSocket, InvokerActions invoker ) throws BDException
    {
        logger.info("Потоки ввода-вывода инициализированы.");
        try
        {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            OutputStream output = clientSocket.getOutputStream();

            CommandProccessor proccessor = new CommandProccessor( invoker );
            try( Connection dbConnection = getDB_Connection(); )
            {
                if( dbConnection == null )
                {
                    throw new BDException("Не удалось подключиться к БД.");
                }
                UserDAO userDAO = new UserDAO(dbConnection);
                StudyGroupDAO dbManager = new StudyGroupDAO(dbConnection);
                while( !clientSocket.isClosed() )
                {
                    try
                    {
                        int requestLength = input.readInt();

                        byte[] requestBytes = new byte[requestLength];
                        input.readFully(requestBytes);

                        // логика обработки поступившей информации
                        // читаем запрос
                        Request request = RequestReader.read(requestBytes);
                        logger.info("Получен запрос: " + request.getCommandType());

                        Future<Response> responseFuture = processorPool.submit( () -> proccessor.ProcessRequest(request, dbManager, userDAO) );
                        Thread responseThread = new Thread(() -> {
                            try
                            {
                                Response response = responseFuture.get();
                                logger.info("Запрос обработан!");
                                logger.info("<Начало запроса>");
                                logger.info("Success: " + response.isSuccess() + ";");
                                logger.info("Message: " + response.getMessage() + ";");
                                logger.info("<Конец запроса>");

                                synchronized( output )
                                {
                                    // отправляем обратно ответ
                                    byte[] responseBytes = ResponseCompiler.compileResponse(response);
                                    output.write(responseBytes);
                                    output.flush();
                                    logger.info("Ответ отправлен!\n");
                                }
                            }
                            catch( InterruptedException e )
                            {
                                logger.error("Поток отправки ответа прерван.");
                                Thread.currentThread().interrupt();
                            }
                            catch( ExecutionException e )
                            {
                                logger.error("Ошибка при отправке ответа: " + e.getMessage());
                            }
                            catch( IOException e )
                            {
                                logger.error("Ошибка при сетевой отправке ответа: " + e.getMessage());
                            }
                        });
                        responseThread.start();
                        responseThread.join();
                    }
                    catch( ClassNotFoundException e )
                    {
                        logger.error("Некорректные полученные данные");
                    }
                    catch( EOFException e )
                    {
                        logger.info("Клиент завершил сессию.");
                        break;
                    }
                    catch( SocketException e )
                    {
                        logger.error("Соединение с клиентом потеряно.");
                        break;
                    }
                    catch (IOException e )
                    {
                        logger.error("Ошибка потоков ввода-вывода: " + e.getMessage());
                        break;
                    }
                    catch( Exception e )
                    {
                        logger.error("Неизвестная ошибка при обработке запроса: " + e.getMessage());
                    }
                }
            }
            logger.info("Сессия завершена!\n");
        }
        catch( Exception e )
        {
            logger.error("Ошибка сессии клиента " + e.getMessage());
        }
        finally
        {
            try
            {
                if (clientSocket != null && !clientSocket.isClosed())
                {
                    clientSocket.close();
                }
            }
            catch (IOException e)
            {
                logger.error("Ошибка при финальном закрытии сокета: " + e.getMessage());
            }
            logger.info("Ресурсы клиента закрыты.");
        }
    }

    public static Connection getDB_Connection()
    {
        String username = null;
        String password = null;
        synchronized( Server.class )
        {
            try( Scanner credentials = new Scanner(new FileReader("credentials.txt")) )
            {
                if (credentials.hasNextLine()) username = credentials.nextLine().trim();
                if (credentials.hasNextLine()) password = credentials.nextLine().trim();
            }
            catch( FileNotFoundException e )
            {
                System.err.println("He найден credentials.txt с данными для входа в базу данных.");
                System.exit(-1);
            }
            catch( NullPointerException e )
            {
                System.err.println("Не определен сканер файла!");
            }
            catch( NoSuchElementException e )
            {
                System.err.println("Не найдены данные для входа в файле. Завершение работы.");
                System.exit(-1);
            }
            catch( Exception e )
            {
                logger.error("Ошибка чтения файла credentials.txt: " + e.getMessage());
                return null;
            }
        }

        DatabaseHandler database = new DatabaseHandler(jdbcURL, username, password);
        Connection dbConnection = database.connectToDatabase();
        return dbConnection;
    }

    public static void registerClientCommands( InvokerActions invoker, CollectionManager manager )
    {
        /// регистрируем все команды
        invoker.addCommand("help", new HelpCommand(invoker));
        invoker.addCommand("info", new InfoCommand(manager));
        invoker.addCommand("show", new ShowCommand(manager));
        invoker.addCommand("insert_element", new InsertElCommand(manager));
        invoker.addCommand("update_id", new UpdateIdCommand(manager));
        invoker.addCommand("remove_key", new RemoveCommand(manager));
        invoker.addCommand("clear", new ClearCommand(manager));
        invoker.addCommand("remove_greater", new RemoveGreater(manager));
        invoker.addCommand("remove_lower", new RemoveLower(manager));
        invoker.addCommand("remove_lower_key", new RomoveLowerKey(manager));
        invoker.addCommand("filter_by_semester_enum", new FilterBySemesterEnum(manager));
        invoker.addCommand("filter_starts_with_name", new FilterStartsWithName(manager));
        invoker.addCommand("print_ascending", new PrintAscending(manager));
    }

    static public void stopServer()
    {
        logger.info("Завершение работы сервера...");
        isRunning = false;

        processorPool.shutdown();

        try
        {
            // Даем пулам время на завершение задач
            if (!processorPool.awaitTermination(5, TimeUnit.SECONDS))
            {
                processorPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            processorPool.shutdownNow();
    }
}
}
