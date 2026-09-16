package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final public class Blizzard extends SpecialMove 
{

  public Blizzard() 
  {
    super(Type.ICE, 110, 0.70, 0, 5);
  }
  @Override public void applyOppEffects( Pokemon p ) 
  {
    if (Math.random() < 0.1) 
    {
      Effect.freeze(p);
    }
  }
  @Override public String describe() 
  {
    return "применил снежный ком";
  }
}