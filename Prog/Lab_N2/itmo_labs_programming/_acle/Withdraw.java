package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

public class Withdraw extends SpecialMove 
{

  public Withdraw() 
  {
    super(Type.WATER, 0, 1.0, 0, 40);
  }
  @Override public void applySelfEffects( Pokemon p ) 
  {
    p.setMod(Stat.DEFENSE, +1);
  }
  @Override public String describe() 
  {
    return "применил осадочную волну";
  }
}