package apw.kohonenSom.logic.winnerSelection;

import java.awt.Point;

public class SOMCountSelection extends SOMWinnerSelection {
	
	private int[][] counts;
	
	private int xm, ym;
	
	private int tMax;
	
	public SOMCountSelection(int x, int y, int maxTime)
	{	
		this.tMax = maxTime;
		
		counts = new int[x][];
		
		for(int i=0; i<x; i++)
			counts[i] = new int[y];
		
		xm = counts.length;
		ym =  counts[0].length;
	}

	private Point chooseWinnerCount(double[][] distances) {
		Point winner = new Point(0,0);
		
		for(int x=0; x<xm; x++)
			for(int y=0; y<ym; y++)
				if(distances[x][y]*counts[x][y] < distances[winner.x][winner.y]*counts[winner.x][winner.y])
				{
					winner.x = x;
					winner.y = y;
				}
			
		counts[winner.x][winner.y]++;	
		return winner;
	}

	@Override
	public Point complexWinnerSelection(double[][] distances, int time) {
		if(time<=tMax)
			return chooseWinnerCount(distances);
		else
			return super.simpleWinnerSelection(distances);
	}

	@Override
	public void resetSelector() {
		for(int i=0; i<counts.length; i++)
			for(int j=0; j<counts[i].length; j++)
				counts[i][j] = 0;
		
	}
	
}
