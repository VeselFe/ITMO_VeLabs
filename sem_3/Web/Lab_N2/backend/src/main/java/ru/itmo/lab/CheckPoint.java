package ru.itmo.lab;

public class CheckPoint
{
    private double x;
    private double y;
    private double r;
    private String creationTime = null;

    public CheckPoint( Point point )
    {
        x = point.getX();
        y = point.getY();
        r = point.getR();
    }

    public boolean checkPoint()
    {
        creationTime = java.time.Instant.now().toString();
        if( x >= 0 && y <= 0 && (x * x + y * y <= r * r / 4) )
            return true;
        if( x >= 0 && x <= r && y >= 0 && y <= r / 2 )
            return true;
        if( x <= 0 && y >= 0 && (y - x <= r / 2) )
            return true;
        return false;
    }

    public String getCreationTime() { return creationTime; }
}