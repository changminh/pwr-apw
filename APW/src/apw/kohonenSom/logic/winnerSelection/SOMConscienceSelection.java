package apw.kohonenSom.logic.winnerSelection;

import java.awt.Point;

public class SOMConscienceSelection extends SOMWinnerSelection {
	
	private double[][] potentials;
	
	private double pMin;
	
	private int nn, xm, ym;
	
	private int tMax;
	
	public SOMConscienceSelection(int x, int y, double pMin, int maxTime)
	{
		this.pMin = pMin;
		this.tMax = maxTime;
		
		potentials = new double[x][];
		
		for(int i=0; i<x; i++)
		{
			potentials[i] = new double[y];
			
			for(int j=0; j<y; j++)
				potentials[i][j] = 1;
		}
		
		xm = potentials.length;
		ym =  potentials[0].length;
		nn = ym*xm;
	}
	
	private Point chooseWinnerConscience(double[][] distances)
	{
		Point winner = new Point();
		
		for(int x=0; x<xm; x++)
			for(int y=0; y<ym; y++)
			{
				if(distances[x][y] < distances[winner.x][winner.y] &&
						potentials[x][y] >= pMin)
				{
					winner.x = x;
					winner.y = y;
				}
				else if(distances[x][y] == distances[winner.x][winner.y] &&
						potentials[x][y] >= pMin &&
						potentials[x][y] > potentials[winner.x][winner.y])
				{
					winner.x = x;
					winner.y = y;
				} 
			}
			
		adjustPotentials(winner);		
		return winner;
	}
	
	private void adjustPotentials(Point winner)
	{	
		for(int x=0; x<xm; x++)
			for(int y=0; y<ym; y++)
			{
				if(x==winner.x && y==winner.y)
					potentials[x][y] = java.lang.Math.max(0, potentials[x][y] - pMin);
				else
					potentials[x][y] = java.lang.Math.min(1, (potentials[x][y] + 1.0/(double)nn));
			}
	}

	@Override
	public Point complexWinnerSelection(double[][] distances, int time) {
		if(time<tMax)
			return chooseWinnerConscience(distances);
		else
			return super.simpleWinnerSelection(distances);
	}

	@Override
	public void resetSelector() {
		for(int i=0; i<potentials.length; i++)
			for(int j=0; j<potentials[i].length; j++)
				potentials[i][j] = 1;
		
	}
	
}
