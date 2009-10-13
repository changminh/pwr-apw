package apw.kohonenSom.timeFactors;

public class SOMExpotentialTimeFactor implements SOMTimeFactor {

	private double C;
	
	public SOMExpotentialTimeFactor(double C)
	{
		this.C = C;
	}
	
	@Override
	public double getTimeModifier(double t) {
		return java.lang.Math.exp(-1*t*C);
	}

}
