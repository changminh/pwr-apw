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

import static apw.core.algorithms.DistanceMetrics.getEuclideanDistance;
import static apw.core.util.MiscUtils.copyMatrix;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.JFileChooser;

import apw.core.Attribute;
import apw.core.Evaluator;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.algorithms.KMeansAlgorithm;
import apw.core.loader.ARFFLoader;
import apw.core.meta.ClassifierCapabilities;
import apw.core.util.PropertiesManager;
import apw.core.util.SamplesNormalizer;
import apw.gui.ResultPanel;

/**
 *
 * @author Waldemar Szostak
 */
@ClassifierCapabilities(handlesNumericAtts = true,
                        multiClass = true,
                        regression = true)
public class RbfNeuralNetwork extends Classifier {
	private final double MAX_ACCEPTABLE_ERROR;
	private final double LEARNING_RATE;
	private final double MOMENTUM;
	private final int NO_OF_CENTRES;
	private final long MAX_ITERATIONS;
	{
		final Properties properties = PropertiesManager.getProperties("rbfNeuralNetwork");

		MAX_ACCEPTABLE_ERROR = Double.valueOf(properties.getProperty("maxAcceptableError"));
		LEARNING_RATE = Double.valueOf(properties.getProperty("learningRate"));
		MOMENTUM = Double.valueOf(properties.getProperty("momentum"));
		NO_OF_CENTRES = Integer.valueOf(properties.getProperty("noOfCentres"));
		MAX_ITERATIONS = Long.valueOf(properties.getProperty("maxIterations"));
	}
    
    private double[][] outputWeights;
    private double[] outputBiases;
    private double[][] centres;
    private double[] deviations;
    private Samples samples;
    private final int noOfOutputNodes;
    
    private boolean rebuildNeeded;


    public RbfNeuralNetwork(final Samples samples) {
        super(samples);
        

        if (samples.size() == 0) {
            throw new RuntimeException("The samples object cannot be empty");
        }

        /* If the class attribute is numeric, we want to output just one value,
         * but if we have nominal class attribute, let's have as many  */
        final Attribute classAttr = samples.getClassAttribute();
        this.noOfOutputNodes = classAttr.isNominal() ? ((Nominal) classAttr).getKeys().size() : 1;
        this.samples = SamplesNormalizer.normalize(samples);

        this.rebuildNeeded = true;
    }

    public RbfNeuralNetwork(final String inputPath) throws IOException, ParseException {
        this(new ARFFLoader(new File(inputPath)).getSamples());
    }

    public static void main(String[] args) throws Exception {
    	args = new String[1];
    	args[0] = "/home/wsz/dev/workspace/apw/data/iris.arff";
    	
        File f = null;
        if (args.length > 0 && !args[0].isEmpty())
            f = new File(args[0]);
        else {
            // Ta sekcja jest po to, żeby móc wybrać łatwo zbiór testowy
            JFileChooser jf = new JFileChooser("data/");
            jf.showOpenDialog(null);
            f = jf.getSelectedFile();
        }

        if (f != null) {
            Samples samples = new ARFFLoader(f).getSamples();
            samples.setClassAttributeIndex(samples.getAtts().size()-1);

            RbfNeuralNetwork network = new RbfNeuralNetwork(samples);
            network.rebuild();
            
            // Evaluator to obiekt, który liczy miary jakości klasyfikacji
            Evaluator e = new Evaluator(network, samples);

            // To metoda statyczna prezentująca miary jakości w oknie Swing
            ResultPanel.showResultFrame(e);

            samples = SamplesNormalizer.normalize(samples);
            System.out.println("Correct classification ratio: " + samples.getCorrectClassificationRate(network));
        }
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
            for (int j = 0; j < NO_OF_CENTRES; j++) {
                result[k] += outputWeights[j][k] * hiddenOutputs[j];
            }
            result[k] += outputBiases[k];
        }

        return result;
    }

    private double[] getHiddenOutputs(final Sample sample) {
        final double[] result = new double[NO_OF_CENTRES];
        final double[] inputs = sample.toDoubleArray();

        for (int j = 0; j < NO_OF_CENTRES; j++) {
        	// exp(-r^2/(2*d^2)) = exp(-0.5 * tmp^2), tmp = r/d 
            final double tmp = getEuclideanDistance(inputs, centres[j]) / deviations[j];
            
            result[j] = Math.exp(-0.5 * tmp * tmp);
        }

        return result;
    }

    
    @Override
    public void rebuild() {
        this.deviations = new double[NO_OF_CENTRES];
        this.centres = KMeansAlgorithm.findClusterCentres(samples, NO_OF_CENTRES);
        
        findDeviations();
        initWeights();
        findWeights();

        rebuildNeeded = false;
    }

	private void initWeights() {
        outputWeights = new double[NO_OF_CENTRES][noOfOutputNodes];
        outputBiases = new double[noOfOutputNodes];

        for (int k = 0; k < noOfOutputNodes; k++) {
            for (int j = 0; j < NO_OF_CENTRES; j++) {
                outputWeights[j][k] = Math.random();
            }
            outputBiases[k] = Math.random();
        }
    }

    private void findDeviations() {
        System.out.print("Calculating standard deviations...");

        for (int i = 0; i < centres.length; i++) {
            double minDistance = Double.MAX_VALUE;
            for (int j = 0; j < centres.length; j++)
                if (i != j) {	// if it's not the same centre
                    final double tmpDistance = getEuclideanDistance(centres[i], centres[j]);

                    if (tmpDistance < minDistance && tmpDistance != 0) {
                        minDistance = tmpDistance;
                    }
                }

            deviations[i] = minDistance;
        }

        System.out.println(" done.");
    }

    private void findWeights() {
        System.out.print("Calculating network's weights: ");

        double error;
        
        double[][] weightChanges = new double[NO_OF_CENTRES][];
        for (int i = 0; i < NO_OF_CENTRES ; i++) {
        	weightChanges[i] = new double[noOfOutputNodes];
        	Arrays.fill(weightChanges[i], 0);
        }

        long curIteration = 0;
        do {
            System.out.print(".");
            error = 0;

            for (final Sample sample : samples) {
                final double targetValue = (Double) sample.classAttributeRepr();
                final double[] hiddenOutputs = getHiddenOutputs(sample);

                for (int k = 0; k < noOfOutputNodes; k++) {
                    double singleNodeOuput = outputBiases[k];

                    for (int j = 0; j < NO_OF_CENTRES; j++) {
                        singleNodeOuput += outputWeights[j][k] * hiddenOutputs[j];
                    }
                    
                    double targetCalculatedDiff = (targetValue == k ? 1 : 0) - singleNodeOuput;
                    error += targetCalculatedDiff * targetCalculatedDiff;

                    targetCalculatedDiff *= LEARNING_RATE;	// now: tmp = (targetValue - outputNode) * learningRate
                    for (int j = 0; j < NO_OF_CENTRES; j++) {
                    	weightChanges[j][k] = targetCalculatedDiff * hiddenOutputs[j] + MOMENTUM * weightChanges[j][k];
                        outputWeights[j][k] += weightChanges[j][k]; 
                    }
                    
                    outputBiases[k] = outputBiases[k] + targetCalculatedDiff;
                }       
            }
            
            error *= 0.5;

            System.out.println("\tError: " + error);

        } while (error > MAX_ACCEPTABLE_ERROR && ++curIteration < MAX_ITERATIONS);

        if (curIteration == MAX_ITERATIONS) {
        	System.out.println("Too many iterations, weight adjusting aborted.");
        } else {
        	System.out.println(" done.");
        }
    }
    
    @Override
    public Classifier copy() {
        final RbfNeuralNetwork result = new RbfNeuralNetwork(samples);

        result.outputWeights = copyMatrix(NO_OF_CENTRES, noOfOutputNodes, outputWeights);
        result.centres = copyMatrix(NO_OF_CENTRES, 2, centres);

        result.outputBiases = new double[noOfOutputNodes];
        System.arraycopy(outputBiases, 0, result.outputBiases, 0, noOfOutputNodes);

        result.deviations = new double[NO_OF_CENTRES];
        System.arraycopy(deviations, 0, result.deviations, 0, NO_OF_CENTRES);

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
