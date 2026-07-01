package terminal;

import model.StudyGroup;
import myExceptions.CreationException;
import myRecords.FieldDescriptor;

import static myRecords.Lab5FieldDescriptor.NEW_GROUP_FIELDS;

/**
 * Класс для чтения значений для полей {@link model.StudyGroup} из консоли
 * Используется для создания новой группы
 */
public class StudyGroupReader
{
    private IO_Handler reader;
    public StudyGroupReader( IO_Handler newConsole )
    {
        reader = newConsole;
    }

    public StudyGroup readStudygroup()
    {
        String input;

        StudyGroup.Builder groupGenerator = new StudyGroup.Builder();

        for (FieldDescriptor field : NEW_GROUP_FIELDS)
        {
            do
            {
                try
                {
                    reader.printRequest(field.request());
                    input = reader.readline();
                    switch (field.name().toLowerCase())
                    {
                        case "name" -> groupGenerator.setName(input);
                        case "coordinates" -> groupGenerator.setCoordinates(input);
                        case "studentcount" -> groupGenerator.setStudCount(input);
                        case "shoudbeexpelled" -> groupGenerator.setShBeExp(input);
                        case "formofeducation" -> groupGenerator.setFormOfEdu(input);
                        case "semester" -> groupGenerator.setSem(input);
                        default -> throw new IllegalArgumentException("Неизвестное поле: " + field.name());
                    }
                    break;
                }
                catch (Exception e)
                {
                    reader.printError(e.getMessage() + "\nПопробуйте снова ввести");
                }
            } while (true);
        }
        try 
        {
            reader.printInfo("Заполните анкету о новом админе");
            groupGenerator.setAdmin(reader.readPerson());
            return groupGenerator.build();
        }
        catch( Exception e )
        {
            throw new CreationException("Ошибка при попытке инициализации группы");
        }
    }
}
