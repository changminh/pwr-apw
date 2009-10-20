package apw.kohonenSom.learningFactors;

public class SOMLinearDecrease implements SOMLearningFactor {
	private double etaMax;
	private double TMax;

    private static final double endEta = 0.0001;
	
	public SOMLinearDecrease(double etaMax, double TMax)
	{
		this.etaMax = etaMax;
		this.TMax = TMax;
	}
	
	@Override
	public double getEta(double time) {
        double eta = etaMax*((TMax - time)/TMax);
		return java.lang.Math.max(eta, endEta);
	}

}
