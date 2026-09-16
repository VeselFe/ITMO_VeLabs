package renders;
import interfaces.ChangeMessagesRenderFuncs;

public class ChangesRender implements ChangeMessagesRenderFuncs
{
    private boolean state = true;
    @Override
    public void renderChanges( String SystemName, String Changes )
    {
        if(state)
            System.out.println(SystemName + ": " + Changes);    
    }
    @Override
    public boolean isEnabled()
    {
        return state;
    }
    @Override
    public void setEnabled( boolean NewState )
    {
        if(NewState)
        {
            state = NewState;
            renderChanges("Change Message Handler turned ", "on");
        }
        else
        {
            renderChanges("Change Message Handler turned ", "off");  
            state = NewState;
        }
    }
}
