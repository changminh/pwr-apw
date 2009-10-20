package apw.kohonenSom;

import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import apw.kohonenSom.distances.SOMDistanceFunction;
import apw.kohonenSom.learningFactors.SOMLearningFactor;
import apw.kohonenSom.topology.SOMTopology;
import apw.kohonenSom.trainingMethods.SOMTrainingMethod;
import apw.kohonenSom.winnerSelection.SOMWinnerSelection;
import apw.kohonenSom.neighborhoods.SOMNeighbourhoodFunction;
import apw.kohonenSom.patterns.SOMSamplesLoader;
import apw.kohonenSom.timeFactors.SOMTimeFactor;
import apw.kohonenSom.visualization.SOMVisualization;
import apw.kohonenSom.weightsInitialization.SOMWeightsInitializer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Christopher Wadowski
 */
public class KohonenNetworkTest {
    public static void main(String args[]) throws ParseException, IOException{

        String segments = "data/segment-challenge.arff";
        String iris = "data/iris.arff";
        String iris2 = "data/iris-mini.arff";
        String wine = "data/wine.arff";
        String animals = "data/animals.arff";

        int TMax = 10;
        int XMax = 50;
        int YMax = 50;

        int maxR = 40;
        int maxTimeSelMet = TMax/10;

        double etaMax = 0.4;

        double consciencePotential = 0.75;

        boolean normalize = true;

        KohonenNetwork net;
        Samples samples =
                new ARFFLoader(new File(animals)).getSamples();

        int numMisVal = SOMSamplesLoader.NUM_AVR_MISSING_VAL_ARITHM;
        int nomMisVal = SOMSamplesLoader.NOM_MFREQ_MISSING_VAL;

        SOMSamplesLoader patterns =
                KohonenNetwork.initLoader(
                    samples ,numMisVal, nomMisVal, normalize);

        SOMLearningFactor etaExp =
                KohonenNetwork.initLearningFactorExponential(etaMax, 0.1);

        SOMLearningFactor etaLin =
                KohonenNetwork.initLearningFactorLinear(etaMax, TMax);

        SOMLearningFactor etaHyperb =
                KohonenNetwork.initLearningFactorHyperbolic(etaMax, 0.1);

        SOMTimeFactor timeTypeLin =
                KohonenNetwork.initTimeFactorExpotential(TMax);

        SOMTimeFactor timeTypeHyper =
                KohonenNetwork.initTimeFactorHyperbolic(0.1);

        SOMTimeFactor timeTypeExpo =
                KohonenNetwork.initTimeFactorExpotential(0.1);

        SOMNeighbourhoodFunction neighTypeGaus =
                KohonenNetwork.initNeighFuncGaussian(timeTypeLin, maxR);

        SOMNeighbourhoodFunction neighTypeRect =
                KohonenNetwork.initNeighFuncRectangular(maxR, 0.2);

        SOMNeighbourhoodFunction neighTypeLin =
                KohonenNetwork.initNeighFuncLinear(timeTypeLin, maxR);

        SOMTopology hexTopology =
                KohonenNetwork.initHexTopology();

        SOMDistanceFunction distanceEucl =
                KohonenNetwork.initDistanceEuclidean();

        SOMDistanceFunction distanceScal =
                KohonenNetwork.initDistanceScalar();

        SOMDistanceFunction distanceL =
                KohonenNetwork.initDistanceL();

        SOMDistanceFunction distanceManh =
                KohonenNetwork.initDistanceManhattan();

        SOMWinnerSelection selectorConsc =
                KohonenNetwork.initWinnerSelectorConscience(
                XMax,YMax,consciencePotential,maxTimeSelMet);

        SOMWinnerSelection selectorCount =
                KohonenNetwork.initWinnerSelectorCount(
                XMax, YMax, maxTimeSelMet);

        SOMWinnerSelection selector =
                KohonenNetwork.initWinnerSelector();

        SOMWeightsInitializer wInit =
                KohonenNetwork.initWeightsInitializerRandom();

        SOMTrainingMethod trainerWTM =
                KohonenNetwork.initTrainingMethodWTM(
                    etaLin, neighTypeGaus, hexTopology);

        SOMTrainingMethod trainerWTA =
                KohonenNetwork.initTrainingMethodWTA(etaLin);

        SOMVisualization hexVisualizator=
                KohonenNetwork.initHexVisualization();

        net = new KohonenNetwork(
                TMax,
                XMax,
                YMax,
                distanceEucl,
                selector,
                wInit,
                trainerWTM,
                hexTopology,
                patterns,
                hexVisualizator
                );

        System.out.println("start;");

        for(int i=0; i<1; i++)
        {
            System.out.println("test: "+(i+1));
            net.generateMap();
            net.trainMap();
        }

        System.out.println("stop;");

        BufferedImage umap = net.getUMap();
        BufferedImage pdmap = net.getPatternsDensityMap();
        BufferedImage clustmap = net.getClusterMap();

        ImageIO.write(umap, "PNG", new File ("Visualizations/testUMap.png"));  
        ImageIO.write(pdmap, "PNG", new File ("Visualizations/testPDMap.png"));
        ImageIO.write(clustmap, "PNG", new File ("Visualizations/testClustMap.png"));
    }
}
