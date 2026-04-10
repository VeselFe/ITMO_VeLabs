package exceptions;

public class DuplicateResidentException extends KingdomException
{
    private final String residentName;

    public DuplicateResidentException(String kingdomName, String residentName) 
    {
        super(kingdomName);
        this.residentName = residentName;
    }

    public String getResidentName() 
    {
        return residentName;
    }

    @Override
    protected String getDetailMessage() 
    {
        return "resident '" + residentName + "'' already exists";
    }
}
