package apw.kohonenSom.learningFactors;

public class SOMExponentialDecrease implements SOMLearningFactor {
	private double etaMax;
	private double C;

	public SOMExponentialDecrease(double etaMax, double C)
	{
		this.etaMax = etaMax;
		this.C = C;
	}
	
	@Override
	public double getEta(double time) {
		return etaMax*java.lang.Math.exp(-(C*time));
	}

}
