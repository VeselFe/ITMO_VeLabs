package exceptions;

public abstract class KingdomException extends Exception
{
    private final String kingdomName;

    protected KingdomException(String kingdomName) 
    {
        this.kingdomName = kingdomName;
    }

    public String getKingdomName() 
    {
        return kingdomName;
    }

    @Override
    public String getMessage() 
    {
        return "Kingdom '" + kingdomName + "'': " + getDetailMessage();
    }

    protected abstract String getDetailMessage();
}

