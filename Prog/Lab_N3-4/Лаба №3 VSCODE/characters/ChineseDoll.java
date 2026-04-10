package characters;

import enums.Sizes;
import interfaces.Greetable;
import renders.ChangesRender;

final public class ChineseDoll extends MyCharacter implements Greetable
{

    static int number = 1;
    public ChineseDoll( ChangesRender newCreationRenderer, String Name, Sizes NewSize, String NewLocation )
    {
      super( newCreationRenderer, Name +  " #" + number, NewSize, NewLocation );
      number += 1;
    }
    public ChineseDoll( ChangesRender newCreationRenderer, String Name )
    {
      super( newCreationRenderer, Name +  " #" + number );
      number += 1;
    }
    public ChineseDoll( ChangesRender newCreationRenderer )
    {
      super( newCreationRenderer );
      number += 1;
    }    

    @Override
    public String describe()
    {
      return "I'm a little chinese doll!";
    }    
    @Override 
    public String greet( MyCharacter person )
    {
      return "Приветствует " + person.getName();
    }
}