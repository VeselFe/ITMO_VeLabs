package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

public class Rest extends StatusMove 
{

  public Rest() 
  {
    super(Type.PSYCHIC, 0, 1.0, 0, 5);
  }
  @Override public void applySelfEffects( Pokemon p ) 
  {
    p.restore();
    Effect.sleep(p);
  }
  @Override public String describe() 
  {
    return "отдыхает и восстанавливается";
  }
}