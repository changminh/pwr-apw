package apw.kohonenSom;

import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import apw.kohonenSom.patterns.SOMSamplesLoader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 *
 * @author Krzysiek
 */
public class KohonenNetworkTest {
    public static void main(String args[]) throws ParseException, IOException{

        KohonenNetwork net;
        Samples samples =  new ARFFLoader(
                new File("data/iris.arff")).getSamples();

        SOMSamplesLoader patterns =
                KohonenNetwork.initLoader(
                    samples, SOMSamplesLoader.NUM_AVR_MISSING_VAL_ARITHM,
                    SOMSamplesLoader.NOM_MFREQ_MISSING_VAL);

        net = new KohonenNetwork(
                10,
                10,
                10,
                KohonenNetwork.initDistanceEuclidean(),
                KohonenNetwork.initWinnerSelectorConscience(
                    10,
                    10,
                    0.75,
                    500),
                KohonenNetwork.initWeightsInitializerRandom(
                    10,
                    10,
                    4,
                    patterns.getMaxNumNormalizedValue(),
                    patterns.getMinNumNormalizedValue()
                    ),
                KohonenNetwork.initTrainingMethodWTA(
                    KohonenNetwork.initTimeFactorExponential(0.5, 1),
                    KohonenNetwork.initNeighFuncGaussian(
                        KohonenNetwork.initTimeFactorExpotential(1),
                        8),
                    KohonenNetwork.initNeuronsDistNeurNum()),
                patterns,
                true);

        net.generateMap();
        net.trainMap();
    }
}
