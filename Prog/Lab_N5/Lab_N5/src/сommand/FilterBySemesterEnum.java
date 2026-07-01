package сommand;

import manager.CollectionManager;
import model.StudyGroup;
import myEnums.Semester;
import myExceptions.CommandException;
import terminal.IO_Handler;


/**
 * Команда для вывода элементов коллекции, отсортировав по семестру обучения
 */
public class FilterBySemesterEnum implements CommandWithArgs
{
    private final CollectionManager collection;
    private Semester sem;

    public FilterBySemesterEnum( CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void getArgs( String Args )
    {
        try
        {
            sem = Semester.valueOf( Args.toUpperCase() );
        }
        catch (IllegalArgumentException e)
        {
            throw new CommandException("Неверно введен семестр. Варианты: FIRST, SECOND, THIRD, FIFTH, EIGHTH");
        }
    }

    @Override
    public void execute( IO_Handler consol )
    {
        long count = 0;
        for(StudyGroup group : collection.getStudyGroups().values())
        {
            if( group.getSemester().equals( sem ) )
            {
                consol.printInfo(group.getInformation());
                count++;
            }
        }
        if( count == 0 )
        {
            consol.printInfo("Совпадений не найдено!");
        }
        else
        {
            consol.printInfo("Было найдено " + count + " совпадений.");
        }
    }
    @Override
    public String getName()
    {
        return "filter_by_semester_enum { semesterEnum }";
    }
    @Override
    public String getDescription()
    {
        return "вывести элементы, значение поля semesterEnum которых равно заданному";
    }
}
