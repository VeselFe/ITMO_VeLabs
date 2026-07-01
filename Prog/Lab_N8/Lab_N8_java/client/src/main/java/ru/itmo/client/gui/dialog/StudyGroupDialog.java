package ru.itmo.client.gui.dialog;

import ru.itmo.client.gui.utils.Session;
import ru.itmo.lab.common.model.*;
import ru.itmo.lab.common.myEnums.FormOfEducation;
import ru.itmo.lab.common.myEnums.Semester;

import javax.swing.*;
import java.awt.*;

public class StudyGroupDialog extends JDialog
{
    //private SpinnerNumberModel modelKey = new SpinnerNumberModel(0, 0, 10000, 1); 
    //private JSpinner KeySpinner = new JSpinner(modelKey);
    private JTextField key = new JTextField(5);
    private JTextField nameField = new JTextField(15);
    private JTextField xField = new JTextField(5);
    private JTextField yField = new JTextField(5);
    //private JTextField studentsField = new JTextField(5);
    //private JTextField shouldBeExpField = new JTextField(5);
    private SpinnerNumberModel modelForStudCount = new SpinnerNumberModel(0, 0, 10000, 1); 
    private SpinnerNumberModel modelForShBEExp = new SpinnerNumberModel(0, 0, 10000, 1); 
    private JSpinner studCountSpinner = new JSpinner(modelForStudCount);
    private JSpinner studShBeExpSpinner = new JSpinner(modelForShBEExp);
    private JComboBox<Semester> semesterBox = new JComboBox<>( Semester.values() );
    private JComboBox<FormOfEducation> formOfEduBox = new JComboBox<>( FormOfEducation.values() );

    private JButton adminButton = new JButton("Добавить администратора");
    private Person currentAdmin = null;

    private boolean confirmed = false;

    public StudyGroupDialog( Frame owner )
    {
        super(owner, "Добавить группу", true);
        setLayout(new GridLayout(9, 2, 5, 5));

        add( new JLabel("Ключ группы:") ); add(key);
        add( new JLabel("Имя:")) ; add(nameField);
        add( new JLabel("X:") ); add(xField);
        add( new JLabel("Y:") ); add(yField);
        //add( new JLabel("Кол-во студентов:") ); add(studentsField);
        add( new JLabel("Кол-во студентов:") ); add(studCountSpinner);
        //add( new JLabel("Кол-во студентов на отчисление:") ); add(studentsField);
        add( new JLabel("Кол-во студентов  на отчисление:") ); add(studShBeExpSpinner);

        add( new JLabel("Семестр:") ); add(semesterBox);
        add( new JLabel("Форма обучения:") ); add(formOfEduBox);

        adminButton.addActionListener(e -> {
            AdminDialog adminDialog = new AdminDialog((Frame) SwingUtilities.getWindowAncestor(this));
            adminDialog.setVisible(true);

            if( adminDialog.getPerson() != null )
            {
                this.currentAdmin = adminDialog.getPerson();
                adminButton.setText("Админ: " + currentAdmin.getName());
            }
        });
        add(adminButton);

        JButton okButton = new JButton("ОК");
        okButton.addActionListener(e -> { confirmed = true; dispose(); } );
        add(okButton);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() { return confirmed; }

    // Метод для создания объекта из данных формы
    public StudyGroup getStudyGroup()
    {
        try
        {
            StudyGroup.Builder groupBuilder = new StudyGroup.Builder();
            groupBuilder.setName(nameField.getText());
            groupBuilder.setCoordinates(xField.getText() + ' ' + yField.getText());
            groupBuilder.setStudCount( (Integer) studCountSpinner.getValue() );
            groupBuilder.setShBeExp( studShBeExpSpinner.getValue().toString() );
            groupBuilder.setSem((Semester) semesterBox.getSelectedItem());
            groupBuilder.setFormOfEdu((FormOfEducation) formOfEduBox.getSelectedItem());
            groupBuilder.setAdmin(currentAdmin);
            groupBuilder.setOwner(Session.getInstance().getLogin());

            return groupBuilder.build();
        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog(null, "Некорректные параметры для создания: " + e.getMessage(),
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
    public Long getKey()
    {
        try
        {
            long key = Long.valueOf(this.key.getText());
            return key;
        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog(this, "Некорректое значение ключа. Формат: целое число",
                "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}