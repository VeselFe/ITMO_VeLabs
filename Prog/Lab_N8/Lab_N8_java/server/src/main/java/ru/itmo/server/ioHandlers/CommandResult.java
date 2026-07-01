package ru.itmo.server.ioHandlers;

import ru.itmo.server.serverInterfaces.ExecuteResult;
import ru.itmo.lab.common.model.StudyGroup;

import java.util.Hashtable;
import java.util.List;

public class CommandResult implements ExecuteResult
{
    private final boolean success;
    private final String message;
    private final List<StudyGroup> studyGroupList;
    private final Hashtable<Long, StudyGroup> collection;

    public static CommandResult.Builder builder()
    {
        return new CommandResult.Builder();
    }
    private CommandResult( CommandResult.Builder builder )
    {
        this.success = builder.success;
        this.message = builder.message;
        this.studyGroupList = builder.studyGroupList;
        this.collection = builder.collection;
    }

    public static class Builder
    {
        private boolean success = false;
        private String message = "";
        private List<StudyGroup> studyGroupList = null;
        private Hashtable<Long, StudyGroup> collection = null;

        public CommandResult buildCommandResult()
        {
            return new CommandResult( this );
        }

        public CommandResult.Builder setSuccess( Boolean success )
        {
            this.success = success;
            return this;
        }
        public CommandResult.Builder setMessage( String message )
        {
            this.message = message;
            return this;
        }
        public CommandResult.Builder setStudyGroupList( List<StudyGroup> collection )
        {
            this.studyGroupList = collection;
            return this;
        }
        public CommandResult.Builder setCollection( Hashtable<Long, StudyGroup> collection )
        {
            this.collection = collection;
            return this;
        }
    }

    @Override
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<StudyGroup> getStudyGroupList() { return studyGroupList; }
    public Hashtable<Long, StudyGroup> getCollection() { return collection; }
}
