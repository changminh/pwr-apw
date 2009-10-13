package apw.kohonenSom.timeFactors;

public class SOMHyperbolicTimeFactor implements SOMTimeFactor {
	
	private double C;
	
	public SOMHyperbolicTimeFactor(double C)
	{
		this.C = C;
	}
	
	@Override
	public double getTimeModifier(double t) {
		return C/(C + t);
	}

}
