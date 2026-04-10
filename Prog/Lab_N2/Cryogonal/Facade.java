package move;

import ru.ifmo.se.pokemon.*;
import pokemon.*;

final public class Facade extends PhysicalMove 
{
  public Facade( Pokemon p ) 
  {
    super(Type.NORMAL, 70, 1.0, 0, 20);
    Status st = p.getCondition(); 
    if ( st == Status.BURN || st == Status.PARALYZE || st == Status.POISON )
      this.power = 140;    
  }
  @Override public String describe() 
  {
    return "применяет физическую атаку";
  }
}