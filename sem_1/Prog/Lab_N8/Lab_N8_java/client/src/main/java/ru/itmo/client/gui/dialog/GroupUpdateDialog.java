package ru.itmo.client.gui.dialog;

import ru.itmo.client.gui.utils.LanguageManager;
import ru.itmo.lab.common.model.*;
import ru.itmo.lab.common.myEnums.FormOfEducation;
import ru.itmo.lab.common.myEnums.Semester;
import javax.swing.*;
import java.awt.*;

public class GroupUpdateDialog extends JDialog
{
    private final LanguageManager languageManager = LanguageManager.getInstance();

    private JComboBox<String> fieldSelector;
    private JPanel inputPanel;

    private JTextField nameField = new JTextField(15);
    private JSpinner studCountSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
    private JSpinner studShBeExpSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
    private JComboBox<Semester> semesterBox = new JComboBox<>(Semester.values());
    private JComboBox<FormOfEducation> formOfEduBox = new JComboBox<>(FormOfEducation.values());
    private JTextField xField = new JTextField(5);
    private JTextField yField = new JTextField(5);
    private JButton adminButton = new JButton("Выбрать админа");
    private Person currentAdmin = null;

    private boolean confirmed = false;
    private String selectedField;

    public GroupUpdateDialog( Frame owner )
    {
        super(owner, "Обновить поле группы", true);
        setLayout(new BorderLayout(15, 10));

        // Выбор поля
        fieldSelector = new JComboBox<>( new String[]{"Name", "StudentCount", "ShoudBeExpelled", "Coordinates", "Semester", "FormOfEducation", "Admin"} );
        fieldSelector.addActionListener(e -> updateInputFields());

        JPanel topPanel = new JPanel( new FlowLayout() );
        topPanel.add( new JLabel(languageManager.getString("dialog.select_field")) );
        topPanel.add(fieldSelector);
        add(topPanel, BorderLayout.NORTH);

        // Панель ввода
        inputPanel = new JPanel();
        add(inputPanel, BorderLayout.CENTER);

        JButton okButton = new JButton(languageManager.getString("dialog.update.btn.update"));
        okButton.addActionListener(e -> {
            if( validateInput() )
            {
                confirmed = true;
                dispose();
            }
        });
        add(okButton, BorderLayout.SOUTH);

        updateInputFields();
        pack();
        setLocationRelativeTo(owner);
    }

    private void updateInputFields()
    {
        inputPanel.removeAll();
        selectedField = (String) fieldSelector.getSelectedItem();
        JPanel content = new JPanel(new FlowLayout());

        switch (selectedField)
        {
            case "Name" -> { inputPanel.add(new JLabel(languageManager.getString("dialog.update.new_name"))); inputPanel.add(nameField); }
            case "StudentCount" -> { inputPanel.add(new JLabel(languageManager.getString("dialog.add.stud_count"))); inputPanel.add(studCountSpinner); }
            case "ShoudBeExpelled" -> {
                studCountSpinner.setPreferredSize(new Dimension(100, 25));
                inputPanel.add(new JLabel(languageManager.getString("dialog.add.stud_expelled")));
                inputPanel.add(studShBeExpSpinner);
            }
            case "Semester" -> { inputPanel.add(new JLabel(languageManager.getString("dialog.add.semester"))); inputPanel.add(semesterBox); }
            case "Coordinates" -> {
                content.add(new JLabel("X:")); content.add(xField);
                content.add(new JLabel("Y:")); content.add(yField);
            }
            case "FormOfEducation" -> { inputPanel.add( new JLabel(languageManager.getString("dialog.update.form")) ); inputPanel.add(formOfEduBox); }
            case "Admin" -> {
                adminButton.addActionListener(e -> {
                    AdminDialog ad = new AdminDialog((Frame) SwingUtilities.getWindowAncestor(this));
                    ad.setVisible(true);
                    if( ad.getPerson() != null )
                    {
                        currentAdmin = ad.getPerson();
                        adminButton.setText(languageManager.getString("dialog.add.admin_prefix") + currentAdmin.getName());
                    }
                });
                inputPanel.add(adminButton);
            }
        }
        inputPanel.add(content);
        inputPanel.revalidate();
        inputPanel.repaint();
        this.pack();
    }

    public boolean isConfirmed() { return confirmed; }
    public String getFieldName() { return selectedField; }

    public Object getNewValue()
    {
        return switch (selectedField)
        {
            case "Name" -> nameField.getText();
            case "StudentCount" -> studCountSpinner.getValue().toString();
            case "ShoudBeExpelled" -> studShBeExpSpinner.getValue().toString();
            case "Coordinates" -> xField.getText() + " " + yField.getText();
            case "Semester" -> semesterBox.getSelectedItem().toString();
            case "FormOfEducation" -> formOfEduBox.getSelectedItem().toString();
            case "Admin" -> currentAdmin;
            default -> null;
        };
    }

    private boolean validateInput()
    {
        switch (selectedField)
        {
            case "Name" -> {
                if( nameField.getText() == null || nameField.getText().trim().isEmpty() )
                {
                    JOptionPane.showMessageDialog(this, languageManager.getString("dialog.err.name"), languageManager.getString("dialog.err.input"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            case "Coordinates" -> {
                String xStr = xField.getText().trim();
                String yStr = yField.getText().trim();
                if( xStr.isEmpty() || yStr.isEmpty() )
                {
                    JOptionPane.showMessageDialog(this, languageManager.getString("dialog.err.null_coordinates"), languageManager.getString("dialog.err.input"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                try
                {
                    Double.parseDouble(xStr);
                    Double.parseDouble(yStr);
                }
                catch (NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(this, languageManager.getString("dialog.err.coordinates"), languageManager.getString("dialog.err.input"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            case "Admin" -> {
                if( currentAdmin == null )
                {
                    JOptionPane.showMessageDialog(this, languageManager.getString("dialog.err.null_admin"), languageManager.getString("dialog.err.input"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }
}