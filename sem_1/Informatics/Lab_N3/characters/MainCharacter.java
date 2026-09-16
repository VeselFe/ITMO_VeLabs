package characters;

final public class MainCharacter extends MyCharacter
{
    private boolean confusion = false;
    public MainCharacter( String Name, String NewSize, String NewLocation )
    {
      super(Name,NewSize,NewLocation);
    }

    public void confused()
    {
      confusion = true;
      this.move_description( Name + " was confused" );
    }
    public void laughs()
    {
      this.move_description( Name + " laughs");
    }
    
}