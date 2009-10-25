package apw.kohonenSom.logic;

import apw.kohonenSom.distances.SOMDistanceFunction;
import apw.kohonenSom.topology.SOMTopology;
import apw.kohonenSom.trainingMethods.SOMTrainingMethod;
import apw.kohonenSom.winnerSelection.SOMWinnerSelection;
import apw.kohonenSom.patterns.SOMSamplesLoader;
import apw.kohonenSom.util.SOMOrderRandomizer;
import apw.kohonenSom.weightsInitialization.SOMWeightsInitializer;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Christopher Wadowski
 */
public class SOMKohonenMap{

	//------------------------------------------------------
	private int tMax;
	
	private int xMax;
	private int yMax;
	
	private SOMDistanceFunction distType;
	private SOMWeightsInitializer wInit;
	private SOMTrainingMethod trainer;
	private SOMWinnerSelection selector;
    private SOMTopology structure;
	
	//------------------------------------------------------
	private double[][][] weights;
	
	//------------------------------------------------------
	private SOMOrderRandomizer orderRand;

    //------------------------------------------------------
	//constructors
	public SOMKohonenMap(int TMax, int XMax, int YMax,
			SOMDistanceFunction distanceType, SOMWinnerSelection selector,
			SOMWeightsInitializer wInit, SOMTrainingMethod trainer,
            SOMTopology structure)
	{	
		this.tMax = TMax;
		
		this.xMax = XMax;
		this.yMax = YMax;
	
		this.distType = distanceType;
		this.wInit = wInit;
		this.trainer = trainer;
		this.selector = selector;
        this.structure = structure;

		this.orderRand = new SOMOrderRandomizer();
	}

	//------------------------------------------------------
    //public methods
	public Point sendSignal(double[] vector)
	{
		Point winner = new Point(-1,-1);

        if(weights != null){
            double[][] distances = calcDist(vector);
            winner = selector.winnerSelection(distances);
        }
		
		return winner;
	}
	
	//---------------------------------
    //training
	public void trainIterative(SOMSamplesLoader samples)
	{
        weights = wInit.initializeWeights(samples, xMax, yMax);

        ArrayList<double[]> patterns = samples.getNumericalData();

        ArrayList<Integer> patternOrder;
		double[][] distances;	
		Point winner;
        
		for(int time=0; time<tMax; time++){
            patternOrder =  orderRand.randomizeOrder(patterns.size());
            
            for(int p=0; p<patterns.size(); p++)
            {
				double[] vector = patterns.get(patternOrder.get(p));
				distances = calcDist(vector);
				winner = selector.winnerSelection(distances, time);
				weights = trainer.adaptWeights(vector, weights, winner, time);
			}
            /*
            if(checkWeights(weights)){
                System.out.println(
                        "error weights, e: "+T+"; t: "+time+"; ");
            }*/
		}
	}
	
	//------------------------------------------------------
	//visualization data generation
    public ArrayList<Point> generateClusterCenters(
            SOMSamplesLoader samples){
        ArrayList<double[]> patterns = samples.getNumericalData();
        ArrayList<Point> centers = new ArrayList<Point>();

        double[] vector;
		Point winner;

		for(int p=0; p<patterns.size(); p++){
			vector = patterns.get(p);
			winner = sendSignal(vector);

			if(!isCenter(winner, centers))
				centers.add(winner);
		}

        return centers;
    }

    //---------------------------------
    public ArrayList<Integer>[][] generatePatternClustersMap(
            SOMSamplesLoader samples)
	{
        ArrayList<Point> centers =
                this.generateClusterCenters(samples);

        ArrayList<Integer>[][] map =
                this.generatePatternsPositionsMap(samples);
		
		map = fillEmptyNeurons(map, centers);

        return map;
	}

    //---------------------------------
    public int[][] generateClustersMap(
            SOMSamplesLoader samples)
	{
        ArrayList<Point> centers =
                this.generateClusterCenters(samples);

        int[][] map = new int[xMax][];
        for(int ix=0; ix<xMax; ix++){
            map[ix] = new int[yMax];
            for(int iy=0; iy<yMax; iy++){
                map[ix][iy] = -1;
            }
        }

        for(int ic=0; ic<centers.size(); ic++){
            Point c = centers.get(ic);
            map[c.x][c.y] = ic;
        }

		map = fillEmptyNeurons(map, centers);

        return map;
	}

    //---------------------------------
    public int[][] generatePatternsDensityMap(
            ArrayList<double[]> patterns){
        int[][] map = new int[xMax][];
        for(int ix=0; ix<xMax; ix++){
            map[ix] = new int[yMax];
            for(int iy=0; iy<yMax; iy++){
                map[ix][iy] = 0;
            }
        }

        double[] vector;
		Point winner;
		for(int p=0; p<patterns.size(); p++){
			vector = patterns.get(p);
			winner = sendSignal(vector);

			map[winner.x][winner.y]++;
		}

        return map;
    }

    //---------------------------------
    public ArrayList<Integer>[][] generatePatternsPositionsMap(
            SOMSamplesLoader samples){
        ArrayList<double[]> patterns = samples.getNumericalData();

        ArrayList<Integer>[][] map = new ArrayList[xMax][];
        for(int ix=0; ix<xMax; ix++){
            map[ix] = new ArrayList[yMax];
            for(int iy=0; iy<yMax; iy++){
                map[ix][iy] = new ArrayList<Integer>();
            }
        }

        double[] vector;
		Point winner;
		for(int p=0; p<patterns.size(); p++){
			vector = patterns.get(p);
			winner = sendSignal(vector);
			map[winner.x][winner.y].add(p);
		}

        return map;
    }

    //---------------------------------
    public double[][] generateUMap(){
        double uMap[][];

        int ux = this.xMax*2 - 1;
        int uy = this.yMax*2 - 1;

        uMap = new double[ux][];

        for(int iux=0; iux<ux; iux++){
            uMap[iux] = new double[uy];
        }

        for(int ix=0, iux=0; ix< this.xMax; ix++, iux+=2){
            for(int iy=0, iuy=0; iy< this.yMax; iy++, iuy+=2){
                double xy = 0;
                int n = 0;
                Point neuron = new Point(ix, iy);
                ArrayList<Point> neighbours =
                        structure.getNeighbours(neuron, xMax, yMax);

                for(int in=0; in<neighbours.size(); in++){
                    Point nNeuron = neighbours.get(in);
                    int dx = nNeuron.x - neuron.x;
                    int dy = nNeuron.y - neuron.y;
                    
                    double[] v1 = weights[neuron.x][neuron.y];
                    double[] v2 = weights[nNeuron.x][nNeuron.y];

                    double dist = distType.getDistance(v1, v2);

                    uMap[iux+dx][iuy+dy] = dist;

                    xy += dist; n++;
                }

                uMap[iux][iuy] = xy/n;
            }
        }

        return uMap;
    }

    //---------------------------------
    public void generateVoronoiData(){
        //TODO
    }
	
	//------------------------------------------------------
    //private methods
	private  ArrayList<Integer>[][] fillEmptyNeurons(
            ArrayList<Integer>[][] map, ArrayList<Point> centers)
	{
        ArrayList<Integer>[][] resultMap = new ArrayList[xMax][];
        for(int ix=0; ix<xMax; ix++){
            resultMap[ix] = new ArrayList[yMax];
            for(int iy=0; iy<yMax; iy++){
                resultMap[ix][iy] = new ArrayList<Integer>();
            }
        }

		for(int ix=0; ix<xMax; ix++){
            for(int iy=0; iy<yMax; iy++){
                if(map[ix][iy].size() == 0){
                    int c = findCenterNearestToNeuron(ix, iy, centers);
                    int cx = centers.get(c).x;
                    int cy = centers.get(c).y;
                    resultMap[ix][iy] = map[cx][cy];
                }
                else
                    resultMap[ix][iy] = map[ix][iy];
            }
        }
        
		return resultMap;
	}

    //---------------------------------
	private int[][] fillEmptyNeurons(
            int[][] map, ArrayList<Point> centers)
	{
        int[][] resultMap = new int[xMax][];
        for(int ix=0; ix<xMax; ix++){
            resultMap[ix] = new int[yMax];
        }

		for(int ix=0; ix<xMax; ix++){
            for(int iy=0; iy<yMax; iy++){
                if(map[ix][iy] == -1){
                    resultMap[ix][iy] = 
                            findCenterNearestToNeuron(ix, iy, centers);
                }
                else
                    resultMap[ix][iy] = map[ix][iy];
            }
        }

		return resultMap;
	}

	//---------------------------------
	private int findCenterNearestToNeuron(
            int nx,int ny, ArrayList<Point> centers){
        double[] nVector = weights[nx][ny];            
        ArrayList<Double> cDist = new ArrayList<Double>();
        for(int ic=0; ic<centers.size(); ic++){
            cDist.add(distType.getDistance(
                    weights[centers.get(ic).x][centers.get(ic).y], 
                    nVector));
        }

        int nearest = 0;
        for(int ic=1; ic<centers.size(); ic++){
            if(
                    cDist.get(ic)
                    <
                    cDist.get(nearest))
                nearest = ic;
        }

        return nearest;
    }

	//---------------------------------
	private double[][] calcDist(double[] vector)
	{
		double[][] distances = new double[xMax][];
		
		for(int ix=0; ix<xMax; ix++)
			distances[ix] = new double[yMax];
		
		for(int ix=0; ix<xMax; ix++){
			for(int iy=0; iy<yMax; iy++){
				distances[ix][iy] =
                        distType.getDistance(weights[ix][iy], vector);
            }
        }
		
		return distances;
	}
	
	//---------------------------------
	private boolean isCenter(Point p, ArrayList<Point> centers) {
		for(int c=0; c<centers.size(); c++)
			if(centers.get(c).x == p.x && centers.get(c).y == p.y)
				return true;
		return false;
	}

	//------------------------------------------------------
	// getters
	public int getTMax() {
		return tMax;
	}

	public int getXMax() {
		return xMax;
	}

	public int getYMax() {
		return yMax;
	}

	public SOMDistanceFunction getDistType() {
		return distType;
	}

	public SOMWeightsInitializer getwInit() {
		return wInit;
	}

	public SOMTrainingMethod getTrainer() {
		return trainer;
	}

	public SOMWinnerSelection getSelector() {
		return selector;
	}

	public double[][][] getWeights() {
		return weights;
	}

	//------------------------------------------------------
    private boolean checkWeights(double[][][] weights) {
        for(int x=0; x<weights.length; x++)
            for(int y=0; y<weights[0].length; y++)
                for(int w=0; w<weights[0][0].length; w++){
                    String s=Double.toString(weights[x][y][w]);
                    if(s.equals("NaN")){
                        System.out.println(
                                weights[x][y][w]+"; x: "+x
                                +"; y: "+y+"; w: "+w+";");

                        return true;
                    }
                }

        return false;
    }
}
