package ru.itmo.server.manager.serverLogic;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.lab.common.commonNet.ResponseCmdRes;
import ru.itmo.server.dao.StudyGroupDAO;
import ru.itmo.server.dao.UserDAO;
import ru.itmo.server.serverInterfaces.CommandArgs;
import ru.itmo.server.serverInterfaces.ExecuteResult;
import ru.itmo.server.serverInterfaces.InvokerActions;

import java.sql.SQLException;

public class CommandProccessor
{
    public static final Logger logger = LoggerFactory.getLogger(CommandProccessor.class);
    private InvokerActions invoker;

    public CommandProccessor( InvokerActions invoker )
    {
        this.invoker = invoker;
    }

    public ResponseCmdRes ProcessRequest(Request clientRequest, StudyGroupDAO dbManager, UserDAO userDAO )
    {
        try
        {
            logger.debug("Получен запрос");
            logger.debug(clientRequest.toString());
            if(clientRequest.getCommandType().equals("register"))
            {
                long res = userDAO.registerUser(clientRequest.getLogin(), clientRequest.getPassword());
                return new ResponseCmdRes.Builder()
                        .setSuccess(true)
                        .setMessage("Пользователь успешно зарегистрирован.")
                        .buildResponse();
            }
            else if( clientRequest.getCommandType().equals("login") )
            {
                long res = userDAO.authenticateUser(clientRequest.getLogin(), clientRequest.getPassword());
                return new ResponseCmdRes.Builder()
                        .setSuccess(true)
                        .setMessage("Пользователь успешно авторизован.")
                        .buildResponse();
            }
            else
            {
                long userID = userDAO.authenticateUser(clientRequest.getLogin(), clientRequest.getPassword());
                CommandArgs requestArgs = new RequestAdapter( clientRequest, dbManager );
                requestArgs.setOwnerID( userID );
                ExecuteResult result = invoker.execute( requestArgs );
                if( result.getCollection() != null )
                    logger.info("Результат.Коллекция: " + result.getCollection().toString());
                ResponseCmdRes.Builder builder = new ResponseCmdRes.Builder();
                return new ResponseCmdRes.Builder()
                        .setSuccess(result.isSuccess())
                        .setMessage(result.getMessage())
                        .setSortedCollection(result.getStudyGroupList())
                        .setCollection(result.getCollection())
                        .buildResponse();
            }
        }
        catch( SQLException e )
        {
            logger.warn(e.getMessage());
            return ResponseCmdRes.builder()
                    .setSuccess(false)
                    .setMessage(e.getMessage())
                    .buildResponse();
        }
        catch( NullPointerException e )
        {
            String errorMessage;
            if( clientRequest.getLogin() == null || clientRequest.getPassword() == null )
                errorMessage = "Не удалось распознать атрибуты пользователя(логин или пароль)";
            else
                errorMessage = "Ошибка неопределенности: " + e.getMessage();
            logger.error(errorMessage);
            return ResponseCmdRes.builder()
                    .setSuccess(false)
                    .setMessage(errorMessage + "\n")
                    .buildResponse();
        }
        catch( Exception e )
        {
            logger.error(e.getMessage());
            return ResponseCmdRes.builder()
                    .setSuccess(false)
                    .setMessage("Ошибка при конвертации данных: " + e.getMessage() + "\n")
                    .buildResponse();
        }
    }
}
