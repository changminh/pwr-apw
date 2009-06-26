package apw.augmentedLearning.logic;

import apw.core.loader.ARFFLoader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

// Klasa tymczasowa, 'demonstracyjna', do niczego nie potrzebna;
public class Merged {
    public static void main(String[] args) throws IOException, ParseException {
        AugmentedLearning inst;
        try {
             inst = new AugmentedLearning(new ARFFLoader(new File("data/iris-with-nulls.arff")).getSamples());
        }
        catch(ExceptionInInitializerError ex) {
            System.out.println("Nie udało się utworzyć klasyfikatora!");
            ex.printStackTrace();
        }
        System.out.println("WRESZCIE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}