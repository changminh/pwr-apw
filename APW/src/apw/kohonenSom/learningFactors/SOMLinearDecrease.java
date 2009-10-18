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
		return java.lang.Math.max(etaMax*((1 - time)/TMax), endEta);
	}

}
