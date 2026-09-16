package interfaces;

import characters.MyCharacter;
import locations.Location;

public interface SceneContainer
{
    void addCharacter( MyCharacter NewCharacter );
    void addLocation( Location NewLocation );
    void removeCharacter( MyCharacter NameCharacter );    
}
