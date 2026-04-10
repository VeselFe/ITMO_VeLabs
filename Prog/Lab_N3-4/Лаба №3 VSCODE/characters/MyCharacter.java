package characters;

import java.util.Objects;
import myobjects.*;
import enums.Sizes;
import interfaces.*;
import renders.*;

abstract public class MyCharacter extends MyObject implements Speakable
{
    private Sizes Size = Sizes.NOSIZE;
    private String Location = "nowhere";

    public MyCharacter( ChangesRender newCreationRenderer, String Name, Sizes NewSize, String NewLocation )
    {
      super(newCreationRenderer, Name);
      Size = NewSize;
      Location = NewLocation;
    }
    public MyCharacter(ChangesRender newCreationRenderer, String Name, Sizes NewSize )
    {
      super(newCreationRenderer, Name);
      Size = NewSize;
    }    
    public MyCharacter(ChangesRender newCreationRenderer,  String Name )
    {
      super(newCreationRenderer, Name);
    }  
    public MyCharacter(ChangesRender newCreationRenderer)
    {
      super(newCreationRenderer, "");
    }  

    public void setLocation( String NewLocation, SpeechRenderer renderer ) 
    {
      Location = NewLocation;
      renderer.printMessage(super.getName(), "'s location switched - new location: " + Location);
    }
     
    abstract public String describe();
    
    @Override
    public String speak( String phrase )
    {
      return phrase;
    }
    @Override
    public String toString()
    {
        return this.getName();
    }
    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false; 
        
        MyCharacter that = (MyCharacter) obj;
        return Objects.equals(getName(), that.getName()) &&  
              Size == that.Size &&                       
              Objects.equals(Location, that.Location);   
    }
    @Override
    public int hashCode() 
    {
        return Objects.hash(getName(), Size, Location); 
    }
}