package apw.kohonenSom.logic.nuronDistances;

import java.awt.Point;

public class SOMNeuronNumber implements SOMNeuronsDistance{

	@Override
	public double calcDistance(Point neuronA, Point neuronB) {
		double d = 0;
		
		d += java.lang.Math.abs(neuronA.x - neuronB.x);
		d += java.lang.Math.abs(neuronA.y - neuronB.y);
		
		return d;
	}

}
