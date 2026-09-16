package ru.itmo.client;

import ru.itmo.client.clientTerminal.ClientConsoleHandler;
import ru.itmo.client.clientTerminal.AuthManager;
import ru.itmo.client.consoleApp.ConsoleApplication;
import ru.itmo.client.network.NetworkManager;
import ru.itmo.lab.common.commonNet.Request;

import ru.itmo.lab.common.interfaces.IO_Handler;
import ru.itmo.lab.common.myExceptions.ConnectionException;
import ru.itmo.lab.common.myExceptions.ResponseException;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client
{
    private static final String host = "localhost";
    private static final int port = 6020;
    private static final int connectionDelay = 5000;

    public static void main( String[] args )
    {
        NetworkManager networkManager = new NetworkManager();
        ConsoleApplication consoleApplication = new ConsoleApplication(networkManager);

        if (args.length > 0 && args[0].equals("--console")) {
            System.out.println("Запуск в консольном режиме...");
            consoleApplication.appStart();
        }
        else
        {
            System.out.println("Запуск графического интерфейса...");

        }
    }

    public static SocketChannel connectToServer( IO_Handler console )
    {
        SocketChannel socketChannel = null;
        boolean conection = false;

        console.printInfo("Клиент подключен к серверу " + host + ":" + port);

        while( !conection )
        {
            try
            {
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.connect( new InetSocketAddress(host, port) );

                while( !socketChannel.finishConnect() )
                {
                    console.printRequest(".");
                    Thread.sleep(500);
                }
                conection = true;
                console.printInfo("Успешно подключено к серверу!");
            }
            catch( IOException e )
            {
                console.printError("Сервер недоступен. Повторная попытка через " + (connectionDelay / 1000) + " секунд...");
                try
                {
                    if( socketChannel != null ) { socketChannel.close(); }
                    Thread.sleep(connectionDelay);
                }
                catch(InterruptedException | IOException exception )
                {
                    console.printError("Ошибка при переподключении");
                    break;
                }
            }
            catch( InterruptedException e )
            {
                console.printError("Подключение прервано");
                break;
            }
        }
        return socketChannel;
    }
}