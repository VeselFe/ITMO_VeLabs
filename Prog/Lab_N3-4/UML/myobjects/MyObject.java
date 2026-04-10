package myobjects;

import renders.ChangesRender;
import renders.SpeechRenderer;

abstract public class MyObject
{
    private String Name = null;
    private final ChangesRender creationRenderer;

    protected MyObject( ChangesRender newCreationRenderer, String NewName )
    {
        this.creationRenderer = newCreationRenderer;
        Name = NewName;
        // System.out.println(NewName + " was created");
        creationRenderer.renderChanges(NewName, " was created");
    }
    public String getName()
    {
      return Name;
    }
    @Override
    public String toString()
    {
      return Name;
    }
}