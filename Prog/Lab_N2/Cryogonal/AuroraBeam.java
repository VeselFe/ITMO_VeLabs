package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final public class AuroraBeam extends SpecialMove 
{

  public AuroraBeam() 
  {
    super(Type.ICE, 65, 1.0, 0, 20);
  }

  @Override public void applyOppEffects(Pokemon p) 
  {
    if (Math.random() < 0.1) 
    {
      p.setMod(Stat.ATTACK, -1);
    }
  }
  @Override public String describe() 
  {
    return "применяет ледяную атаку";
  }
}