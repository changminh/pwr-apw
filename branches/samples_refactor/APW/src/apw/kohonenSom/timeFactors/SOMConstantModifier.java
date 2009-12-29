package apw.kohonenSom.timeFactors;

/**
 *
 * @author Krzysiek
 */
public class SOMConstantModifier implements SOMTimeFactor {
    private double mod;

	public SOMConstantModifier(double mod)
	{
		this.mod = mod;
	}

	@Override
	public double getTimeModifier(double t) {
		return mod;
	}
}
