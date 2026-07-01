package terminal;

import model.Person;
import model.StudyGroup;
import myExceptions.CreationException;
import myRecords.FieldDescriptor;
import myRecords.Lab5FieldDescriptor;
import manager.Invoker;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * Консольный обработчик ввода-вывода для лабораторной работы №5.
 *
 * <p>Реализует интерфейс {@link IO_Handler} и расширяет {@link GenericConsoleHandler}
 * для обработки команд интерактивного режима работы программы.</p>
 *
 * <p><b>Основное назначение:</b></p>
 * <ul>
 *   <li>Чтение пользовательских команд и их выполнение через {@link Invoker}</li>
 *   <li>Интерактивный ввод сложных объектов: {@link Person}, {@link StudyGroup}</li>
 *   <li>Форматированный вывод информационных сообщений, ошибок и запросов</li>
 *   <li>Управление жизненным циклом консольного приложения</li>
 * </ul>
 */
public class Lab5ConsoleHandler extends GenericConsoleHandler<Invoker>
    implements IO_Handler
{
    private final Scanner scanner = new Scanner(System.in);
    private boolean stop = false;

    public Lab5ConsoleHandler( Invoker invoker )
    {
        super(invoker);
    }

    @Override
    protected boolean executing( String input )
    {
        invoker.executeCommand(input);
        return stop;
    }

    public void printInfo( String message )
    {
        print( "<i> " + message );
    }
    public void printError( String messege )
    {
        print("<Ошибка>:\n" + messege);
    }
    public void printRequest( String request )
    {
        printCurLine( request );
    }
    public String readline()
    {
        return scanner.nextLine();
    }
    public void stop() { stop = true; }

    public Person readPerson() throws CreationException
    {
        return new PersonReader(this).readPerson();
    }
    public StudyGroup readNewStudyGroup()
    {
        return new StudyGroupReader(this).readStudygroup();
    }
}
