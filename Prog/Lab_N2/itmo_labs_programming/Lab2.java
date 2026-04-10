package lab2;
import ru.ifmo.se.pokemon.*;
import pokemon.*;
import move.*;

public class Lab2 {
 public static void main(String[] args) {
   Battle b = new Battle();
  //  Pokemon p1 = new Pokemon("Чужой", 5);
  //  Pokemon p2 = new Pokemon("Хищник", 5);
  //  b.addAlly(p1);
  //  b.addAlly(p2);      
  
   b.addAlly(new Flabebe("Дряблый", 5));
   b.addAlly(new Barbaracle("Варвар", 5));   
   b.addAlly(new Floette("Флотилия", 2));      
   b.addFoe(new Binacle("Связующий", 5));
   b.addFoe(new Florges("Флооржесс", 5));
   b.addFoe(new Cryogonal("Монстрик", 5));

   b.go();
 }
}

// javac -cp ".;Pokemon.jar" -d . *.java
// javac -cp ".:Pokemon.jar" -d . *.java */*.java
// java -cp ".;Pokemon.jar" lab2.Lab2