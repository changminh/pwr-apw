package apw.kohonenSom.topology;

import java.awt.Point;
import java.util.ArrayList;

public interface SOMTopology {
	public abstract double calcDistance(
            Point origin, Point destination);
    public ArrayList<Point> getNeighbours(
            Point origin, int xMax, int yMax);
    public ArrayList<Point> getNeighbours(
            Point origin, int xMax, int yMax, int n);
}
