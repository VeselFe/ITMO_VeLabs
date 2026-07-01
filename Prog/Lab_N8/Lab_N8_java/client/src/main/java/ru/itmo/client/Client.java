package ru.itmo.client;

import ru.itmo.client.clientTerminal.ClientConsoleHandler;
import ru.itmo.client.gui.AuthFrame;
import ru.itmo.client.network.NetworkManager;
import ru.itmo.lab.common.commonNet.Request;

import ru.itmo.lab.common.interfaces.IO_Handler;
import ru.itmo.lab.common.myExceptions.ConnectionException;
import ru.itmo.lab.common.myExceptions.ResponseException;

import javax.swing.*;
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
        SocketChannel channel = connectToServer();
        if( channel == null )
        {
            System.out.println("ERROR: Не удалось подключиться к серверу.");
            return;
        }
        try
        {
            networkManager.setChannel(channel);
        }
        catch( IOException e )
        {
            System.err.println("Ошибка настройки сетевого менеджера: " + e.getMessage());
            return;
        }

        if (args.length > 0 && args[0].equals("--console")) {
            System.out.println("Запуск в консольном режиме...");
            //consoleApplication.appStart();
        }
        else
        {
            System.out.println("Запуск графического интерфейса...");
            SwingUtilities.invokeLater(() -> {
                AuthFrame authFrame = new AuthFrame(networkManager);
                authFrame.setVisible(true);
            });
        }
    }

    public static SocketChannel connectToServer()
    {
        SocketChannel socketChannel = null;
        boolean conection = false;

        System.out.println("Клиент подключен к серверу " + host + ":" + port);

        while( !conection )
        {
            try
            {
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.connect( new InetSocketAddress(host, port) );

                while( !socketChannel.finishConnect() )
                {
                    System.out.print(".");
                    Thread.sleep(500);
                }
                conection = true;
                System.out.println("Успешно подключено к серверу!");
            }
            catch( IOException e )
            {
                System.out.println("ERROR: Сервер недоступен. Повторная попытка через " + (connectionDelay / 1000) + " секунд...");
                try
                {
                    if( socketChannel != null ) { socketChannel.close(); }
                    Thread.sleep(connectionDelay);
                }
                catch(InterruptedException | IOException exception )
                {
                    System.out.println("ERROR: Ошибка при переподключении");
                    break;
                }
            }
            catch( InterruptedException e )
            {
                System.out.println("ERROR: Подключение прервано");
                break;
            }
        }
        return socketChannel;
    }
}