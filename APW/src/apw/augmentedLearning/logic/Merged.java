package apw.augmentedLearning.logic;

import apw.core.loader.ARFFLoader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class Merged {
    public static void main(String[] args) throws IOException, ParseException {
        AugmentedLearning inst = new AugmentedLearning(new ARFFLoader(new File("data/iris.arff")).getSamples());
    }
}