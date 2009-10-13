package apw.kohonenSom;

import apw.core.Samples;
import apw.kohonenSom.distances.*;
import apw.kohonenSom.learningFactors.*;
import apw.kohonenSom.logic.*;
import apw.kohonenSom.logic.nuronDistances.*;
import apw.kohonenSom.logic.trainingMethods.*;
import apw.kohonenSom.logic.winnerSelection.*;
import apw.kohonenSom.neighborhoods.*;
import apw.kohonenSom.patterns.*;
import apw.kohonenSom.timeFactors.*;
import apw.kohonenSom.weightsInitialization.*;
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

    private boolean normalizeVectors;

    //------------------------------------------------------
    private SOMSamplesLoader patterns;
	
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
	}

    public KohonenNetwork(int TMax, int XMax, int YMax,
             SOMDistanceFunction distanceType, SOMWinnerSelection selector,
             SOMWeightsInitializer wInit, SOMTrainingMethod trainer,
             SOMSamplesLoader patterns, boolean normalizeVectors){
        
        this.TMax = TMax;
        this.XMax = XMax;
        this.YMax = YMax;
        this.distanceType = distanceType;
        this.selector = selector;
        this.wInit = wInit;
        this.trainer = trainer;
        this.patterns = patterns;
        this.normalizeVectors = normalizeVectors;
    }

    //------------------------------------------------------
    public void generateMap(){
       this.map = new SOMKohonenMap(TMax, XMax, YMax, distanceType,
               selector, wInit, trainer);
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

    //TODO return maps for visualizasion

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
			 SOMNeuronsDistance nDist){
        return new SOMWTMMethod(eta, neighType, nDist);
     }

     public static SOMTrainingMethod initTrainingMethodWTA(SOMLearningFactor eta){
        return new SOMWTAMethod(eta);
     }

     public static SOMSamplesLoader initLoader(
             Samples samples, int numMode, int nomMode){
		return new SOMSamplesLoader(samples, numMode, nomMode);
     }

     public static SOMLearningFactor initTimeFactorExponential(
             double etaMax, double C){
        return new SOMExponentialDecrease(etaMax, C);
     }

     public static SOMLearningFactor initTimeFactorHyperbolic(
             double etaMax, double C){
        return new SOMHyperbolicdecrease(etaMax, C);
     }

     public static SOMLearningFactor initTimeFactorLinear(
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

     public static SOMNeuronsDistance initNeuronsDistNeurNum(){
        return new SOMNeuronNumber();
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
}
