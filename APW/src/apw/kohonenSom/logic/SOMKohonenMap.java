package apw.kohonenSom.logic;

import apw.kohonenSom.distances.SOMDistanceFunction;
import apw.kohonenSom.logic.trainingMethods.SOMTrainingMethod;
import apw.kohonenSom.logic.winnerSelection.SOMWinnerSelection;
import apw.kohonenSom.util.SOMOrderRandomizer;
import apw.kohonenSom.weightsInitialization.SOMWeightsInitializer;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

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
	
	//------------------------------------------------------
	private double[][][] weights;
	private ArrayList<Integer>[][] map;
	private ArrayList<Point> centers;
	
	//------------------------------------------------------
	private SOMOrderRandomizer orderRand;
	
	
	//constructors
	public SOMKohonenMap(int TMax, int XMax, int YMax,
			SOMDistanceFunction distanceType, SOMWinnerSelection selector,
			SOMWeightsInitializer wInit, SOMTrainingMethod trainer
			) throws Exception
	{	
		this.TMax = TMax;
		
		this.XMax = XMax;
		this.YMax = YMax;
	
		this.distType = distanceType;
		this.wInit = wInit;
		this.trainer = trainer;
		this.selector = selector;

		this.orderRand = new SOMOrderRandomizer();
		
		initNetwork();
	}

	//------------------------------------------------------
	public Point sendSignal(double[] vector)
	{
		Point winner = new Point();
		
		double[][] distances = calcDist(vector);
		
		winner = SOMWinnerSelection.simpleWinnerSelection(distances);
		
		return winner;
	}
	
	//---------------------------------
	public void trainIterative(ArrayList<double[]> patterns)
	{	
		resetNetwork();
		
		double[][] distances;	
		Point winner;		
		int T;
		int time;
		
		for(T=0, time=0; T<TMax; T++)
		{
			ArrayList<Integer> patternOrder =  orderRand.randomizeOrder(patterns.size());
			
			for(int p=0; p<patterns.size(); p++, time++)
			{
				double[] vector = patterns.get(patternOrder.get(p));
				
				distances = calcDist(vector);
				winner = chooseWinner(distances, time);
				weights = trainer.adaptWeights(vector, weights, winner, time);
			}
		}
		
		createMap(patterns);
	}
	
	//---------------------------------
	public void resetNetwork()
	{
		for(int x=0; x<XMax; x++)
			for(int y=0; y<YMax; y++)
				map[x][y].clear();
		centers.clear();	
		wInit.initializeWeights();
	}

	//------------------------------------------------------
	//private methods
	private void createMap(ArrayList<double[]> patterns)
	{
		double[] vector;
		Point winner;
		
		for(int p=0; p<patterns.size(); p++)
		{
			vector = patterns.get(p);		
			winner = sendSignal(vector);		
			map[winner.x][winner.y].add(p);
			if(!isCenter(winner))
				centers.add(winner);
		}
		
		//TODO fill empty neurons with nearest patterns?
		boolean fill = false;
		
		if(fill)
			fillEmptyNeurons();
	}
	
	//---------------------------------
	private void fillEmptyNeurons()
	{
		for(int x=0; x<XMax; x++)
			for(int y=0; y<YMax; y++)
				if(map[x][y].size() == 0)
				{
					int nc = chooseNearestCenter(x,y);
					
					map[x][y] = map[centers.get(nc).x][centers.get(nc).y];
				}
		
	}
	
	//---------------------------------
	private int chooseNearestCenter(int x, int y) {
		
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
	@SuppressWarnings("unchecked")
	private void initNetwork() {
		map = new ArrayList[XMax][];
		for(int x=0; x<XMax; x++)
		{
			map[x] = new ArrayList[YMax];
			for(int y=0; y<YMax; y++)
				map[x][y] = new ArrayList<Integer>();
		}
		centers = new ArrayList<Point>();
		wInit.initializeWeights();
	}
	
	//---------------------------------
	private boolean isCenter(Point p) {
		for(int c=0; c<centers.size(); c++)
			if(centers.get(c).x == p.x && centers.get(c).y == p.y)
				return true;
		return false;
	}

	//------------------------------------------------------
	// getters and setters
	public int getTMax() {
		return TMax;
	}

	/*
	public void setTMax(int tMax) {
		TMax = tMax;
	}*/

	public int getXMax() {
		return XMax;
	}

	/*
	public void setXMax(int xMax) {
		XMax = xMax;
	}*/

	public int getYMax() {
		return YMax;
	}

	/*
	public void setYMax(int yMax) {
		YMax = yMax;
	}*/

	public SOMDistanceFunction getDistType() {
		return distType;
	}

	/*
	public void setDistType(SOMDistanceFunction distType) {
		this.distType = distType;
	}*/

	public SOMWeightsInitializer getwInit() {
		return wInit;
	}

	/*
	public void setwInit(SOMWeightsInitializer wInit) {
		this.wInit = wInit;
	}*/

	public SOMTrainingMethod getTrainer() {
		return trainer;
	}

	/*
	public void setTrainer(SOMTrainingMethod trainer) {
		this.trainer = trainer;
	}*/

	public SOMWinnerSelection getSelector() {
		return selector;
	}

	/*
	public void setSelector(SOMWinnerSelection selector) {
		this.selector = selector;
	}*/

	public double[][][] getWeights() {
		return weights;
	}

	/*
	public void setWeights(double[][][] weights) {
		this.weights = weights;
	}*/

	public ArrayList<Integer>[][] getMap() {
		return map;
	}

	/*
	public void setMap(ArrayList<Integer>[][] map) {
		this.map = map;
	}*/

	public ArrayList<Point> getCenters() {
		return centers;
	}

	/*
	public void setCenters(ArrayList<Point> centers) {
		this.centers = centers;
	}*/

	/*
	public SOMOrderRandomizer getOrderRand() {
		return orderRand;
	}*/

	/*
	public void setOrderRand(SOMOrderRandomizer orderRand) {
		this.orderRand = orderRand;
	}*/

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	//------------------------------------------------------
	
}
