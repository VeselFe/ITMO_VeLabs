package ru.itmo.client.consoleApp;

import ru.itmo.client.clientTerminal.AuthManager;
import ru.itmo.client.clientTerminal.ClientConsoleHandler;
import ru.itmo.client.network.NetworkManager;
import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.lab.common.myExceptions.ConnectionException;
import ru.itmo.lab.common.myExceptions.ResponseException;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static ru.itmo.client.Client.connectToServer;

public class ConsoleApplication
{
    private boolean startClient = true;
    private boolean authenticated = false;
    private ClientConsoleHandler console = new ClientConsoleHandler();
    private NetworkManager networkManager;

    public ConsoleApplication(NetworkManager networkManager )
    {
        this.networkManager = networkManager;
    }

    public void appStart()
    {
        while( startClient )
        {
            try( SocketChannel channel = connectToServer(console) )
            {
                networkManager.setChannel( channel );

                while( !authenticated && startClient )
                {
                    AuthManager authManager = new AuthManager(console, networkManager);
                    boolean shouldRetry = authManager.authenticate();
                    if( !shouldRetry )
                    {
                        if( authManager.getLogin() == null )
                        {
                            console.printInfo("Приложение завершило работу.");
                            startClient = false;
                        }
                        else
                        {
                            console.initRequestCreator( console, authManager.getLogin(), authManager.getPassword() );
                            console.setUser(authManager.getLogin());
                            authenticated = true;
                        }
                    }
                }
                if( startClient )
                {
                    processClient(console, networkManager);
                    startClient = false;
                }
            }
            catch( ConnectionException e )
            {
                console.printError("Ошибка соединения: " + e.getMessage());
            }
            catch( IOException e )
            {
                console.printError("Ошибка подключения: " + e.getMessage());
            }
            catch( Exception e )
            {
                console.printError("Неизвестная ошибка: " + e.getMessage());
                startClient = false;
            }
        }
            console.close();
    }

    private static void processClient(ClientConsoleHandler console, NetworkManager networkManager ) throws IOException, ConnectionException
    {
        boolean exit = false;
        console.welcomMessage();

        while( !exit )
        {
            Request request = console.createRequest();
            try
            {
                if (request != null)
                {
                    networkManager.network(request);
                    String serverResponse = networkManager.getServerResponse();
                    if (!request.getCommandType().equals("exit"))
                    {
                        console.printInfo(serverResponse);
                    }
                    else
                    {
                        console.printInfo("Соединение успешно завершено!");
                        exit = true;
                    }
                }
                else
                {
                    console.printError("Ошибка генерации запроса. Запрос не отправлен!");
                }
            }
            catch (ResponseException e)
            {
                console.printError(e.getMessage());
            }
            catch (Exception e)
            {
                throw new IOException("ошибка при попытке отправки запроса на сервер. " + e.getMessage());
            }

            if (exit) break;
        }
    }
}
