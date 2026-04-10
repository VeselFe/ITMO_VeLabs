package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

public class Binacle extends Pokemon{
  public Binacle(String name, int level) {
    super(name, level);
    setType(Type.ROCK);
    addType(Type.WATER);
    setStats(42, 52, 67, 39, 56, 50);
    this.addMove(new Blizzard());
    this.addMove(new SludgeWave());  
    this.addMove(new Withdraw());   
  }
  
}