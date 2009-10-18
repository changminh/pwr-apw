package apw.kohonenSom;

import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import apw.kohonenSom.distances.SOMDistanceFunction;
import apw.kohonenSom.learningFactors.SOMLearningFactor;
import apw.kohonenSom.logic.topology.SOMTopology;
import apw.kohonenSom.logic.trainingMethods.SOMTrainingMethod;
import apw.kohonenSom.logic.winnerSelection.SOMWinnerSelection;
import apw.kohonenSom.neighborhoods.SOMNeighbourhoodFunction;
import apw.kohonenSom.patterns.SOMSamplesLoader;
import apw.kohonenSom.timeFactors.SOMTimeFactor;
import apw.kohonenSom.visualization.SOMVisualization;
import apw.kohonenSom.weightsInitialization.SOMWeightsInitializer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import javax.imageio.ImageIO;

/**
 *
 * @author Christopher Wadowski
 */
public class KohonenNetworkTest {
    public static void main(String args[]) throws ParseException, IOException{

        String segments = "data/segment-challenge.arff";
        String iris = "data/iris.arff";
        String wine = "data/wine.arff";

        int TMax = 10;
        int XMax = 10;
        int YMax = 10;

        int maxR = (int)(0.9*XMax + 0.9*YMax)/2;

        double minW = 0.0;
        double maxW = 1.0;

        double etaMax = 0.1;

        double consciencePotential = 0.75;

        int maxTimeSelMet = TMax/2;

        KohonenNetwork net;
        Samples samples =  new ARFFLoader(
                new File(iris)).getSamples();

        int numMisVal = SOMSamplesLoader.NUM_AVR_MISSING_VAL_ARITHM;
        int nomMisVal = SOMSamplesLoader.NOM_MFREQ_MISSING_VAL;

        SOMSamplesLoader patterns =
                KohonenNetwork.initLoader(
                    samples ,numMisVal, nomMisVal);

        SOMLearningFactor etaExp =
                KohonenNetwork.initLearningFactorExponential(etaMax, 1);
        SOMLearningFactor etaLin =
                KohonenNetwork.initLearningFactorLinear(etaMax, TMax);
        SOMLearningFactor etaHyperb =
                KohonenNetwork.initLearningFactorHyperbolic(etaMax, 1);

        SOMTimeFactor timeTypeLin =
                KohonenNetwork.initTimeFactorExpotential(TMax);

        SOMTimeFactor timeTypeHyper =
                KohonenNetwork.initTimeFactorHyperbolic(0.5);

        SOMTimeFactor timeTypeExpo =
                KohonenNetwork.initTimeFactorExpotential(0.5);

        SOMNeighbourhoodFunction neighTypeGaus =
                KohonenNetwork.initNeighFuncGaussian(timeTypeExpo, maxR);

        SOMNeighbourhoodFunction neighTypeRec =
                KohonenNetwork.initNeighFuncRectangular(maxR, 0.2);

        SOMNeighbourhoodFunction neighTypeLin =
                KohonenNetwork.initNeighFuncLinear(timeTypeLin, maxR);

        SOMTopology topology =
                KohonenNetwork.initHexTopology();

        int inputs = patterns.getNumAttrNumber();    

        SOMDistanceFunction distanceType =
                KohonenNetwork.initDistanceEuclidean();

        SOMWinnerSelection selector =
                KohonenNetwork.initWinnerSelectorConscience(
                XMax,YMax,consciencePotential,maxTimeSelMet);

        SOMWeightsInitializer wInit =
                KohonenNetwork.initWeightsInitializerRandom(
                    XMax, YMax, inputs, minW, maxW);

        SOMTrainingMethod trainerWTM =
                KohonenNetwork.initTrainingMethodWTM(
                    etaLin, neighTypeGaus, topology);

        SOMTrainingMethod trainerWTA =
                KohonenNetwork.initTrainingMethodWTA(etaExp);

        SOMVisualization hexVisualizator=
                KohonenNetwork.initHexVisualization();

        net = new KohonenNetwork(
                TMax, XMax, YMax, distanceType, selector, wInit, trainerWTM,
                topology, patterns, hexVisualizator, true);

        System.out.println("start;\n");

        for(int i=0; i<1; i++)
        {
            System.out.println("test: "+(i+1));
            net.generateMap();
            net.trainMap();
        }

        System.out.println("stop;\n");

        BufferedImage umap = net.getUMap();
        BufferedImage pdmap = net.getPatternsDensityMap();

        ImageIO.write(umap, "PNG", new File ("Visualizations/testUMap.png"));  
        ImageIO.write(pdmap, "PNG", new File ("Visualizations/testPDMap.png"));
    }
}
