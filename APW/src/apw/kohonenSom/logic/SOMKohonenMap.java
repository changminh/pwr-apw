package apw.kohonenSom.logic;

import apw.kohonenSom.distances.SOMDistanceFunction;
import apw.kohonenSom.logic.topology.SOMTopology;
import apw.kohonenSom.logic.trainingMethods.SOMTrainingMethod;
import apw.kohonenSom.logic.winnerSelection.SOMWinnerSelection;
import apw.kohonenSom.util.SOMOrderRandomizer;
import apw.kohonenSom.util.SOMPrinter;
import apw.kohonenSom.weightsInitialization.SOMWeightsInitializer;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Christopher Wadowski
 */
public class SOMKohonenMap implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//------------------------------------------------------
	private int TMax;
	
	private int XMax;
	private int YMax;
	
	private SOMDistanceFunction distType;
	private SOMWeightsInitializer wInit;
	private SOMTrainingMethod trainer;
	private SOMWinnerSelection selector;
    private SOMTopology links;
	
	//------------------------------------------------------
	private double[][][] weights;
	
	//------------------------------------------------------
	private SOMOrderRandomizer orderRand;

    //------------------------------------------------------
	//constructors
	public SOMKohonenMap(int TMax, int XMax, int YMax,
			SOMDistanceFunction distanceType, SOMWinnerSelection selector,
			SOMWeightsInitializer wInit, SOMTrainingMethod trainer,
            SOMTopology links)
	{	
		this.TMax = TMax;
		
		this.XMax = XMax;
		this.YMax = YMax;
	
		this.distType = distanceType;
		this.wInit = wInit;
		this.trainer = trainer;
		this.selector = selector;
        this.links = links;

		this.orderRand = new SOMOrderRandomizer();
		
		weights = wInit.initializeWeights();
	}

	//------------------------------------------------------
	public Point sendSignal(double[] vector)
	{
		Point winner;
		
		double[][] distances = calcDist(vector);
		
		winner = SOMWinnerSelection.simpleWinnerSelection(distances);
		
		return winner;
	}
	
	//---------------------------------
    //training
	public void trainIterative(ArrayList<double[]> patterns)
	{		
		double[][] distances;	
		Point winner;		
		int T, time;

		for(T=0, time=1; T<TMax; T++)
		{
            ArrayList<Integer> patternOrder =  orderRand.randomizeOrder(patterns.size());
            
            for(int p=0; p<patterns.size(); p++, time++)
            {
				double[] vector = patterns.get(patternOrder.get(p));             
				distances = calcDist(vector);
				winner = chooseWinner(distances, time);
				weights = trainer.adaptWeights(vector, weights, winner, time);              
			}
            /*
            if(checkWeights(weights)){
                System.out.println(
                        "error weights, e: "+T+"; t: "+time+"; ");
            }*/
		}

        if(selector != null)
			selector.resetSelector();
	}
	
	//---------------------------------
	public void resetNetwork()
	{
		weights = wInit.initializeWeights();

        if(selector != null)
			selector.resetSelector();
	}

	//------------------------------------------------------
	//visualization data generation
    public ArrayList<Point> generateClusterCenters(
            ArrayList<double[]> patterns){
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
    public ArrayList<Integer>[][] generateClustersMap(
            ArrayList<double[]> patterns)
	{
        ArrayList<Integer>[][] map =
                this.generatePatternsPositionsMap(patterns);

        ArrayList<Point> centers =
                this.generateClusterCenters(patterns);
		
		fillEmptyNeurons(map, centers);

        return map;
	}

    //---------------------------------
    public int[][] generatePatternsDensityMap(
            ArrayList<double[]> patterns){
        int[][] map = new int[XMax][];

        for(int ix=0; ix<XMax; ix++){
            map[ix] = new int[YMax];
            for(int iy=0; iy<YMax; iy++){
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
            ArrayList<double[]> patterns){
        ArrayList<Integer>[][] map = new ArrayList[XMax][];

        for(int ix=0; ix<XMax; ix++){
            map[ix] = new ArrayList[YMax];
            for(int iy=0; iy<YMax; iy++){
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

        int ux = this.XMax*2 - 1;
        int uy = this.YMax*2 - 1;

        uMap = new double[ux][];

        for(int iux=0; iux<ux; iux++){
            uMap[iux] = new double[uy];
        }

        for(int ix=0, iux=0; ix< this.XMax; ix++, iux+=2){
            for(int iy=0, iuy=0; iy< this.YMax; iy++, iuy+=2){
                double xy = 0;
                int n = 0;
                Point neuron = new Point(ix, iy);
                ArrayList<Point> neighbours =
                        links.getNeighbours(neuron, XMax, YMax);

                for(int in=0; in<neighbours.size(); in++){
                    Point nNeuron = neighbours.get(in);
                    int dx = nNeuron.x - neuron.x;
                    int dy = nNeuron.y - neuron.y;
                    
                    double[] v1 = weights[ix][iy];
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
	private void fillEmptyNeurons(
            ArrayList<Integer>[][] map, ArrayList<Point> centers)
	{
		for(int x=0; x<XMax; x++)
			for(int y=0; y<YMax; y++)
				if(map[x][y].size() == 0)
				{
					int nc = chooseNearestCenter(x,y, centers);
					
					map[x][y] = map[centers.get(nc).x][centers.get(nc).y];
				}
		
	}
	
	//---------------------------------
	private int chooseNearestCenter(int x, int y, ArrayList<Point> centers) {
		
		int result = 0;
		
		double resDist, newDist;
		double[] v1, v2;
		Point p1, p2;
		
		double[] w = weights[x][y];
		
		for(int c=1; c<centers.size(); c++)
		{			
			p1 = centers.get(result);
			p2 = centers.get(c);
			
			v1 = weights[p1.x][p1.y];
			v2 = weights[p2.x][p2.y];
			
			resDist = distType.getDistance(v1, w);	
			newDist = distType.getDistance(v2, w);	
			
			if(resDist > newDist)
				result = c;
		}
		
		return result;
	}

	//---------------------------------
	private Point chooseWinner(double[][] distances, int time) {
		if(selector == null)
			return SOMWinnerSelection.simpleWinnerSelection(distances);
		else
			return selector.complexWinnerSelection(distances, time);
	}
	
	//---------------------------------
	private double[][] calcDist(double[] pattern)
	{
		double[][] d = new double[XMax][];
		
		for(int x=0; x<XMax; x++)
			d[x] = new double[YMax];
		
		for(int x=0; x<XMax; x++)
			for(int y=0; y<YMax; y++)		
				d[x][y] = distType.getDistance(weights[x][y], pattern);	
		
		return d;
	}
	
	//---------------------------------
	private boolean isCenter(Point p, ArrayList<Point> centers) {
		for(int c=0; c<centers.size(); c++)
			if(centers.get(c).x == p.x && centers.get(c).y == p.y)
				return true;
		return false;
	}

    //---------------------------------
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

	//------------------------------------------------------
	// getters
	public int getTMax() {
		return TMax;
	}

	public int getXMax() {
		return XMax;
	}

	public int getYMax() {
		return YMax;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	//------------------------------------------------------

}
