package locations;

import java.util.ArrayList;
import java.util.List;
import characters.MyCharacter;
import renders.*;
import exceptions.*;

final public class Kingdom extends Location 
{
    private final List<MyCharacter> residents = new ArrayList<>();
    private final KingdomConfig config;
    
    public Kingdom( ChangesRender newCreationRenderer, 
                    ChangesRender renderLocChanges, 
                    KingdomConfig config )
    {
        super(newCreationRenderer,  "Kingdom " + config.defaultLocationName(), renderLocChanges );
        this.config = config;
        super.setDescription("This is a kingdom for different characters and interesting stories");
    }

    public void addResident( MyCharacter NewResident ) throws KingdomOverFlowException, DuplicateResidentException
    {
        if( residents.size() >= config.maxResidents() )
            throw new KingdomOverFlowException(getName(), config.maxResidents());

        if( !config.allowDuplicates() && residents.stream().anyMatch(x -> equals(NewResident)) )
            throw new DuplicateResidentException(NewResident.getName(), getName());

        residents.add( NewResident );
        changeLocRender.renderChanges(getName(), "New resident " + NewResident.getName() + " was added");
    }
    public void removeResident( MyCharacter DelResident )
    {
        residents.remove( DelResident );
        super.changeLocRender.renderChanges(this.getName(), "Resident " + DelResident.getName() + " was removed from this Kingdom");
    }
    @Override
    public void setDescription( String NewDescription )
    {
        if(config.changableDescription())
            this.Description = NewDescription;
    }
}
