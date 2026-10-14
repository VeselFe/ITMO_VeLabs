package ru.itmo.lab;


public class Point {
    private final double x;
    private final double y;
    private final double r;
    private boolean success;
    private String creationTime;
    private final String errorMessage;

    public Point( double x, double y, double r )
    {
        this.x = x;
        this.y = y;
        this.r = r;

        if( !Validator.validateX(x) )
            errorMessage = "Validate error: x";
        else if( !Validator.validateY(y) )
            errorMessage = "Validate error: y";
        else if( !Validator.validateR(r) )
            errorMessage = "Validate error: r";
        else
            errorMessage = "0";
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }

    public boolean isSuccess() { return success; }
    public void setSuccess( boolean success ) { this.success = success; }

    public String getCreationTime() { return creationTime; }
    public void setCreationTime( String checkTime ) { this.creationTime = checkTime; }

    public String getErrorMessage() { return errorMessage; }

    protected static class Validator
    {
        protected static boolean validateX( double x )
        {
            double[] mas = {-4, -3, -2, -1, 0, 1, 2, 3, 4};
            for( double n : mas )
                if( n == x )
                    return true;
            return false;
        }
        protected static boolean validateR( double r )
        {
            double[] mas = {1, 1.5, 2, 2.5, 3};
            for( double n : mas )
                if( n == r )
                    return true;
            return false;
        }
        protected static boolean validateY( double y )
        {
            if( y < -3 )
                return false;
            if( y > 5 )
                return false;
            return true;
        }
    }
    public String toJson()
    {
        return String.format(
                "{\"x\": \"%.2f\", \"y\": \"%.2f\", \"r\": \"%.2f\", \"success\": \"%b\", \"time\": \"%s\"}",
                x, y, r, success, creationTime
        );
    }
}