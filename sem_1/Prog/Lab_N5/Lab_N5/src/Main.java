import generators.BasicGenerator;
import manager.CollectionManager;
import manager.GroupsFileManager;
import manager.Invoker;
import manager.Launcher;
import model.*;
import myEnums.*;
import terminal.Lab5ConsoleHandler;

public class Main
{
    /**
     * Главный метод программы.
     * Выполняет последовательную инициализацию всех компонентов системы:
     * <ol>
     *     <li>Создание менеджера коллекции StudyGroup из файла</li>
     *     <li>Инициализация Invoker с паттерном Command</li>
     *     <li>Создание консольного интерфейса</li>
     *     <li>Инициализация ввода-вывода для команд</li>
     *     <li>Интерактивный запуск программы</li>
     * </ol>
     *
     * При возникновении любой ошибки инициализации выводит сообщение об ошибке
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args)
    {
        try
        {
            /**
             * Создает коллекцию StudyGroup из XML файла.
             * Файл ищется автоматически согласно логике CollectionManager.
             * @see CollectionManager#createCollection()
             */
            CollectionManager myCollection = CollectionManager.createCollection();
            StudyGroup.setIdGenerator( new BasicGenerator(myCollection) );
            /**
             * Инициализирует систему команд.
             * Регистрирует все доступные команды в таблице команд.
             * запускает команды на исполнение.
             * @see Invoker
             */
            Invoker invoker = new Invoker(myCollection);
            /**
             * Создает консольный интерфейс для интерактивной работы.
             * Обрабатывает команды и ввод пользователя.
             * Выводит результат команд.
             * @see Lab5ConsoleHandler
             */
            Lab5ConsoleHandler console = new Lab5ConsoleHandler(invoker);
            GroupsFileManager.setErrorPrinter(console);
            /**
             * Настраивает потоки ввода-вывода для команд.
             * Связывает консоль с системой команд.
             */
            invoker.initIOput(console);

            new Launcher(myCollection, console).launchCollection();
            /**
             * Запускает основной цикл интерактивной консоли.
             * Обрабатывает команды пользователя до команды exit.
             */
            console.start();
        }
        catch ( Exception e )
        {
            System.out.println(e.getMessage());
        }
    }
}