package terminal;

import java.util.Scanner;

/**
 * Абстрактный базовый класс для обработки консольного ввода команд.
 *
 * Предоставляет общую логику интерактивного консольного интерфейса для
 * различных типов инвокаторов команд. Наследники реализуют специфическую
 * логику выполнения команд через абстрактный метод {@link #executing(String)}.
 */
abstract public class GenericConsoleHandler<T>
{
    protected final T invoker;
    private boolean exit = false;

    public GenericConsoleHandler( T newInvoker )
    {
        invoker = newInvoker;
    }

    public void start()
    {
        Scanner scanner = new Scanner(System.in);

        welcomMessage();
        while (!exit)
        {
            print("\n         Введите команду");
            print("=====================================");
            String input = getInput(scanner);
            try {
                exit = executing(input);
            }
            catch( Exception e )
            {
                error(e.getMessage());
            }
        }

    }

    protected String getInput( Scanner scanner )
    {
        return scanner.nextLine();
    }
    final protected void print( String message ) { System.out.println(message); }
    final protected void printCurLine( String message ) { System.out.print(message); }
    protected void error( String messege ) { print("Ошибка: " + messege); }
    protected void welcomMessage() { print("Go!"); }

    abstract protected boolean executing( String input );
}
