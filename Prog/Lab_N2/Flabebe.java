package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

public class Flabebe extends Pokemon{
  public Flabebe(String name, int level) {
    super(name, level);
    setType(Type.FAIRY);
    setStats(44, 38, 39, 61, 79, 42);
    this.addMove(new Psychic()); 
    this.addMove(new Rest());  
  }
  
}