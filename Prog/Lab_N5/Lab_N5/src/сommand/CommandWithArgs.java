package сommand;

/**
 * Интерфейс для класса пользовательской команды, принемающей аргументы из консоли
 */
public interface CommandWithArgs extends Command
{
    void getArgs( String Args );
}
