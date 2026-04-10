package exceptions;

public class SceneOverflowRuntimeException extends RuntimeException
{
    public SceneOverflowRuntimeException( int maxCharacters )
    {
        super("Scene is full. Max characters = " + maxCharacters);
    }
}
