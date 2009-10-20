package apw.kohonenSom.logic.winnerSelection;

import java.awt.Point;
import java.util.Random;

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
	
		xm = x;
		ym = y;
	}

	private Point chooseWinnerCount(double[][] distances) {
		Random  rand = new Random();

		int xMax = distances.length;
		int yMax = distances[0].length;

        int x = rand.nextInt(xMax);
        int y = rand.nextInt(yMax);

        Point winner = new Point(x,y);
		
		for(int ix=0; ix<xm; ix++)
			for(int iy=0; iy<ym; iy++)
				if(
                distances[ix][iy]*counts[ix][iy] <
                distances[winner.x][winner.y]*counts[winner.x][winner.y])
				{
					winner.x = ix;
					winner.y = iy;
				}
			
		counts[winner.x][winner.y]++;	
		return winner;
	}

	@Override
	public Point winnerSelection(double[][] distances, int time) {
		if(time<tMax){
            if(time == 0){
                this.resetSelector();
            }
			return chooseWinnerCount(distances);
        }
		else{
			return winnerSelection(distances);
        }
	}

	private void resetSelector() {
		for(int i=0; i<counts.length; i++)
			for(int j=0; j<counts[i].length; j++)
				counts[i][j] = 0;
		
	}
	
}
