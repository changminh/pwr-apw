package apw.kohonenSom.timeFactors;

public class SOMHyperbolicTimeFactor implements SOMTimeFactor {
	
	private double C;

    private double endModif;
	
	public SOMHyperbolicTimeFactor(double C, double minModif)
	{
		this.C = C;
        this.endModif = minModif;
	}
	
	@Override
	public double getTimeModifier(double t) {
		double modifier =  C/(C + t);
        return java.lang.Math.max(modifier, endModif);
	}

}
