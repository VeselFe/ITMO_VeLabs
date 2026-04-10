package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

final public class Barbaracle extends Binacle{
  public Barbaracle(String name, int level) {
    super(name, level);
    setStats(72, 105, 115, 54, 86, 68);  
    this.addMove(new BulkUp()); 
  }
  
}