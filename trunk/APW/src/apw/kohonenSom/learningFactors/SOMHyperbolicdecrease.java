package apw.kohonenSom.learningFactors;

public class SOMHyperbolicDecrease implements SOMLearningFactor {
	private double etaMax;
	private double C;

    private static final double endEta = 0.0001;

	public SOMHyperbolicDecrease(double etaMax, double C)
	{
		this.etaMax = etaMax;
		this.C = C;
	}
	
	@Override
	public double getEta(double time) {
        double eta = etaMax*(C/(C+time));
		return java.lang.Math.max(eta, endEta);
	}

}
