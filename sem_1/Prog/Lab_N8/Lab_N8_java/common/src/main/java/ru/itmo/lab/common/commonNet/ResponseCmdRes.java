package ru.itmo.lab.common.commonNet;

import ru.itmo.lab.common.model.StudyGroup;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

public class ResponseCmdRes extends AbstractResponse
{
    private final boolean success;
    private final String message;
    private final List<StudyGroup> studyGroupsList;

    public static Builder builder()
    {
        return new Builder();
    }
    private ResponseCmdRes(Builder builder )
    {
        this.success = builder.success;
        this.message = builder.message;
        this.studyGroupsList = builder.studyGroupsList;
        this.collection = builder.collection;
    }

    public static class Builder
    {
        private boolean success = false;
        private String message = "";
        private List<StudyGroup> studyGroupsList = null;
        private Hashtable<Long, StudyGroup> collection = null;

        public ResponseCmdRes buildResponse()
        {
            return new ResponseCmdRes( this );
        }

        public Builder setSuccess( Boolean success )
        {
            this.success = success;
            return this;
        }
        public Builder setMessage( String message )
        {
            this.message = message;
            return this;
        }
        public Builder setSortedCollection( List<StudyGroup> collection )
        {
            this.studyGroupsList = collection;
            return this;
        }
        public Builder setCollection( Hashtable<Long, StudyGroup> collection )
        {
            this.collection = collection;
            return this;
        }
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<StudyGroup> getSortedList() { return studyGroupsList; }
    public Hashtable<Long, StudyGroup> getCollection() { return collection; }
}
