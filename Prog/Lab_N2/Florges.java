package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

final public class Florges extends Floette{
  public Florges(String name, int level) {
    super(name, level);
    setStats(78, 65, 68, 112, 154, 75);
    this.addMove(new Facade(this));
  }
  
}