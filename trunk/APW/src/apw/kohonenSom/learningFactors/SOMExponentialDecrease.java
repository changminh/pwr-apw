package apw.kohonenSom.learningFactors;

public class SOMExponentialDecrease implements SOMLearningFactor {
	private double etaMax;
	private double C;

    private static final double endEta = 0.0001;

	public SOMExponentialDecrease(double etaMax, double C)
	{
		this.etaMax = etaMax;
		this.C = C;
	}
	
	@Override
	public double getEta(double time) {
        double eta = etaMax*java.lang.Math.exp(-(C*time));
		return java.lang.Math.max(eta, endEta);
	}

}
