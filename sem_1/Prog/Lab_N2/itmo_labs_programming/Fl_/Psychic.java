package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

public class Psychic extends SpecialMove 
{

  public Psychic() 
  {
    super(Type.PSYCHIC, 90, 1.0, 0, 10);
  }
  @Override public void applyOppEffects( Pokemon p ) 
  {
    if (Math.random() < 0.1) 
    {
      p.setMod(Stat.SPECIAL_DEFENSE , -1);
    }
  }
  @Override public String describe() 
  {
    return "применил психологическую атаку";
  }
}