package ru.itmo.client.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.client.clienInterfaces.ResponseListener;
import ru.itmo.client.gui.utils.Session;
import ru.itmo.client.network.NetworkManager;
import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.lab.common.commonNet.ResponseCmdRes;
import ru.itmo.lab.common.model.StudyGroup;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthFrame extends JFrame implements ResponseListener
{
    private final NetworkManager networkManager;
    private final Logger logger = LoggerFactory.getLogger(AuthFrame.class);

    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;
    private JComboBox<String> languageBox;

    private String waitingMessage;
    private String errorMessageEmptyInput;
    private String successfulLogin;
    private String statusMessage;

    private ResourceBundle bundle;

    public AuthFrame( NetworkManager networkManager )
    {
        this.networkManager = networkManager;
        this.networkManager.addListener(this);

        loadBundle( new Locale("ru", "RU") );

        initUI();
        updateTexts();
    }

    private void initUI()
    {
        setTitle("Авторизация");
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout( new BorderLayout() );

        // Панель выбора языка
        JPanel topPanel = new JPanel( new FlowLayout(FlowLayout.RIGHT) );
        languageBox = new JComboBox<>( new String[]{"Русский", "English", "Norsk", "Italiano"} );
        languageBox.addActionListener(e -> changeLanguage());
        topPanel.add(languageBox);
        add(topPanel, BorderLayout.NORTH);

        // Центральная панель с формой
        JPanel centerPanel = new JPanel( new GridLayout(3, 2, 10, 10) );
        centerPanel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );

        loginLabel = new JLabel();
        passwordLabel = new JLabel();

        centerPanel.add( loginLabel );
        loginField = new JTextField();
        centerPanel.add( loginField );

        centerPanel.add( passwordLabel );
        passwordField = new JPasswordField();
        centerPanel.add( passwordField );

        loginButton = new JButton("Войти");
        registerButton = new JButton("Регистрация");

        // Обработчики кнопок
        loginButton.addActionListener(e -> sendAuthRequest("login"));
        registerButton.addActionListener(e -> sendAuthRequest("register"));

        centerPanel.add(loginButton);
        centerPanel.add(registerButton);

        add(centerPanel, BorderLayout.CENTER);

        // Статусная строка для ошибок
        statusLabel = new JLabel(statusMessage, SwingConstants.CENTER);
        statusLabel.setForeground(Color.GRAY);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void changeLanguage()
    {
        int index = languageBox.getSelectedIndex();
        switch (index)
        {
            case 0 -> loadBundle( new Locale("ru", "RU") );
            case 1 -> loadBundle( new Locale("en", "US") );
            case 2 -> loadBundle( new Locale("no", "NO") );
            case 3 -> loadBundle( new Locale("it", "IT") );
        }
        updateTexts();
    }

    private void loadBundle( Locale locale )
    {
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    private void updateTexts()
    {
        setTitle(bundle.getString("auth.title"));
        loginLabel.setText(bundle.getString("auth.login"));
        passwordLabel.setText(bundle.getString("auth.password"));
        loginButton.setText(bundle.getString("auth.btn.login"));
        registerButton.setText(bundle.getString("auth.btn.register"));
        waitingMessage = bundle.getString("auth.status.wait");
        errorMessageEmptyInput = bundle.getString("auth.status.empty");
        successfulLogin = bundle.getString("auth.status.success");
        statusMessage = bundle.getString("statusMessageForInput");

        statusLabel.setText(statusMessage);
        statusLabel.setForeground(Color.GRAY);
    }

    private void sendAuthRequest( String command )
    {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        if( login.isEmpty() || password.isEmpty() )
        {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(errorMessageEmptyInput);
            return;
        }

        // Блокируем кнопки для задержки
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);
        statusLabel.setForeground(Color.BLUE);
        statusLabel.setText(waitingMessage);

        Request request = new Request.Builder()
                                    .setCommandType(command)
                                    .setLogin(login)
                                    .setPassword(password)
                                    .buildRequest();
        networkManager.sendRequest(request);
    }

    @Override
    public void handleResponse( ResponseCmdRes responseCmdRes )
    {
        // Разблокируем кнопки
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);

        if( responseCmdRes.isSuccess() )
        {
            statusLabel.setForeground(new Color(0, 150, 0));
            statusLabel.setText(successfulLogin);

            logger.info("Успешное подключение");

            Session.getInstance().setUser(
                    loginField.getText().trim(),
                    new String( passwordField.getPassword() )
            );

            // убираем из списка принемающих ответ от сервера и закрываем окно
            networkManager.removeListener(this);
            this.dispose();
            logger.info("Успешный вход. Запуск приложения.");

            SwingUtilities.invokeLater( () -> {
                MainFrame mainFrame = new MainFrame( networkManager );
                mainFrame.setVisible(true);
            });
        }
        else
        {
            logger.error("Ошибка авторизации: " + responseCmdRes.getMessage());
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(responseCmdRes.getMessage());
        }
    }

    @Override
    public void onError(String error )
    {
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);
        statusLabel.setForeground(Color.RED);
        statusLabel.setText(error);
    }

    @Override
    public void onCollectionUpdated( Hashtable<Long, StudyGroup> newCollection ) { return; }
}