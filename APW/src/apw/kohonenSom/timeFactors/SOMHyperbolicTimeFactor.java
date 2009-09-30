package apw.kohonenSom.timeFactors;

public class SOMHyperbolicTimeFactor implements SOMTimeFactor {
	
	private int C;
	
	public SOMHyperbolicTimeFactor(int C)
	{
		this.C = C;
	}
	
	@Override
	public double getTimeModifier(double t) {
		return C/(C + t);
	}

}
