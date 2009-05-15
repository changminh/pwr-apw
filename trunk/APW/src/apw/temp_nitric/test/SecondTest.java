/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.temp_nitric.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import jpl.Compound;
import jpl.Query;

/**
 *
 * @author Nitric
 */
public class SecondTest {

    private static String[] nazwyHipotez = {
        "hipoteza1",
        "hipoteza2",
        "hipoteza3",
        "hipoteza4",
        "hipoteza5"
    };

    private static int[] statystyki = new int[nazwyHipotez.length];

    public static void main(String[] args) throws Exception {
        Util.wczytajPlik((new File(SecondTest.class.getResource("liczby.pl").toURI())));

        // Wczytanie pliku z liczbami:
        File dane = new File(SecondTest.class.getResource("dane.txt").toURI());
        BufferedReader czytnik = new BufferedReader(new FileReader(dane));
        String linijka;
        String[] tokeny;
        Query q = null;
        int licznik = 0;
        jpl.Term[] liczby = new jpl.Integer[4];
        while ((linijka = czytnik.readLine()) != null && linijka.length() > 1) {
            licznik++;
            System.out.println("Przetwarzam: " + linijka);
            tokeny = linijka.split(" ");
            for (int i = 0; i < 4; i++) {
                liczby[i] = new jpl.Integer(Integer.parseInt(tokeny[i]));
            }
            for (int i = 0; i < nazwyHipotez.length; i++) {
                q = new Query(new Compound(nazwyHipotez[i], liczby));
                System.out.println(nazwyHipotez[i] + " -> " + q.hasSolution());
                if (q.hasSolution() && tokeny[4].equals("t"))
                    statystyki[i]++;
                else if (!q.hasSolution() && tokeny[4].equals("f"))
                    statystyki[i]++;
            }
            System.out.println("");
        }
        System.out.println("-----------------------------------------");
        System.out.println("Wszystkich krotek: " + licznik);
        System.out.println("Liczba trafień dla hipotez: ");
        for (int i = 0; i < nazwyHipotez.length ; i++)
            System.out.println(nazwyHipotez[i] + " -> " + statystyki[i]);
    }
}
