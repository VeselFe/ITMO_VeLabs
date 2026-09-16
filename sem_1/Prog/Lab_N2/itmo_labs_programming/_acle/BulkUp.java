package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

public class BulkUp extends StatusMove 
{

  public BulkUp() 
  {
    super(Type.FIGHTING, 0, 1.0, 0, 20);
  }
  @Override public void applySelfEffects( Pokemon p ) 
  {
    p.setMod(Stat.ATTACK, +1);
    p.setMod(Stat.DEFENSE, +1);
  }
  @Override public String describe() 
  {
    return "увеличился";
  }
}