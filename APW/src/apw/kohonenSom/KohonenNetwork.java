package apw.kohonenSom;

import apw.core.Samples;
import apw.kohonenSom.distances.*;
import apw.kohonenSom.learningFactors.*;
import apw.kohonenSom.logic.*;
import apw.kohonenSom.logic.topology.*;
import apw.kohonenSom.logic.trainingMethods.*;
import apw.kohonenSom.logic.winnerSelection.*;
import apw.kohonenSom.neighborhoods.*;
import apw.kohonenSom.patterns.*;
import apw.kohonenSom.timeFactors.*;
import apw.kohonenSom.visualization.SOMSimpleVisualizationHex;
import apw.kohonenSom.visualization.SOMSimpleVisualizationMatrix;
import apw.kohonenSom.visualization.SOMVisualization;
import apw.kohonenSom.weightsInitialization.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Christopher Wadowski
 */
public class KohonenNetwork {
	private SOMKohonenMap map;

    //------------------------------------------------------
    private int TMax;
    private int XMax;
    private int YMax;
	private SOMDistanceFunction distanceType;
    private SOMWinnerSelection selector;
	private SOMWeightsInitializer wInit;
    private SOMTrainingMethod trainer;
    private SOMTopology structure;

    private boolean normalizeVectors;

    //------------------------------------------------------
    private SOMSamplesLoader patterns;
    private SOMVisualization visualizator;
	
	//------------------------------------------------------
    public KohonenNetwork(KohonenNetwork som){
        
        this.TMax = som.getTMax();
        this.XMax = som.getXMax();
        this.YMax = som.getYMax();
        this.selector = som.getSelector();
        this.wInit = som.getWeightsInitializer();
        this.trainer = som.getTrainer();
        this.patterns = som.getLoader();
        this.map = som.getMap();
        this.normalizeVectors = som.getNormalize();
        this.distanceType = som.getDistanceType();
        this.visualizator = som.getVisualizator();
        this.structure = som.getStructure();
	}

    public KohonenNetwork(int TMax, int XMax, int YMax,
             SOMDistanceFunction distanceType, SOMWinnerSelection selector,
             SOMWeightsInitializer wInit, SOMTrainingMethod trainer,
             SOMTopology structure, SOMSamplesLoader patterns,
             SOMVisualization visualizator, boolean normalizeVectors){
        
        this.TMax = TMax;
        this.XMax = XMax;
        this.YMax = YMax;
        this.distanceType = distanceType;
        this.selector = selector;
        this.wInit = wInit;
        this.trainer = trainer;
        this.patterns = patterns;
        this.normalizeVectors = normalizeVectors;
        this.visualizator = visualizator;
        this.structure = structure;
    }

    //------------------------------------------------------
    public void generateMap(){
       this.map = new SOMKohonenMap(TMax, XMax, YMax, distanceType,
               selector, wInit, trainer, structure);
    }

    public void trainMap(){
        ArrayList<double[]> pat;

        if(this.normalizeVectors){
            pat = patterns.getNormalizedNumericalData();
        }else{
            pat = patterns.getNumericalData();
        }
        
            this.map.trainIterative(pat);
        }

    public BufferedImage getFeatMap(int feat){
        return visualizator.generateFeatMap(map, feat, patterns);
    }

    public BufferedImage getVoronoiMap(){
        return visualizator.generateVoronoiMap(map, patterns);
    }

    public BufferedImage getPatternsDensityMap(){
        return visualizator.generatePatternDensityMap(map, patterns);
    }

    public BufferedImage getClassMap(){
        return visualizator.generateClassMap(map, patterns);
    }

    public BufferedImage getUMap(){
        return visualizator.generateUMap(map, patterns);
    }

    //------------------------------------------------------
     public static SOMDistanceFunction initDistanceEuclidean(){
         return new SOMEuclideanDistance();
     }

     public static SOMDistanceFunction initDistanceL(){
         return new SOMLDistance();
     }

     public static SOMDistanceFunction initDistanceManhattan(){
         return new SOMManhattanDistance();
     }

     public static SOMDistanceFunction initDistanceScalar(){
         return new SOMScalarDistance();
     }

     public static SOMWinnerSelection initWinnerSelectorCount(
             int x, int y, int maxTime){
         return new SOMCountSelection(x, y, maxTime);
     }

     public static SOMWinnerSelection initWinnerSelectorConscience(int x, int y,
             double pMin, int maxTime){
         return new SOMConscienceSelection(x,y, pMin, maxTime);
     }

     public static SOMWeightsInitializer initWeightsInitializerRandom(
             int x, int y, int inpNum, double WMax, double WMin){
        return new SOMRandomWeightsInitializer(x, y, inpNum, WMax, WMin);
     }

     public static SOMTrainingMethod initTrainingMethodWTM(
             SOMLearningFactor eta, SOMNeighbourhoodFunction neighType,
			 SOMTopology nDist){
        return new SOMWTMMethod(eta, neighType, nDist);
     }

     public static SOMTrainingMethod initTrainingMethodWTA(SOMLearningFactor eta){
        return new SOMWTAMethod(eta);
     }

     public static SOMSamplesLoader initLoader(
             Samples samples, int numMode, int nomMode){
		return new SOMSamplesLoader(samples, numMode, nomMode);
     }

     public static SOMLearningFactor initLearningFactorExponential(
             double etaMax, double C){
        return new SOMExponentialDecrease(etaMax, C);
     }

     public static SOMLearningFactor initLearningFactorHyperbolic(
             double etaMax, double C){
        return new SOMHyperbolicdecrease(etaMax, C);
     }

     public static SOMLearningFactor initLearningFactorLinear(
             double etaMax, double TMax){
        return new SOMLinearDecrease(etaMax, TMax);
     }

     public static SOMNeighbourhoodFunction initNeighFuncGaussian(
             SOMTimeFactor timeType, double maxR){
        return new SOMGaussianNeighbourhood(timeType, maxR);
     }

     public static SOMNeighbourhoodFunction initNeighFuncLinear(
             SOMTimeFactor timeType, double maxR){
        return new SOMLinearNeighbourhood(timeType, maxR);
     }

     public static SOMNeighbourhoodFunction initNeighFuncRectangular(
             double maxR, double decrease){
        return new SOMRectangularNeighbourhood(maxR, decrease);
     }

     public static SOMTopology initHexTopology(){
        return new SOMHexagonalNetwork();
     }

     public static SOMTopology initRectTopology(){
        return new SOMRectangonalNetwork();
     }

     public static SOMTimeFactor initTimeFactorExpotential(double C){
         return new SOMExpotentialTimeFactor(C);
     }

     public static SOMTimeFactor initTimeFactorHyperbolic(double C){
         return new SOMHyperbolicTimeFactor(C);
     }

     public static SOMTimeFactor initTimeFactorLinear(int tMax){
         return new SOMLinearTimeFactor(tMax);
     }

     public static SOMVisualization initHexVisualization(){
         return new SOMSimpleVisualizationHex();
     }

     public static SOMVisualization initMatrixVisualization(){
         return new SOMSimpleVisualizationMatrix();
     }

    //------------------------------------------------------
    public void setTMax(int TMax){
        this.TMax = TMax;
    }

    public void setXMax(int XMax){
        this.XMax = XMax;
    }

    public void setYMax(int YMax){
        this.YMax = YMax;
    }

    public void setTrainer(SOMTrainingMethod trainer){
        this.trainer = trainer;
    }

    public void setSelector(SOMWinnerSelection selector){
        this.selector = selector;
    }

    public void setWeightsInitializer(SOMWeightsInitializer wInit){
        this.wInit = wInit;
    }

    public void setDistanceType(SOMDistanceFunction distanceType){
        this.distanceType = distanceType;
    }

    public void setLoader(SOMSamplesLoader patterns){
        this.patterns = patterns;
    }

    public void setNormalize(boolean normalizeVectors){
        this.normalizeVectors = normalizeVectors;
    }

    public void setVisualizator(SOMVisualization visualizator){
        this.visualizator = visualizator;
    }

    public void setStructure(SOMTopology structure) {
        this.structure = structure;
    }

    //------------------------------------------------------
    public int getTMax(){
        return this.TMax;
    }

    public int getXMax(){
        return this.XMax;
    }

    public int getYMax(){
        return this.YMax;
    }

    public SOMTrainingMethod getTrainer(){
        return this.trainer;
    }

    public SOMWinnerSelection getSelector(){
        return this.selector;
    }

    public SOMWeightsInitializer getWeightsInitializer(){
        return this.wInit;
    }

    public SOMDistanceFunction getDistanceType(){
        return this.distanceType;
    }

    public SOMSamplesLoader getLoader(){
        return this.patterns;
    }

    public SOMKohonenMap getMap(){
        return this.map;
    }

    public boolean getNormalize(){
        return this.normalizeVectors;
    }

    public SOMVisualization getVisualizator(){
        return this.visualizator;
    }

    private SOMTopology getStructure() {
        return this.structure;
    }
}
