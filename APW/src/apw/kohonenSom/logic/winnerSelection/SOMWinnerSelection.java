package apw.kohonenSom.logic.winnerSelection;

import java.awt.Point;
import java.util.Random;

public abstract class SOMWinnerSelection {

	public static Point simpleWinnerSelection(double[][] distances) {
        Random  rand = new Random();

		int xMax = distances.length;
		int yMax = distances[0].length;

        int x = rand.nextInt(xMax);
        int y = rand.nextInt(yMax);

        Point winner = new Point(x,y);

		for(int ix=0; ix<xMax; ix++)
			for(int iy=0; iy<yMax; iy++)
				if(distances[ix][iy] < distances[winner.x][winner.y])
				{
					winner.x = ix;
					winner.y = iy;
				}
			
		return winner;
	}
	
	public abstract Point complexWinnerSelection(double[][] distances, int time);
	
	public abstract void resetSelector();
}
