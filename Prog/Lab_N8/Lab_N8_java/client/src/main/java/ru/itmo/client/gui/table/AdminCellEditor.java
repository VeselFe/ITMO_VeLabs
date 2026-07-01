package ru.itmo.client.gui.table;

import ru.itmo.client.gui.dialog.AdminDialog;
import ru.itmo.lab.common.model.Person;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class AdminCellEditor extends AbstractCellEditor implements TableCellEditor
{
    private Person person;
    private final Frame owner;

    public AdminCellEditor( Frame owner ) { this.owner = owner; }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col)
    {
        AdminDialog adminDialog = new AdminDialog(owner);
        adminDialog.setVisible(true);
        this.person = adminDialog.getPerson();

        fireEditingStopped();
        return new JLabel(person != null ? person.getName() : "Нет админа");
    }

    @Override
    public Object getCellEditorValue() { return person; }
}
