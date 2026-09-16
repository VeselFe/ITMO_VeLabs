package ru.itmo.client.gui.table;

import ru.itmo.client.gui.dialog.AdminDialog;
import ru.itmo.client.gui.utils.Session;
import ru.itmo.client.network.NetworkManager;
import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.lab.common.model.Person;
import ru.itmo.lab.common.model.StudyGroup;
import ru.itmo.lab.common.myRecords.Lab5FieldDescriptor;
import ru.itmo.lab.common.myRecords.UpdatedFieldDescriptor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

public class StudyGroupTable extends AbstractTableModel
{
    private final NetworkManager networkManager;
    private final String[] columnNames = {"Key", "ID", "Имя", "X", "Y", "Дата создания", "Количество студентов", "Количество студентов на отчисление", "Семестр", "Форма обучения", "Админ", "Создал"};
    private List<Entry> dataList = new ArrayList<>();

    public StudyGroupTable(NetworkManager networkManager)
    {
        this.networkManager = networkManager;
    }

    private static class Entry
    {
        Long key;
        StudyGroup group;
        Entry( Long key, StudyGroup group )
        {
            this.key = key;
            this.group = group;
        }
    }

    public void setData( Hashtable<Long, StudyGroup> newData )
    {
        this.dataList = new ArrayList<>();
        for( Long key : newData.keySet() )
            dataList.add( new Entry(key, newData.get(key)) );

        fireTableDataChanged();
    }

    public void updateAllData( Hashtable<Long, StudyGroup> newData )
    {
        this.dataList.clear();
        for( Long key : newData.keySet() )
        {
            dataList.add( new Entry(key, newData.get(key)) );
        }
        fireTableDataChanged();
    }

    public void clear()
    {
        this.dataList.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() { return dataList.size(); }

    @Override
    public int getColumnCount() { return columnNames.length; }

    @Override
    public String getColumnName( int column ) { return columnNames[column]; }

    @Override
    public Object getValueAt( int rowIndex, int columnIndex )
    {
        Entry entry = dataList.get(rowIndex);
        long key = entry.key;
        StudyGroup group = entry.group;

        return switch( columnIndex )
        {
            case 0 -> key;
            case 1 -> group.getId();
            case 2 -> group.getName();
            case 3 -> group.getCoordinates().getX();
            case 4 -> group.getCoordinates().getY();
            case 5 -> group.getCreationFormattedDate();
            case 6 -> group.getStudentsCount();
            case 7 -> group.getShouldBeExp();
            case 8 -> group.getSemester();
            case 9 -> group.getFormOfEducation().toString().replace("_", " ");
            case 10 -> (group.getAdmin() != null) ? group.getAdmin().getName() : "Нет админа";
            case 11 -> group.getOwnerName();
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable( int rowIndex, int columnIndex )
    {
        return columnIndex != 0 && columnIndex != 1 && columnIndex != 5 && columnIndex != 11;
    }

    @Override
    public void setValueAt( Object aValue, int rowIndex, int columnIndex )
    {
        Entry entry = dataList.get(rowIndex);

        String fieldName = getColumnFieldName(columnIndex);
        UpdatedFieldDescriptor descriptor = Lab5FieldDescriptor.UPDATED_FIELDS.stream()
                .filter(f -> f.name().equalsIgnoreCase(fieldName))
                .findFirst()
                .orElse(null);

        Request.Builder requestBuilder = new Request.Builder()
                .setCommandType("update_id")
                .setID(entry.key)
                .setLogin(Session.getInstance().getLogin())
                .setPassword(Session.getInstance().getPassword())
                .setUpdatedField(descriptor);
        //
        if( descriptor != null )
        {
            if (columnIndex == 3 || columnIndex == 4)
            {
                String x = (columnIndex == 3) ? aValue.toString() : getValueAt(rowIndex, 3).toString();
                String y = (columnIndex == 4) ? aValue.toString() : getValueAt(rowIndex, 4).toString();

                requestBuilder.setArgument(x + " " + y)
                              .buildRequest();
            }
            else if( columnIndex == 10 )
            {
                if( aValue instanceof Person )
                {
                    requestBuilder.setPerson((Person) aValue);
                }
            }
            else
            {
                requestBuilder.setArgument(aValue.toString()).buildRequest();
            }
            networkManager.sendRequest(requestBuilder.buildRequest());
        }
    }

    private String getColumnFieldName( int columnIndex )
    {
        return switch (columnIndex)
        {
            case 2 -> "Name";
            case 3 -> "Coordinates";
            case 4 -> "Coordinates";
            case 6 -> "StudentCount";
            case 7 -> "ShoudBeExpelled";
            case 8 -> "Semester";
            case 9 -> "FormOfEducation";
            case 10 -> "Admin";
            default -> "";
        };
    }
}