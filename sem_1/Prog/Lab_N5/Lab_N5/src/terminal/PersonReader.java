package terminal;

import model.Person;
import myExceptions.CreationException;
import myRecords.FieldDescriptor;

import static myRecords.Lab5FieldDescriptor.NEW_PERSON_FIELDS;


/**
 * Класс для чтения значений для полей {@link model.Person} из консоли
 * Используется для создания нового администратора
 */
public class PersonReader
{
    private IO_Handler reader;
    public PersonReader( IO_Handler newConsole )
    {
        reader = newConsole;
    }

    public Person readPerson()
    {
        String input;
        try
        {
            Person.Builder personGenerator = new Person.Builder();

            for(FieldDescriptor field : NEW_PERSON_FIELDS)
            {
                do
                {
                    try
                    {
                        reader.printRequest(field.request());
                        input = reader.readline();
                        switch (field.name().toLowerCase())
                        {
                            case "name" -> personGenerator.setName(input);
                            case "birthday" -> personGenerator.setBirthday(input);
                            case "weight" -> personGenerator.setWeight(input);
                            case "passport id" -> personGenerator.setPassportID(input);
                            case "nationality" -> personGenerator.setNationality(input);
                            default -> throw new IllegalArgumentException("Неизвестное поле: " + field.name());
                        };
                        break;
                    }
                    catch ( Exception e )
                    {
                        reader.printError(e.getMessage() + "\nПопробуйте снова ввести");
                    }
                } while (true);
            }

            return personGenerator.build();
        }
        catch( Exception e )
        {
            throw new CreationException("Ошибка при попытке инициализации студента: \n>>\n" + e + "\n<<\n");
        }
    }
}
