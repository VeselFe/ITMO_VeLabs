package locations;
import java.util.Objects;
import myobjects.MyObject;
import renders.ChangesRender;

public class Location extends MyObject
{
    private String Description = "Here will be description of location";
    protected final ChangesRender changeLocRender;
    Location( ChangesRender newCreationRenderer, String NewName, ChangesRender renderer )
    {
        this.changeLocRender = renderer;
        super(newCreationRenderer, NewName);
    }
    public String getDesciption()
    {
        return Description;
    }
    public void setDescription( String NewDescription )
    {
        Description = NewDescription;
    }
    @Override
    public String toString()
    {
        return this.getName() + "\n" + this.getDesciption();
    }
    @Override
    public boolean equals(Object o) 
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(this.getName(), location.getName()); 
    }
    @Override
    public int hashCode() 
    {
        return Objects.hash(this.getName()); 
    }    
}
