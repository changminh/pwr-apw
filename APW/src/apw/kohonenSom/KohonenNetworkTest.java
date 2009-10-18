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
import apw.kohonenSom.visualization.SOMVisualization;
import apw.kohonenSom.weightsInitialization.SOMWeightsInitializer;
import java.awt.Point;
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

        KohonenNetwork net;
        Samples samples =  new ARFFLoader(
                new File("data/iris.arff")).getSamples();

        int numMisVal = SOMSamplesLoader.NUM_AVR_MISSING_VAL_ARITHM;
        int nomMisVal = SOMSamplesLoader.NOM_MFREQ_MISSING_VAL;

        SOMSamplesLoader patterns =
                KohonenNetwork.initLoader(
                    samples ,numMisVal, nomMisVal);

        SOMLearningFactor eta =
                KohonenNetwork.initTimeFactorExponential(0.1, 1);

        int maxR = 10;

        SOMTimeFactor timeType =
                KohonenNetwork.initTimeFactorExpotential(0.1);

        SOMNeighbourhoodFunction neighTypeGaus =
                KohonenNetwork.initNeighFuncGaussian(timeType, maxR);

        SOMNeighbourhoodFunction neighTypeRec =
                KohonenNetwork.initNeighFuncRectangular(9, 0.1);

        SOMNeighbourhoodFunction neighTypeLin =
                KohonenNetwork.initNeighFuncLinear(timeType, maxR);

        SOMNeuronsDistance nDistHex =
                KohonenNetwork.initHexNeuronsDistNeurNum();

        System.out.println(
                nDistHex.calcDistance(new Point(5,5), new Point(0,1)));

        int TMax = 5;
        int XMax = 10;
        int YMax = 10;

        int inputs = patterns.getNumAttrNumber();
        
        double minW = 0.0;
        double maxW = 10.0;

        double consciencePotential = 0.75;

        int maxTimeSelMet = samples.size() * TMax/5;

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
                    eta, neighTypeGaus, nDistHex);

        SOMTrainingMethod trainerWTA =
                KohonenNetwork.initTrainingMethodWTA(eta);

        SOMVisualization hexVisualizator=
                KohonenNetwork.initHexVisualization();

        net = new KohonenNetwork(
                TMax, XMax, YMax, distanceType, selector, wInit, trainerWTA,
                nDistHex, patterns, hexVisualizator, true);

        System.out.println("start;\n");

        for(int i=0; i<1; i++)
        {
            System.out.println("test: "+(i+1));
            net.generateMap();
            net.trainMap();
        }

        System.out.println("stop;\n");

        /*
        ArrayList<Point> list = net.getMap().getCenters();

        System.out.println("centra wzorcow:");
        for(int i=0; i<list.size(); i++){
            System.out.println(list.get(i).x +"; "+list.get(i).y);
        }*/

        /*
        double[][] mapa = net.getMap().generateUMap();
        for(int iy=0; iy<mapa[0].length; iy++){
            for(int ix=0; ix<mapa.length; ix++){
            System.out.print(mapa[ix][iy]+"; ");
            }
            System.out.println();
        }*/

        BufferedImage umap = net.getUMap();
        BufferedImage fmap0 = net.getFeatMap(0);
        BufferedImage fmap1 = net.getFeatMap(1);
        BufferedImage fmap2 = net.getFeatMap(2);
        BufferedImage fmap3 = net.getFeatMap(3);
        BufferedImage pdmap = net.getPatternsDensityMap();

        ImageIO.write(umap, "PNG", new File ("Visualizations/testUMap.png"));
        ImageIO.write(fmap0, "PNG", new File ("Visualizations/testF0Map.png"));
        ImageIO.write(fmap1, "PNG", new File ("Visualizations/testF1Map.png"));
        ImageIO.write(fmap2, "PNG", new File ("Visualizations/testF2Map.png"));
        ImageIO.write(fmap3, "PNG", new File ("Visualizations/testF3Map.png"));
        ImageIO.write(pdmap, "PNG", new File ("Visualizations/testPDMap.png"));
    }
}
