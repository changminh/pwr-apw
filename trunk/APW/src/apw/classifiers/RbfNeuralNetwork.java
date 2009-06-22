/*
 *  Copyright (c) 2009, Wrocław University of Technology
 *  All rights reserved.
 *  Redistribution  and use in source  and binary  forms,  with or
 *  without modification,  are permitted provided that the follow-
 *  ing conditions are met:
 *
 *   • Redistributions of source code  must retain the above copy-
 *     right  notice, this list  of conditions and  the  following
 *     disclaimer.
 *   • Redistributions  in binary  form must  reproduce the  above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the  documentation and/or other mate provided
 *     with the distribution.
 *   • Neither  the name of the  Wrocław University of  Technology
 *     nor the names of its contributors may be used to endorse or
 *     promote products derived from this  software without speci-
 *     fic prior  written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRI-
 *  BUTORS "AS IS" AND ANY  EXPRESS OR IMPLIED WARRANTIES, INCLUD-
 *  ING, BUT NOT  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTA-
 *  BILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 *  NO EVENT SHALL THE COPYRIGHT HOLDER OR  CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCURE-
 *  MENT OF SUBSTITUTE  GOODS OR SERVICES;  LOSS OF USE,  DATA, OR
 *  PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER  CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING  NEGLIGENCE  OR  OTHERWISE) ARISING IN ANY WAY
 *  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSI-
 *  BILITY OF SUCH DAMAGE.
 */
package apw.classifiers;

import apw.core.algorithms.KMeansAlgorithm;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static apw.core.algorithms.DistanceMetrics.getEuclideanDistance;
import static apw.core.util.MiscUtils.copyMatrix;

/**
 *
 * @author Waldemar Szostak < wszostak@wp.pl >
 */
public class RbfNeuralNetwork extends Classifier {
    final static double MAX_ACCEPTABLE_ERROR = 0.4;

    private double[][] weights;
    private double[][] centres;
	private double[] deviations;

    private Samples samples;
    private int noOfHiddenNodes;
    private int noOfOutputNodes;

	private double learningRate = 0.01;

    private boolean rebuildNeeded;


    public RbfNeuralNetwork(final Samples samples, final int noOfCentres) {
        super(samples);

        if (samples.size() == 0) {
            throw new RuntimeException("The samples object cannot be empty");
        }

		/* If the class attribute is numeric, we want to output just one value,
		 * but if we have nominal class attribute, let's have as many  */
		this.noOfOutputNodes = samples.getClassAttribute().isNominal()
				? ((Nominal) samples.getClassAttribute()).getKeys().size() : 1;

		this.noOfHiddenNodes = noOfCentres == -1 ? noOfOutputNodes : noOfCentres;
		this.samples = samples;

        this.rebuildNeeded = true;
    }

	public void setNoOfOutputNodes(final int noOfOutputNodes) {
		this.noOfOutputNodes = noOfOutputNodes;
		rebuildNeeded = true;
	}

	public void setNoOfHiddenNodes(final int noOfHiddenNodes) {
		this.noOfHiddenNodes = noOfHiddenNodes;
		rebuildNeeded = true;
	}

	public void setLearningRate(final int learningRate) {
		this.learningRate = learningRate;
		rebuildNeeded = true;
	}

    public RbfNeuralNetwork(final Samples samples) {
        this(samples, -1);
    }

    public RbfNeuralNetwork(final String inputPath) throws IOException, ParseException {
        this(new ARFFLoader(new File(inputPath)).getSamples());
    }

    public static void main(String[] args) throws Exception {
        RbfNeuralNetwork network = new RbfNeuralNetwork("data/iris.arff");



		network.rebuild();

    }

    @Override
    public double[] classifySample(final Sample sample) {
        if (rebuildNeeded) {
            throw new IllegalStateException("The classifier has been modified " +
                    "(new sample/-s have been added) and has to be rebuilt");
        }

        return getOutputs(sample);
    }

	private double[] getOutputs(final Sample sample) {
		final double[] result = new double[noOfOutputNodes];
		final double[] hiddenOutputs = getHiddenOutputs(sample);

		for (int k = 0; k < noOfOutputNodes; k++) {
			for (int j = 0; j < noOfHiddenNodes; j++) {
				result[k] += weights[j][k] * hiddenOutputs[j];
			}
		}

		return result;
	}

    private double[] getHiddenOutputs(final Sample sample) {
		final double[] result = new double[noOfHiddenNodes];
		final double[] inputs = sample.toDoubleArray();

        for (int j = 0; j < noOfHiddenNodes; j++) {
			final double tmp = getEuclideanDistance(inputs, centres[j]) / deviations[j];
			result[j] = Math.exp(-0.5 * tmp * tmp);
		}

		return result;
    }

    @Override
    public void rebuild() {
		this.deviations = new double[noOfHiddenNodes];
        this.centres = KMeansAlgorithm.findClusterCentres(samples, noOfHiddenNodes);

		findDeviations();

        weights = new double[noOfHiddenNodes][noOfOutputNodes];
		for (double[] row : weights) {
			for (int i = 0; i < row.length; i++) {
				row[i] = Math.random();
			}
		}
        findWeights();

        rebuildNeeded = false;
    }

	private void findDeviations() {
		if (centres.length == 1) {
			deviations[0] = 0.5;	// how this should be handled??
		}

		
		for (int i = 0; i < deviations.length; i++) {
			double minDistance = 9999999;
			for (int j = 0; j < deviations.length; j++) {
				if (i != j) {
System.out.println("i: " + i + "; j: " + j);
					final double tmpDistance = getEuclideanDistance(centres[i], centres[j]);

					if (tmpDistance < minDistance) {
						minDistance = tmpDistance;
					}
				}
			}

			deviations[i] = minDistance;
		}
	}

    private void findWeights() {
        double error;

        do {
            error = 0;

            for (final Sample sample : samples) {
				final double targetValue = (Double) sample.classAttributeRepr();
				final double[] hiddenOutputs = getHiddenOutputs(sample);

				for (int k = 0; k < noOfOutputNodes; k++) {
					double outputNode = 0;

					for (int j = 0; j < noOfHiddenNodes; j++) {
						outputNode += weights[j][k] * hiddenOutputs[j];
					}

					double tmp = (targetValue == k ? 1 : 0) - outputNode;
					error += tmp * tmp;	// this line has to stay here, because tmp is going to change...

					tmp *= learningRate;	// now: tmp = outputDiff * learningRate
					for (int j = 0; j < noOfHiddenNodes; j++) {
						weights[j][k] = (weights[j][k] + tmp * hiddenOutputs[j]) % 1;
					}
				}
				
				error *= 0.5;
			}
        } while (error > MAX_ACCEPTABLE_ERROR);
    }

    @Override
    public Classifier copy() {
        final RbfNeuralNetwork result = new RbfNeuralNetwork(samples, centres.length);
		
		result.weights = copyMatrix(noOfHiddenNodes, noOfOutputNodes, weights);
		result.centres = copyMatrix(noOfHiddenNodes, 2, centres);

		result.deviations = new double[noOfHiddenNodes];
		System.arraycopy(deviations, 0, result.deviations, 0, noOfHiddenNodes);

		result.learningRate = learningRate;
		result.noOfHiddenNodes = noOfHiddenNodes;
		result.noOfOutputNodes = noOfOutputNodes;
		result.rebuildNeeded = false;

        return result;
    }

    @Override
    public void addSamples(final Samples s) {
        samples.addAll(s);
        rebuildNeeded = true;
    }

    @Override
    public void addSample(final Sample s) {
        samples.add(s);
        rebuildNeeded = true;
    }
}