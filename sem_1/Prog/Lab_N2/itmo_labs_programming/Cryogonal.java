package pokemon;

import ru.ifmo.se.pokemon.*;
import move.*;

final public class Cryogonal extends Pokemon{
  public Cryogonal(String name, int level) {
    super(name, level);
    setType(Type.ICE);
    setStats(80, 50, 50, 95, 135, 105);
    this.addMove(new AuroraBeam());
    this.addMove(new Facade(this));
    this.addMove(new AncientPower());
    this.addMove(new Haze());
  }
  
}