package locations;

import java.util.ArrayList;
import java.util.List;
import interfaces.SceneContainer;
import renders.*;
import exceptions.*;
import characters.MyCharacter;

final public class Scene implements SceneContainer
{
    private final List<MyCharacter> characters = new ArrayList<>();
    private final List<Location> locations = new ArrayList<>();
    private final SpeechRenderer sceneMessage;
    private final ChangesRender changeRender;
    private final int maxCharacters = 150;
    private boolean showChangeSceneMessage = true;

    public Scene( SpeechRenderer renderer, ChangesRender chRneder )
    {
        this.sceneMessage = renderer; 
        this.changeRender = chRneder;
        sceneMessage.printMessage("Scene", "New Scene was created");
    }

    public void readDiscribtion( Location thisLocation )
    {
        sceneMessage.printMessage("Description of " + thisLocation.getName(), thisLocation.getDesciption());
    }
    public void messageInScene( MyCharacter Speaker, String Message )
    {
        sceneMessage.printMessage(Speaker.getName(), Message);
    }
    private void sceneChangeMessage( String message )
    {
        changeRender.renderChanges("Scene", message);
    }

    @Override
    public void addLocation( Location NewLocation )
    {
        locations.add(NewLocation);
        sceneChangeMessage(NewLocation.getName() + " was added in Scene!");
    }
    @Override
    public void addCharacter( MyCharacter NewCharacter )
    {
        if( characters.size() >= maxCharacters )
            throw new SceneOverflowRuntimeException( maxCharacters );
        
        characters.add(NewCharacter);
        sceneChangeMessage(NewCharacter.getName() + " was added in Scene!");
    }
    @Override
    public void removeCharacter( MyCharacter NameCharacter )
    {
        characters.remove( NameCharacter );
        sceneChangeMessage(NameCharacter.getName() + " was removed in Scene!");
    }
}
