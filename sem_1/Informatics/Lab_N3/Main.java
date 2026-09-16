
import characters.*;

class Main 
{
    public static void main(String[] args) 
    {
        Doragli doragli = new Doragli("Doragli", "BIG", "Pizde");

        ChineseDoll[] Dolls = new ChineseDoll[50];
        for( int i = 0; i < 50; i++ )
            Dolls[i] = new ChineseDoll("Test_Doll");
    }
}