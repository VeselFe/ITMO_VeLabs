package renders;

import interfaces.MessageHandler;

public class SpeechRenderer implements MessageHandler 
{
    @Override
    public void printMessage( String Speaker, String Message )
    {
        System.out.println(Speaker + ": " + Message);
    }
}
