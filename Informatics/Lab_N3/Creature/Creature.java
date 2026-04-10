package Creature;

abstract public class Creature
{
    protected String Name = null;
    protected Creature( String NewName )
    {
        Name = NewName;
        System.out.println("New object '" + Name + "' created!");
    }
}