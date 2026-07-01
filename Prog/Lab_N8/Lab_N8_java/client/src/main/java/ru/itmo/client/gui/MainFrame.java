package ru.itmo.client.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.client.clienInterfaces.ResponseListener;
import ru.itmo.client.gui.dialog.GroupUpdateDialog;
import ru.itmo.client.gui.dialog.StudyGroupDialog;
import ru.itmo.client.gui.table.AdminCellEditor;
import ru.itmo.client.gui.utils.LanguageManager;
import ru.itmo.client.gui.utils.Session;
import ru.itmo.client.gui.visual.CollectionCanvas;
import ru.itmo.client.network.NetworkManager;
import ru.itmo.client.gui.table.StudyGroupTable;
import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.lab.common.commonNet.ResponseCmdRes;
import ru.itmo.lab.common.model.Person;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.myEnums.FormOfEducation;
import ru.itmo.lab.common.myEnums.Semester;
import ru.itmo.lab.common.myRecords.Lab5FieldDescriptor;
import ru.itmo.lab.common.myRecords.UpdatedFieldDescriptor;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.*;

public class MainFrame extends JFrame implements ResponseListener
{
    private final CollectionCanvas collectionCanvas = new CollectionCanvas();
    private final String USER;
    private final String PASSWORD;
    private final Logger logger = LoggerFactory.getLogger(MainFrame.class);
    private final NetworkManager networkManager;

    private StudyGroupTable tableModel;
    private JTable table;
    private TableRowSorter<StudyGroupTable> sorter;
    private JTabbedPane tabbedPane;

    private JLabel userLabel;
    private JLabel langLabel;
    private JComboBox<String> languageBox;
    private JButton reloginButton;
    private JButton exitButton;
    private JButton addButton;
    private JButton clearButton;
    private JButton removeButton;
    private JButton updateButton;

    private final LanguageManager languageManager = LanguageManager.getInstance();

    public MainFrame( NetworkManager networkManager )
    {
        this.networkManager = networkManager;
        this.networkManager.addListener( this );
        networkManager.sendRequest( new Request.Builder()
                .setCommandType("show")
                .setLogin(Session.getInstance().getLogin())
                .setPassword(Session.getInstance().getPassword())
                .buildRequest() );
        USER = Session.getInstance().getLogin();
        PASSWORD = Session.getInstance().getPassword();

        collectionCanvas.setPreferredSize(new Dimension(600, 800));
        initUI();

        updateTexts();
    }

    private void initUI()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout( new BorderLayout() );

        // Шапка
        JPanel topPanel = new JPanel( new BorderLayout() );
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Левая часть
        JPanel userPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        userLabel = new JLabel();
        userPanel.add( userLabel );
        topPanel.add( userPanel, BorderLayout.WEST );

        // Правая часть
        JPanel controlsPanel = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );

        languageBox = new JComboBox<>( new String[]{"Русский", "English", "Norsk", "Italiano"} );
        Locale currentLoc = languageManager.getLocale();
        if( currentLoc.getLanguage().equals("en") ) languageBox.setSelectedIndex(1);
        else if( currentLoc.getLanguage().equals("no") ) languageBox.setSelectedIndex(2);
        else if( currentLoc.getLanguage().equals("it") ) languageBox.setSelectedIndex(3);
        else languageBox.setSelectedIndex(0);

        languageBox.addActionListener(e -> changeLanguage());

        langLabel = new JLabel();
        reloginButton = new JButton();
        exitButton = new JButton();

        controlsPanel.add( langLabel );
        controlsPanel.add( languageBox );
        controlsPanel.add( reloginButton );
        controlsPanel.add( exitButton );
        topPanel.add( controlsPanel, BorderLayout.EAST );

        add( topPanel, BorderLayout.NORTH );

        // Рабочая область
        tabbedPane = new JTabbedPane();
        tableModel = new StudyGroupTable( networkManager );
        table = new JTable( tableModel );

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        tabbedPane.addTab("", new JScrollPane(table));
        tabbedPane.addTab("", new JScrollPane(collectionCanvas));
        add(tabbedPane, BorderLayout.CENTER);

        // Панель команд
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        addButton = new JButton();
        addButton.addActionListener(e -> {
            StudyGroupDialog dialog = new StudyGroupDialog(this);
            dialog.setVisible(true);

            if( dialog.isConfirmed() )
            {
                StudyGroup newGroup = dialog.getStudyGroup();
                Long key = dialog.getKey();
                if( key != null && newGroup != null )
                {
                    Request request = new Request.Builder()
                            .setCommandType("insert_element")
                            .setID(key)
                            .setGroup(newGroup)
                            .setLogin(USER)
                            .setPassword(PASSWORD)
                            .buildRequest();
                    networkManager.sendRequest(request);
                }
            }
        });
        buttonPanel.add(addButton);

        clearButton = new JButton();
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, languageManager.getString("main.msg.clear_confirm"));
            if (confirm == JOptionPane.YES_OPTION)
            {
                Request request = new Request.Builder()
                        .setCommandType("clear")
                        .setLogin(USER)
                        .setPassword(PASSWORD)
                        .buildRequest();
                networkManager.sendRequest(request);
            }
        });
        buttonPanel.add(clearButton);

        removeButton = new JButton();
        removeButton.addActionListener(e -> {
            String keyStr = JOptionPane.showInputDialog(this, languageManager.getString("main.msg.remove_input"));
            if (keyStr != null && !keyStr.isEmpty())
            {
                try
                {
                    Long key = Long.parseLong(keyStr);
                    Request request = new Request.Builder()
                            .setCommandType("remove_key")
                            .setID(key)
                            .setLogin(USER)
                            .setPassword(PASSWORD)
                            .buildRequest();
                    networkManager.sendRequest(request);
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this, languageManager.getString("main.msg.remove_error"));
                }
            }
        });
        buttonPanel.add(removeButton);

        updateButton = new JButton();
        updateButton.addActionListener(e -> {
            String keyStr = JOptionPane.showInputDialog(this, languageManager.getString("main.msg.update_input"));

            if (keyStr != null)
            {
                String trimmedKey = keyStr.trim();

                if (trimmedKey.isEmpty())
                {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                languageManager.getString("main.msg.update_error"),
                                languageManager.getString("main.err.execution"),
                                JOptionPane.ERROR_MESSAGE);
                    });
                    return;
                }
                try
                {
                    Long key = Long.parseLong(trimmedKey);

                    GroupUpdateDialog dialog = new GroupUpdateDialog(this);
                    dialog.setVisible(true);

                    if (dialog.isConfirmed())
                    {
                        Object updatedItem = dialog.getNewValue();
                        String fieldName = dialog.getFieldName();
                        logger.info("fieldName: " + ((fieldName != null) ? fieldName : "null"));
                        UpdatedFieldDescriptor fieldDescriptor = Lab5FieldDescriptor.UPDATED_FIELDS.stream()
                                .filter(f -> f.name().equalsIgnoreCase(fieldName))
                                .peek(item -> System.out.println(item.name()))
                                .findFirst().orElse(null);

                        Request.Builder requestBuilder = new Request.Builder()
                                .setCommandType("update_id")
                                .setID(key)
                                .setLogin(USER)
                                .setPassword(PASSWORD)
                                .setUpdatedField(fieldDescriptor);
                        if( updatedItem instanceof Person )
                        {
                            requestBuilder.setPerson((Person) updatedItem);
                        }
                        else
                        {
                            if( !(updatedItem instanceof String) )
                                logger.error("Неизвестный формат для команды обновления элемента!");
                            requestBuilder.setArgument( (String) updatedItem );
                        }

                        networkManager.sendRequest(requestBuilder.buildRequest());
                    }
                }
                catch( NumberFormatException ex )
                {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                languageManager.getString("main.msg.remove_error"),
                                languageManager.getString("main.err.execution"),
                                JOptionPane.ERROR_MESSAGE);
                    });
                }
            }
        });
        buttonPanel.add(updateButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void handleResponse( ResponseCmdRes response )
    {
        logger.debug(languageManager.getString("main.msg.server_update"));
        if( !response.isSuccess() )
        {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "Ошибка: " + response.getMessage(),
                        languageManager.getString("main.err.execution"),
                        JOptionPane.ERROR_MESSAGE);
            });
        }
        else
        {
            logger.debug("Команда выполнена успешно!");
        }
        if( response.getCollection() != null )
        {
            SwingUtilities.invokeLater( () -> {
                tableModel.setData( response.getCollection() );
                collectionCanvas.setGroups(new ArrayList<>(response.getCollection().values()));
            });
        }
        else
            logger.debug("Полученная коллекция не определена!");
    }

    @Override
    public void onCollectionUpdated( Hashtable<Long, StudyGroup> newCollection )
    {
        tableModel.updateAllData( newCollection );
        collectionCanvas.setGroups( new ArrayList<>(newCollection.values()) );

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, languageManager.getString("main.msg.server_update"));
        });
    }

    private void changeLanguage()
    {
        int index = languageBox.getSelectedIndex();

        switch (index)
        {
            case 0 -> languageManager.setLocale(new Locale("ru", "RU"));
            case 1 -> languageManager.setLocale(new Locale("en", "US"));
            case 2 -> languageManager.setLocale(new Locale("no", "NO"));
            case 3 -> languageManager.setLocale(new Locale("it", "IT"));
        }

        updateTexts();
    }

    private void updateTexts()
    {
        setTitle(languageManager.getString("main.title"));
        userLabel.setText(languageManager.getString("main.user") + Session.getInstance().getLogin());
        langLabel.setText(languageManager.getString("main.language"));
        reloginButton.setText(languageManager.getString("main.btn.relogin"));
        exitButton.setText(languageManager.getString("main.btn.exit"));

        tabbedPane.setTitleAt(0, languageManager.getString("main.tab.table"));
        tabbedPane.setTitleAt(1, languageManager.getString("main.tab.visual"));

        addButton.setText(languageManager.getString("main.btn.add"));
        clearButton.setText(languageManager.getString("main.btn.clear"));
        removeButton.setText(languageManager.getString("main.btn.remove"));
        updateButton.setText(languageManager.getString("main.btn.update"));

        tableModel.fireTableStructureChanged();

        setupTableEditors();
        setupTableSorters();
    }

    private void setupTableEditors()
    {
        TableColumn semesterCol = table.getColumnModel().getColumn(8);
        semesterCol.setCellEditor(new DefaultCellEditor(new JComboBox<>(Semester.values())));
        TableColumn formCol = table.getColumnModel().getColumn(9);
        formCol.setCellEditor(new DefaultCellEditor(new JComboBox<>(FormOfEducation.values())));
        table.getColumnModel().getColumn(10).setCellEditor(new AdminCellEditor(this));
    }

    private void setupTableSorters()
    {
        sorter.setComparator(8, (o1, o2) -> {
            Map<String, Integer> weights = Map.of(
                    "FIRST", 1, "SECOND", 2, "THIRD", 3, "FIFTH", 5, "EIGHTH", 8
            );
            return Integer.compare(weights.get(o1.toString()), weights.get(o2.toString()));
        });
        sorter.setComparator(9, (o1, o2) -> {
            Map<String, Integer> weights = Map.of(
                    "EVENING CLASSES", 1,
                    "DISTANCE EDUCATION", 2,
                    "FULL TIME EDUCATION", 3
            );
            return Integer.compare(weights.get(o1.toString()), weights.get(o2.toString()));
        });
        Comparator<Object> numberComparator = (o1, o2) -> {
            try
            {
                double n1 = Double.parseDouble(o1.toString());
                double n2 = Double.parseDouble(o2.toString());
                return Double.compare(n1, n2);
            }
            catch( NumberFormatException e )
            {
                return 0;
            }
        };
        sorter.setComparator(0, numberComparator); // key
        sorter.setComparator(1, numberComparator); // ID
        sorter.setComparator(3, numberComparator); // X
        sorter.setComparator(4, numberComparator); // Y
        sorter.setComparator(6, numberComparator); // StudentCount
        sorter.setComparator(7, numberComparator); // ShouldBeExp
    }

    @Override
    public void onError( String error )
    {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    languageManager.getString("main.err.network") + error,
                    languageManager.getString("main.err.critical"),
                    JOptionPane.WARNING_MESSAGE);
        });
    }
}