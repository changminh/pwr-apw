package apw.kohonenSom.timeFactors;

public class SOMHyperbolicTimeFactor implements SOMTimeFactor {
	
	private double C;

    private static final double endModif = 0.0001;
	
	public SOMHyperbolicTimeFactor(double C)
	{
		this.C = C;
	}
	
	@Override
	public double getTimeModifier(double t) {
		double modifier =  C/(C + t);
        return java.lang.Math.max(modifier, endModif);
	}

}
