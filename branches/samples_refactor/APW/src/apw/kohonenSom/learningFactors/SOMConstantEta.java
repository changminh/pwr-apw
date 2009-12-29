package apw.kohonenSom.learningFactors;

/**
 *
 * @author Christopher Wadowski
 */
public class SOMConstantEta implements SOMLearningFactor {
    private double eta;

	public SOMConstantEta(double eta)
	{
		this.eta = eta;
	}

	@Override
    public double getEta(double time) {
        return eta;
    }

}
