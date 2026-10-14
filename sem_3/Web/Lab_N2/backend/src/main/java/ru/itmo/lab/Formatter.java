package ru.itmo.lab;

import java.util.List;


public class Formatter
{
    public static String createHTMLTable( List<Point> history )
    {
        StringBuilder html = new StringBuilder();
        html.append("<table id=\"results\">");
        String head = """
                    <thead>
                    <tr>
                    <th> X </th>
                    <th> Y </th>
                    <th> R </th>
                    <th> Res </th>
                    <th> Время </th>
                    </tr>
                    </thead>
                    """;
        html.append( head );
        html.append( "<tbody>" );
        for( int ind = history.size() - 1; ind >= 0; ind-- )
        {
            Point point = history.get( ind );
            String row = "<tr>" +
                    "<td>" + point.getX() + "</td> " +
                    "<td>" + point.getY() + "</td> " +
                    "<td>" + point.getR() + "</td> " +
                    "<td>" + (point.isSuccess() ? "точка принадлежит" : "точка не принадлежит") + "</td>" +
                    "<td class=\"time\" data-timestamp=\"" + point.getCreationTime() + "\">" + point.getCreationTime() + "</td>" +
                    "</tr>";
            html.append( row );
        }
        html.append( "</tbody>" );
        html.append( "</table>" );

        return html.toString();
    }
}