package apw.kohonenSom.learningFactors;

public class SOMLinearDecreas implements SOMLearningFactor {
	private double etaMax;
	private double TMax;
	
	public SOMLinearDecreas(double etaMax, double TMax)
	{
		this.etaMax = etaMax;
		this.TMax = TMax;
	}
	
	@Override
	public double getEta(double time) {
		return etaMax*((1 - time)/TMax);
	}

}
