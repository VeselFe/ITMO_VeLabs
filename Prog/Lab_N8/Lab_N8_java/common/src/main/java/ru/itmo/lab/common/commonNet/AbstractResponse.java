package ru.itmo.lab.common.commonNet;

import ru.itmo.lab.common.model.StudyGroup;

import java.io.Serializable;
import java.util.Hashtable;

public abstract class AbstractResponse implements Serializable
{
    protected static final long serialVersionUID = 666L;
    protected Hashtable<Long, StudyGroup> collection;
}
