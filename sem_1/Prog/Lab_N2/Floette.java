package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

public class Floette extends Flabebe{
  public Floette(String name, int level) {
    super(name, level);
    setStats(54, 45, 47, 75, 98, 52);
    this.addMove(new VineWhip());
  }
  
}