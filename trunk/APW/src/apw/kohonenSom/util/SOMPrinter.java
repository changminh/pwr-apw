package apw.kohonenSom.util;

/**
 *
 * @author Krzysiek
 */
public class SOMPrinter {

    public static void printTable(double[][] table, int maxX, int maxY){
        int maxL = 0;

       for(int iy=0; iy<maxY; iy++){
            for(int ix=0; ix<maxX; ix++){
                String string = ((Double)table[ix][iy]).toString();
                if(maxL < string.length())
                    maxL = string.length();
            }
        }

        for(int iy=0; iy<maxY; iy++){
            for(int ix=0; ix<maxX; ix++){
                String string = ((Double)table[ix][iy]).toString();
                int amp = maxL - string.length();
                for(int i=0; i<amp; i++)
                    System.out.print(" ");
                System.out.print(string+"; ");
            }
            System.out.println();
        }
    }

}
