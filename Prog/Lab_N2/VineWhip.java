package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

public class VineWhip extends PhysicalMove 
{

  public VineWhip() 
  {
    super(Type.GRASS, 45, 1.0, 0, 25);
  }
  @Override public String describe() 
  {
    return "применяет виноградный хлыст";
  }
}