package apw.kohonenSom.learningFactors;

public class SOMHyperbolicdecrease implements SOMLearningFactor {
	private double etaMax;
	private double C;

    private static final double endEta = 0.0001;

	public SOMHyperbolicdecrease(double etaMax, double C)
	{
		this.etaMax = etaMax;
		this.C = C;
	}
	
	@Override
	public double getEta(double time) {
		return java.lang.Math.max(etaMax*(C/(C+time)), endEta);
	}

}
