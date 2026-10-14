package ru.itmo.lab;

import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FCGIModule
{
    public static String getJsonFromPOST() throws IOException
    {
        String contentLengthStr = FCGIInterface.request.params.getProperty("CONTENT_LENGTH");
        if( contentLengthStr != null && !contentLengthStr.isEmpty() )
        {
            int length = Integer.parseInt(contentLengthStr);
            byte[] buffer = new byte[length];
            int bytesRead = 0;
            while( bytesRead < length )
            {
                int read = System.in.read(buffer, bytesRead, length - bytesRead);
                if (read == -1) break;
                bytesRead += read;
            }
            return new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
        }
        return "";
    }
    public static Map<String, String> getXYRfromPOSTJSON( String json )
    {
        Map<String, String> params = new HashMap<>();
        if( json == null || json.trim().isEmpty() )
        {
            return params;
        }

        String cleanJson = json.trim().replaceAll("[{}\"]", "");
        String[] pairs = cleanJson.split(",");

        for( String pair : pairs )
        {
            String[] keyValue = pair.split(":", 2);
            if( keyValue.length == 2 )
            {
                params.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return params;
    }

    public static String getRequestGET()
    {
        if( FCGIInterface.request != null && FCGIInterface.request.params != null )
        {
            String requestStr = FCGIInterface.request.params.getProperty("QUERY_STRING");
            return requestStr != null ? requestStr : "";
        }
        return "";
    }

    public static Map<String, String> getXYRfromGETStr( String request )
    {
        Map<String, String> parameters = new HashMap<>();
        if( request == null || request.isEmpty() ) return parameters;

        String[] pairs = request.split("&");
        for( String pair : pairs ) 
        {
            int idx = pair.indexOf("=");
            if( idx > 0 )
            {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);

                // Если после = есть значение, декодируем его, иначе сохраняем пустую строку
                String rawValue = (idx < pair.length() - 1) ? pair.substring(idx + 1) : "";
                String value = URLDecoder.decode(rawValue, StandardCharsets.UTF_8);

                parameters.put(key, value);
            }
        }
        return parameters;
    }

    public static void sendResponse( String body, String contType, long exTime )
    {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        String httpResponse = """
HTTP/1.1 200 OK 
Content-Type: %s; charset=utf-8  
Content-Length: %d 
Current-Time: %s 
Execution-Time: %d ns 
 
%s""".formatted(contType, bytes.length, java.time.Instant.now().toString(), exTime, body);

        System.out.print(httpResponse);
        System.out.flush();
    }

    public static void sendError( String message, int statusCode )
    {
        String jsonError = String.format("{\"message\":\"%s\"}", message);
        String httpResponse = """
HTTP/1.1 %d Bad request 
Content-Type: application/json; charset=utf-8 
Content-Length: %d 
 
%s
""".formatted(statusCode, jsonError.getBytes(StandardCharsets.UTF_8).length, jsonError);

        System.out.print(httpResponse);
        System.out.flush();
    }
}
