package terminal;


import model.Person;
import model.StudyGroup;

/**
 * Интерфейс обработки консольного ввода-вывода для команд имеющий вводные значение и вывод резуьтата
 */
public interface IO_Handler {
    String readline();
    StudyGroup readNewStudyGroup();
    Person readPerson();

    void printInfo( String messege );
    void printRequest( String request );
    void printError( String messege );

    void stop();
}
