package apw.kohonenSom.learningFactors;

public class SOMHyperbolicDecrease implements SOMLearningFactor {
	private double etaMax;
	private double C;

    private double endEta;

	public SOMHyperbolicDecrease(double etaMax, double C, double etaMin)
	{
		this.etaMax = etaMax;
        this.endEta = etaMin;
		this.C = C;
	}

	@Override
	public double getEta(double time) {
        double eta = etaMax*(C/(C+time));
		return java.lang.Math.max(eta, endEta);
	}

}
