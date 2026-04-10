package characters;

import Creature.*;

abstract public class MyCharacter extends Creature
{
    private String Size = NO_SIZE;
    private String Location = "nowhere";
    private String Emotion = "NoEmotion";

    public MyCharacter( String Name, String NewSize, String NewLocation )
    {
      super(Name);
      Size = NewSize;
      Location = NewLocation;
    }
    public MyCharacter( String Name, String NewSize )
    {
      super(Name);
      Size = NewSize;
    }    
    public MyCharacter( String Name )
    {
      super(Name);
    }  
    public MyCharacter()
    {
      super("");
    }  

    public void set_location( String NewLocation ) 
    {
      Location = NewLocation;
      System.out.println(Name + "'s location switched - new location: " + Location);
    }
}