package apw.kohonenSom;

import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import apw.kohonenSom.distances.SOMDistanceFunction;
import apw.kohonenSom.learningFactors.SOMLearningFactor;
import apw.kohonenSom.logic.nuronDistances.SOMNeuronsDistance;
import apw.kohonenSom.logic.trainingMethods.SOMTrainingMethod;
import apw.kohonenSom.logic.winnerSelection.SOMWinnerSelection;
import apw.kohonenSom.neighborhoods.SOMNeighbourhoodFunction;
import apw.kohonenSom.patterns.SOMSamplesLoader;
import apw.kohonenSom.timeFactors.SOMTimeFactor;
import apw.kohonenSom.weightsInitialization.SOMWeightsInitializer;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author Christopher Wadowski
 */
public class KohonenNetworkTest {
    public static void main(String args[]) throws ParseException, IOException{

        KohonenNetwork net;
        Samples samples =  new ARFFLoader(
                new File("data/iris.arff")).getSamples();

        SOMLearningFactor eta =
                KohonenNetwork.initTimeFactorExponential(0.1, 1);

        int maxR = 8;

        SOMTimeFactor timeType =
                KohonenNetwork.initTimeFactorExpotential(0.1);

        SOMNeighbourhoodFunction neighType =
                KohonenNetwork.initNeighFuncGaussian(
                timeType, maxR);

        SOMNeuronsDistance nDist =
                KohonenNetwork.initNeuronsDistNeurNum();

        int TMax = 10;
        int XMax = 10;
        int YMax = 10;

        int inputs = 4;
        
        double minW = 0;
        double maxW = 1;

        int numMisVal = SOMSamplesLoader.NUM_AVR_MISSING_VAL_ARITHM;
        int nomMisVal = SOMSamplesLoader.NOM_MFREQ_MISSING_VAL;

        double consciencePotential = 0.75;

        int maxTimeSelMet = 500;

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
                    eta, neighType, nDist);

        SOMTrainingMethod trainerWTA =
                KohonenNetwork.initTrainingMethodWTA(eta);

        SOMSamplesLoader patterns =
                KohonenNetwork.initLoader(
                    samples ,numMisVal, nomMisVal);

        net = new KohonenNetwork(
                TMax, XMax, YMax, distanceType, selector,
                wInit, trainerWTA, patterns, true);

        System.out.println("start;\n");

        for(int i=0; i<100; i++)
        {
            System.out.println("test: "+(i+1));
            net.generateMap();
            net.trainMap();
        }

        /*
        ArrayList<Point> list = net.getMap().getCenters();

        System.out.println("centra wzorcow:");
        for(int i=0; i<list.size(); i++){
            System.out.println(list.get(i).x +"; "+list.get(i).y);
        }*/
    }
}
