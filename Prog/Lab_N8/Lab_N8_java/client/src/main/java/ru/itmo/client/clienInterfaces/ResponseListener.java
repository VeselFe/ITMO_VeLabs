package ru.itmo.client.clienInterfaces;

import ru.itmo.lab.common.commonNet.ResponseCmdRes;
import ru.itmo.lab.common.model.StudyGroup;

import java.util.Hashtable;

public interface ResponseListener
{
    void handleResponse( ResponseCmdRes responseCmdRes);
    void onCollectionUpdated( Hashtable<Long, StudyGroup> newCollection );
    void onError( String message );
}