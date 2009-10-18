package apw.kohonenSom.logic.nuronDistances;

import java.awt.Point;
import java.util.ArrayList;

public interface SOMNeuronsDistance {
	public abstract double calcDistance(
            Point origin, Point destination);
    public ArrayList<Point> getNeighbours(
            Point origin, int xMax, int yMax);
    public ArrayList<Point> getNeighbours(
            Point origin, int xMax, int yMax, int n);
}
