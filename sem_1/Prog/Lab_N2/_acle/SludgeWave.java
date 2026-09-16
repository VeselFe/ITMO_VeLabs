package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final public class SludgeWave extends SpecialMove 
{

  public SludgeWave() 
  {
    super(Type.POISON, 95, 1.0, 0, 10);
  }
  @Override public void applyOppEffects( Pokemon p ) 
  {
    if (Math.random() < 0.1) 
    {
      Effect.poison(p);
    }
  }
  @Override public String describe() 
  {
    return "применил осадочную волну";
  }
}