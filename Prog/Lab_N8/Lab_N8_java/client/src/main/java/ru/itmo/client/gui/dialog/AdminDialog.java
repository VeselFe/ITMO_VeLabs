package ru.itmo.client.gui.dialog;

import ru.itmo.client.gui.utils.LanguageManager;
import ru.itmo.lab.common.model.Person;
import ru.itmo.lab.common.myEnums.Country;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.*;

public class AdminDialog extends JDialog
{
    private final LanguageManager languageManager = LanguageManager.getInstance();
    private JTextField nameField = new JTextField(15);
    private JTextField passportField = new JTextField(10);
    private SpinnerNumberModel modelWeight = new SpinnerNumberModel(70, 15, 400, 1);
    private JComboBox<Country> nationBox = new JComboBox<>( Country.values() );
    private JSpinner WeightSpinner = new JSpinner(modelWeight);
    private JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    private JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());

    private Person person = null;

    public AdminDialog( Frame owner )
    {
        super(owner, "Данные администратора", true);
        setLayout( new GridLayout(0, 2, 5, 5) );

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd.mm.yyyy");
        dateSpinner.setEditor(dateEditor);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm:ss");
        timeSpinner.setEditor(timeEditor);

        add(new JLabel("Имя:")); add(nameField);
        add(new JLabel("Паспорт:")); add(passportField);
        add(new JLabel("Вес:")); add(WeightSpinner);
        add(new JLabel("Дата рождения:")); add(dateSpinner);
        add(new JLabel("Время рождения:")); add(timeSpinner);
        add(new JLabel("Национальность:")); add(nationBox);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            try
            {
                java.util.Date d = (java.util.Date) dateSpinner.getValue();
                java.util.Date t = (java.util.Date) timeSpinner.getValue();
                LocalDate date = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime time = t.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                // Склеиваем их в LocalDateTime
                LocalDateTime birthDateTime = LocalDateTime.of(date, time);
                String filteredBirthday = birthDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                String passp = passportField.getText() == null ? "" : passportField.getText();

                person = new Person.Builder()
                        .setName(nameField.getText())
                        .setPassportID(passp)
                        .setWeight(modelWeight.getValue().toString())
                        .setNationality( (Country) nationBox.getSelectedItem() )
                        .setBirthday(filteredBirthday)
                        .build();
                dispose();
            }
            catch( Exception exp )
            {
                JOptionPane.showMessageDialog(null, "Некорректные параметры для добавления нового администратора: " + exp.getMessage(),
                        "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(saveButton);
        pack();
        setLocationRelativeTo(owner);
    }
    public Person getPerson() { return person; }
}