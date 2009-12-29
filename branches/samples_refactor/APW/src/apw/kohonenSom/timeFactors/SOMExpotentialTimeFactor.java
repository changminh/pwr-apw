package apw.kohonenSom.timeFactors;

public class SOMExpotentialTimeFactor implements SOMTimeFactor {

	private double C;

    private double endModif;
	
	public SOMExpotentialTimeFactor(double C, double minModif)
	{
		this.C = C;
        this.endModif = minModif;
	}
	
	@Override
	public double getTimeModifier(double t) {
        double modifier = java.lang.Math.exp(-1*t*C);
		return java.lang.Math.max(modifier, endModif);
	}

}
