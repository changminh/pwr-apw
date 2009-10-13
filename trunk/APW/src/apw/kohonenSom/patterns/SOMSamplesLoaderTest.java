package apw.kohonenSom.patterns;

import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author Christopher Wadowski
 */
public class SOMSamplesLoaderTest{
    public static void main(String args[]) throws IOException, ParseException{
        SOMSamplesLoader loader;
        Samples samples =  new ARFFLoader(
                new File("data/iris.arff")).getSamples();

        loader = new SOMSamplesLoader(samples,
                SOMSamplesLoader.NUM_AVR_MISSING_VAL_ARITHM,
                SOMSamplesLoader.NOM_MFREQ_MISSING_VAL);

        ArrayList<double[]> data = loader.getNormalizedNumericalData();

        System.out.println("samples number: "+data.size());
        System.out.println("numerical attributes: "+data.get(0).length);
        System.out.println("sample class type: "+loader.getClassNames().get(0));
        System.out.println("samples with missing values: "
                +loader.getSampsWithMisValNumber());
    }
}
