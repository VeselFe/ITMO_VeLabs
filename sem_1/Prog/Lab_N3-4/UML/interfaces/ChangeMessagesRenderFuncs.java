package interfaces;

public interface ChangeMessagesRenderFuncs 
{
    void renderChanges( String SystemName, String Changes );
    boolean isEnabled();
    void setEnabled( boolean NewState );
}
