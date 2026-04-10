

import characters.*;
import renders.*;
import locations.*;
import enums.Sizes;
import exceptions.KingdomException;

class Main
{
    public static void main(String[] args) 
    {
        ChangesRender RenderForSystemState = new ChangesRender();
        RenderForSystemState.setEnabled(false);
        Scene MainScene = new Scene( new SpeechRenderer(), RenderForSystemState );

        MainCharacter Doragli = new MainCharacter(RenderForSystemState, "Дорагли", Sizes.BIG, "Nowhere");
        MainScene.addCharacter(Doragli);
        RenderForSystemState.setEnabled(false);

        ChineseDoll[] Dolls = new ChineseDoll[50];
        for( int i = 0; i < 50; i++ )
            Dolls[i] = new ChineseDoll(RenderForSystemState, "Test_Doll");
        for( int i = 0; i < 50; i++ )
            MainScene.addCharacter(Dolls[i]);
        KingdomConfig NewKingdomConfig = new KingdomConfig(52, true, false, "Tridevyatoe");
        Kingdom Tridevyatoe = new Kingdom(RenderForSystemState, RenderForSystemState, NewKingdomConfig);
        Door MainDoor = new Door(RenderForSystemState, "Front Door", RenderForSystemState);
        MainScene.addLocation(Tridevyatoe);
        MainScene.addLocation(MainDoor);
        // RenderForSystemState.setEnabled(false);

        SpeechRenderer ExceptionRender = new SpeechRenderer();
        try
        {
            Tridevyatoe.addResident(Doragli);
            for( int i = 0; i < 50; i++ )
                Tridevyatoe.addResident(Dolls[i]);        
        }
        catch( KingdomException e )
        {
            ExceptionRender.printMessage("ERROR", e.getMessage());
        }

        // MainScene.readDiscribtion(Tridevyatoe);
        
        for( int i = 0; i < 50; i++ )
            MainDoor.goToDoor(Dolls[i]);
        // RenderForSystemState.setEnabled(true);
        Doragli.confused();
        MainScene.messageInScene(Doragli, Doragli.speak("Что это со мной? Где я нахожусь?"));
        MainDoor.knocked(Dolls[0]);
        MainDoor.goToDoor(Doragli);
        MainDoor.opened(Doragli);
        MainScene.messageInScene(Dolls[1], Dolls[1].greet(Doragli));
        MainScene.messageInScene(Dolls[1], Dolls[1].speak("Мы выражаем надежду, что тебе понравится у нас в королевстве"));
        Doragli.calmedDown();
        MainScene.messageInScene(Doragli, Doragli.speak("Спасибо"));
    }
}