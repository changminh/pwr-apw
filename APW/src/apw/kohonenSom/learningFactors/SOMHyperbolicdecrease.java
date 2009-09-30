package apw.kohonenSom.learningFactors;

public class SOMHyperbolicdecrease implements SOMLearningFactor {
	private double etaMax;
	private double C;

	public SOMHyperbolicdecrease(double etaMax, double C)
	{
		this.etaMax = etaMax;
		this.C = C;
	}
	
	@Override
	public double getEta(double time) {
		return etaMax*(C/(C+time));
	}

}
