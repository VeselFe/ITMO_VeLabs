public class Lab1
{
    public static void main(String []args)
    {
        float[] x = new float[12];
        short[] w = new short[13];

        for (int i = 0, j = 25; i < 13; i++, j -= 2)
        {
            w[i] = (short)j;
        }
        for (int i = 0, j = -17; i < 12; i++, j += 2)
        { 
            x[i] = (float)(-11.0 + Math.random() * 14.0);
        }

        double[][] k = new double[13][12];
        for (int i = 0; i < 13; i++)
            for (int j = 0; j < 12; j++)
                k[i][j] = PutMyElement(x[j], w[i]);

        PrintRes(k, 13, 12);
        System.out.println("end");
    }
    public static int IsIn( int op )
    {
        int[] Array_2 = {1, 5, 7, 9, 13, 25};
        for (int i = 0; i < 6; i++)
        {
            if (Array_2[i] == op)
                return 1;
        }
        return 0;
    }
    public static double PutMyElement( double el, int op )
    {
        if ( op == 19 )
        {
            return Math.pow(Math.pow(Math.pow(el, 2), Math.pow(Math.E, el) + 1), 1/3);
        }
        else if (IsIn(op) == 1)
        {
            return Math.tan(Math.asin(((el - 4) / 14)));
        }
        else
        {
            return Math.pow(Math.tan(Math.log(Math.sqrt(Math.abs(el)))), (Math.tan(Math.pow(Math.E, Math.log(Math.abs(el)))))/12);
        }
    }

    public static void PrintRes( double[][] mas, int W, int H )
    {
        short flag;
        int maxim = 0, th;
        for (int i = 0; i < W; i++ )
            for (int j = 0; j < H; j++ )
            {
                if(mas[i][j] == 0)
                    th = 1;
                else
                {
                    th = (int) (Math.log10(Math.abs(mas[i][j])) + 1);
                    if(th > maxim)
                        maxim = th;
                }
            }
        double el = 0;
        for (int i = 0; i < W; i++ )
        {
            flag = 0;
            System.out.print("| | ");
            for (int j = 0; j < H; j++ )
            {
                el = mas[i][j];
                if(Double.isNaN(el))
                { 
                    System.out.print(" ".repeat(maxim));
                    System.out.printf("%.5f", el);
                    System.out.print(" ".repeat(4));
                }
                else
                {
                    if(el >= 0)
                        System.out.print(" ");  
                    if(el == 0)
                        th = 1;
                    else
                    {
                        th = (int) (Math.log10(Math.abs(el)) + 1);
                        if(th == 0)
                            th += 1;
                    }
                    System.out.print(" ".repeat(maxim - th));
                    System.out.printf("%.5f", el);
                }

                System.out.print(" "); // можно добавить "|" для столбцов
            }
            System.out.print("|");
            System.out.println();
        }   
        System.out.println();
        System.out.println(maxim);
    }    
}