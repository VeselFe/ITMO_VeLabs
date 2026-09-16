package ru.itmo.lab.common.commonNet;

import ru.itmo.lab.common.model.StudyGroup;
import java.util.Hashtable;

public class UpdateNotification extends AbstractResponse
{
    public UpdateNotification( Hashtable<Long, StudyGroup> data )
    {
        this.collection = data;
    }

    public Hashtable<Long, StudyGroup> getData()
    {
        return collection;
    }
}