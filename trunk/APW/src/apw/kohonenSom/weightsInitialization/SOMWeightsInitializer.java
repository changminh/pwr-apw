package apw.kohonenSom.weightsInitialization;

import apw.kohonenSom.patterns.SOMSamplesLoader;

public interface SOMWeightsInitializer {
	public abstract double[][][] initializeWeights(
            SOMSamplesLoader samples, int width, int height);
}
