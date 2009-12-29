package apw.kohonenSom.topology;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Christopher Wadowski
 */
public class SOMRectangonalNetwork implements SOMTopology{

	@Override
	public double calcDistance(Point origin, Point destination) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

    @Override
    public ArrayList<Point> getNeighbours(Point origin, int xMax, int yMax) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Point> getNeighbours(
            Point origin, int xMax, int yMax, int n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
