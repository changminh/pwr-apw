package apw.kohonenSom.logic.winnerSelection;

import java.awt.Point;

public abstract class SOMWinnerSelection {

	public static Point simpleWinnerSelection(double[][] distances) {
		Point winner = new Point(0,0);
		
		int xMax = distances.length;
		int yMax = distances[0].length;
		
		for(int x=0; x<xMax; x++)
			for(int y=0; y<yMax; y++)
				if(distances[x][y] < distances[winner.x][winner.y])
				{
					winner.x = x;
					winner.y = y;
				}
			
		return winner;
	}
	
	public abstract Point complexWinnerSelection(double[][] distances, int time);
	
	public abstract void resetSelector();
}
