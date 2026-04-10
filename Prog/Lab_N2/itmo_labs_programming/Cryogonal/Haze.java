package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

public class Haze extends StatusMove 
{

  public Haze() 
  {
    super(Type.ICE, 0, 1.0, 0, 30);
  }

  @Override public void applySelfEffects(Pokemon p) 
  {
    p.restore();
  }
  @Override public void applyOppEffects(Pokemon p) 
  {
    p.restore();
  }
  @Override public String describe() 
  {
    return "сбрасывает все изменения характеристик покемонов";
  }
}