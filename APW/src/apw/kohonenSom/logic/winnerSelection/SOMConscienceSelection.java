package apw.kohonenSom.logic.winnerSelection;

import java.awt.Point;
import java.util.Random;

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
			potentials[i] = new double[y];
		
		xm = x;
		ym = y;
		nn = x*y;
	}
	
	private Point chooseWinnerConscience(double[][] distances)
	{
		Random  rand = new Random();

		int xMax = distances.length;
		int yMax = distances[0].length;

        int x = rand.nextInt(xMax);
        int y = rand.nextInt(yMax);

        Point winner = new Point(x,y);
		
		for(int ix=0; ix<xm; ix++)
			for(int iy=0; iy<ym; iy++)
			{
				if(distances[ix][iy] < distances[winner.x][winner.y] &&
						potentials[ix][iy] >= pMin)
				{
					winner.x = ix;
					winner.y = iy;
				}
				else if(distances[ix][iy] == distances[winner.x][winner.y] &&
						potentials[ix][iy] >= pMin &&
						potentials[ix][iy] > potentials[winner.x][winner.y])
				{
					winner.x = ix;
					winner.y = iy;
				} 
			}
			
		adjustPotentials(winner);		
		return winner;
	}
	
	private void adjustPotentials(Point winner)
	{	
		for(int ix=0; ix<xm; ix++)
			for(int iy=0; iy<ym; iy++)
			{
				if(ix==winner.x && iy==winner.y)
					potentials[ix][iy] = java.lang.Math.max(
                            0, potentials[ix][iy] - pMin);
				else
					potentials[ix][iy] = java.lang.Math.min(
                            1, (potentials[ix][iy] + 1.0/(double)nn));
			}
	}

	@Override
	public Point winnerSelection(double[][] distances, int time) {
        if(time<tMax){
            if(time == 0){
                this.resetSelector();
            }
			return chooseWinnerConscience(distances);
        }
		else{
			return winnerSelection(distances);
        }
	}

	private void resetSelector() {
		for(int i=0; i<potentials.length; i++)
			for(int j=0; j<potentials[i].length; j++)
				potentials[i][j] = 1;		
	}
	
}
