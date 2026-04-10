package exceptions;

public class KingdomOverFlowException extends KingdomException
{
    private final int maxResidents;

    public KingdomOverFlowException( String kingdomName, int kingdomMaxResidents ) 
    {
        super(kingdomName);
        this.maxResidents = kingdomMaxResidents;
    }

    public int maxResidents() 
    {
        return maxResidents;
    }

    @Override
    protected String getDetailMessage() 
    {
        return "Kingdom '" + getKingdomName() + "' is full. Max residents = '" + maxResidents + "'";
    }
}
