package apw.kohonenSom.timeFactors;

public class SOMExpotentialTimeFactor implements SOMTimeFactor {

	private int C;
	
	public SOMExpotentialTimeFactor(int C)
	{
		this.C = C;
	}
	
	@Override
	public double getTimeModifier(double t) {
		return java.lang.Math.exp(-1*t*C);
	}

}
