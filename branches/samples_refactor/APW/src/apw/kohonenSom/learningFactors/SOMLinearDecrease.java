package apw.kohonenSom.learningFactors;

public class SOMLinearDecrease implements SOMLearningFactor {
	private double etaMax;
	private double TMax;

    private double endEta;
	
	public SOMLinearDecrease(double etaMax, double TMax, double etaMin)
	{
		this.etaMax = etaMax;
        this.endEta = etaMin;
		this.TMax = TMax;
	}
	
	@Override
	public double getEta(double time) {
        double eta = etaMax*((TMax - time)/TMax);
		return java.lang.Math.max(eta, endEta);
	}

}
