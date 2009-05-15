/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.test;

import java.io.File;
import jpl.Atom;
import jpl.Query;
import jpl.Term;

/**
 *
 * @author Nitric
 */
public class Util {
    static {
        System.out.print("Użycie biblioteki JPL w wersji: ");
        jpl.JPL.main(null);
    }
    
    public static String sciezkaDoPliku(File f) {
        return f.toString().replace("\\", "\\\\");
    }

    public static void wczytajPlik(File f) {
        System.out.println("Wczytywanie pliku: " + f.getName());
        new Query("consult", new Term[] {new Atom(sciezkaDoPliku(f))}).hasSolution();
        
    }
}
