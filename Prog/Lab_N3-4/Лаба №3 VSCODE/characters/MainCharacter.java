package characters;

import interfaces.Speakable;
import enums.Sizes;
import renders.ChangesRender;

final public class MainCharacter extends MyCharacter implements Speakable
{
    private boolean confusion = false;
    public MainCharacter( ChangesRender newCreationRenderer, String Name, Sizes NewSize, String NewLocation )
    {
      super(newCreationRenderer, Name, NewSize, NewLocation);
    }



    public void confused()
    {
      this.confusion = true;
    }
    public void calmedDown()
    {
      this.confusion = false;
    }
    public String describe()
    {
      return "I'm a main character";
    }
    @Override
    public String speak(String phrase) 
    {
        if (confusion) 
        {
            return " (в замешательстве): " + phrase;
        }
        return ": " + phrase;
    }    
    @Override
    public boolean equals(Object o) 
    {
        if (this == o) 
          return true;
        if (!(o instanceof MainCharacter)) 
          return false;
        return super.equals(o);
    }
    @Override
    public int hashCode() 
    {
        return super.hashCode(); 
    }
}