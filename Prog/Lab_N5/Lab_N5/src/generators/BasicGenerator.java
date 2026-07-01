package generators;

import manager.CollectionManager;
import model.StudyGroup;
import myExceptions.CreationException;

public class BasicGenerator implements IdGeneratorInterface
{
    private CollectionManager manager;

    public BasicGenerator( CollectionManager newManager )
    {
        manager = newManager;
    }
    /**
     * Генерирует уникальный ID, не совпадающий с ID существующих групп.
     *
     * <p>Алгоритм: последовательный перебор с начала до первого свободного ID.</p>
     *
     * @return первый свободный положительный ID
     * @throws CreationException если все ID заняты (переполнение)
     */

    @Override
    public long generateUniqueId()
    {
        if (manager == null) { return 1L; }

        long id = 1;
        boolean flag = true, searching = true;
        while(searching)
        {
            flag = true;
            for (StudyGroup element : manager.getStudyGroups().values())
            {
                if (id == element.getId())
                {
                    flag = false;
                    break;
                }
            }
            if( flag )
            {
                searching = false;
            }
            else
            {
                id++;
                if( id < 0 )
                {
                    throw new CreationException("Нет свободных ID");
                }
            }
        }

        return id;
    }
}
