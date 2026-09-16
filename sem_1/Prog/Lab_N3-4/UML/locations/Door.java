package locations;

import characters.MyCharacter;
import renders.ChangesRender;

final public class Door extends Location
{
    private boolean isOpened = false;

    public Door( ChangesRender newCreationRenderer, String NewName, ChangesRender renderer )
    {
        super(newCreationRenderer, NewName, renderer);
        // renderer.renderChanges(NewName, "was created");
    }    

    public void goToDoor( MyCharacter character )
    {
        changeLocRender.renderChanges(super.getName(), character.getName() + " went to the " + super.getName());
    }
    public void knocked( MyCharacter guest )
    {
        changeLocRender.renderChanges(guest.getName(), " knocked on the door " + this.getName());
    }
    public void opened( MyCharacter DoorOpener )
    {
        isOpened = true;
        changeLocRender.renderChanges(DoorOpener.getName(), " opened the door " + this.getName());
    }
    public void closed( MyCharacter DoorCloser )
    {
        isOpened = false;
        changeLocRender.renderChanges(DoorCloser.getName(), " closed the door " + this.getName());
    }

}
