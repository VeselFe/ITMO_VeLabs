import characters.*;
import enums.Sizes;
import locations.*;
import renders.ChangesRender;
import renders.SpeechRenderer;
import enums.Sizes;

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
        Kingdom Tridevyatoe = new Kingdom(RenderForSystemState, "Tridevyatoe", new SpeechRenderer(), RenderForSystemState);
        Door MainDoor = new Door(RenderForSystemState, "Front Door", RenderForSystemState);
        MainScene.addLocation(Tridevyatoe);
        MainScene.addLocation(MainDoor);
        // RenderForSystemState.setEnabled(false);

        Tridevyatoe.addResident(Doragli);
        for( int i = 0; i < 50; i++ )
            Tridevyatoe.addResident(Dolls[i]);
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