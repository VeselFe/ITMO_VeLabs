package ru.itmo.server.serverInterfaces;

import ru.itmo.lab.common.model.StudyGroup;

import java.util.Hashtable;
import java.util.List;

public interface ExecuteResult
{
    String getMessage();
    boolean isSuccess();
    List<StudyGroup> getStudyGroupList();
    Hashtable<Long, StudyGroup> getCollection();
}
