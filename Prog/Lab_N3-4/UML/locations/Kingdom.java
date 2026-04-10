package locations;

import java.util.ArrayList;
import java.util.List;

import characters.MyCharacter;
import renders.ChangesRender;
import renders.SpeechRenderer;

final public class Kingdom extends Location 
{
    private final List<MyCharacter> residents = new ArrayList<>();
    private final SpeechRenderer KingdomRenderer;
    
    public Kingdom( ChangesRender newCreationRenderer, String NewName, SpeechRenderer renderer, ChangesRender renderLocChanges )
    {
        super(newCreationRenderer,  "Kingdom " + NewName, renderLocChanges );
        this.KingdomRenderer = renderer;
        super.setDescription("This is a kingdom for different characters and interesting stories");
    }

    public void addResident( MyCharacter NewResident )
    {
        residents.add( NewResident );
        changeLocRender.renderChanges(this.getName(), "New resident " + NewResident.getName() + " was added");
    }
    public void removeResident( MyCharacter DelResident )
    {
        residents.remove( DelResident );
        super.changeLocRender.renderChanges(this.getName(), "Resident " + DelResident.getName() + " was removed from this Kingdom");
    }
}
