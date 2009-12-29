package apw.augmentedLearning.logic;

import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

// Klasa tymczasowa, 'demonstracyjna', do niczego nie potrzebna;
public class Merged {
    public static void main(String[] args) throws IOException, ParseException {
        AugmentedLearning inst;
        try {
            Samples samples = new ARFFLoader(new File("data/wine.arff")).getSamples();
            // samples.setClassAttributeIndex(0);
            inst = new AugmentedLearning(samples);
        }
        catch(ExceptionInInitializerError ex) {
            System.out.println("Nie udało się utworzyć klasyfikatora!");
            ex.printStackTrace();
        }
        System.out.println("WRESZCIE!!!!!!!!!!!!!!!!!!!!!");
    }
}