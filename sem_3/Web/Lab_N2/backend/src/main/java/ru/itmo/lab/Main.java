package ru.itmo.lab;

import com.fastcgi.FCGIInterface;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
    private static final List<Point> history = new CopyOnWriteArrayList<>();
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main( String[] args )
    {
        logger.info("Server starts working..");
        FCGIInterface fcgi = new FCGIInterface();
        while( fcgi.FCGIaccept() >= 0 )
        {
            long startTime = System.nanoTime();
            try
            {
                String method = FCGIInterface.request.params != null ? FCGIInterface.request.params.getProperty("REQUEST_METHOD") : null;
                if( method == null || (!method.equalsIgnoreCase("GET") && !method.equalsIgnoreCase("POST")) )
                {
                    FCGIModule.sendError("Method is not allowed. Only POST, GET requests are allowed.", 405);
                    continue;
                }

                Map<String, String> parameters = null;
                String requestBody;
                switch( method )
                {
                    case "POST":
                    {
                        requestBody = FCGIModule.getJsonFromPOST();
                        parameters = FCGIModule.getXYRfromPOSTJSON( requestBody );

                        logger.info("[DEBUG]  " + method.toString() + (!parameters.isEmpty() ? parameters.toString() : "NO PARAMETERS") );
                        if( !parameters.containsKey("x") || !parameters.containsKey("y") || !parameters.containsKey("r") )
                        {
                            logger.info("[DEBUG]  " + "error of request");
                            FCGIModule.sendError("Missing required parameters: x, y, r", 400);
                            continue;
                        }

                        double x = Double.parseDouble(parameters.get("x"));
                        double y = Double.parseDouble(parameters.get("y"));
                        double r = Double.parseDouble(parameters.get("r"));
                        Point point = new Point(x, y, r);
                        if( !point.getErrorMessage().equals("0") )
                        {
                            FCGIModule.sendError(point.getErrorMessage(), 400);
                            continue;
                        }

                        CheckPoint checker =new CheckPoint( point );
                        point.setSuccess( checker.checkPoint() );
                        point.setCreationTime( checker.getCreationTime() );
                        long executionTime = System.nanoTime() - startTime;
                        history.add( point );
                        FCGIModule.sendResponse( point.toJson(), "application/json", executionTime );
                        break;
                    }
                    case "GET":
                    {
                        requestBody = FCGIModule.getRequestGET();
                        parameters = FCGIModule.getXYRfromGETStr( requestBody );
                        if( parameters == null || parameters.isEmpty() )
                        {
                            FCGIModule.sendError("Expected parameter of request", 400);
                            continue;
                        }
                        if( parameters.get("request").equals("CREATE_TABLE") )
                        {
                            logger.info("[DEBUG]  " + "request for creating table");
                            FCGIModule.sendResponse( Formatter.createHTMLTable(history), "text/html", 0 );
                            continue;
                        }
                        FCGIModule.sendError("Expected parameter of request", 400);
                        continue;
                    }
                    default:
                    {
                        logger.info("[ERROR]  Unexpected method");
                        throw new Exception("Unexpected method");
                    }
                }

            }
            catch( NumberFormatException e )
            {
                FCGIModule.sendError("Parameters x, y, r must be valid numbers.", 400);
                logger.log(Level.WARNING, "Parameters x, y, r must be valid numbers.");
            }
            catch( Exception e )
            {
                FCGIModule.sendError("Internal Server Error: " + e.getMessage(), 500);
                logger.log(Level.WARNING, "Internal Server Error: " + e.getMessage());
            }
        }
        logger.info("Server was stopped.");
    }
}
