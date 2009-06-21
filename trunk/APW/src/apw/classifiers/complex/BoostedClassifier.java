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
package apw.classifiers.complex;

import apw.classifiers.Classifier;
import apw.core.Sample;
import apw.core.Samples;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Waldemar Szostak < wszostak@wp.pl >
 */
public class BoostedClassifier extends Classifier {
    final static Class[] CLASSIFIER_CONSTRUCTOR_PARAMETERS = new Class[]{Samples.class};
    final int SAMPLE_LAST_ATTRIBUTE_INDEX;
    
    private Class<Classifier>[] baseClassifiersClasses;
    private Classifier[] complexClassifier;
    private double[] baseClassifiersWeights;
    private Samples samples;
    private double[] samplesProbabilities;
    private boolean rebuildNeeded;


    public BoostedClassifier(final Class<Classifier>[] baseClassifiersClasses, final Samples samples) {
        super(samples);

        if (samples.size() == 0) {
            throw new RuntimeException("The samples object cannot be empty");
        }

        this.baseClassifiersClasses = baseClassifiersClasses;
        this.samples = samples;
        this.samplesProbabilities = new double[samples.size()];
        this.rebuildNeeded = true;

        SAMPLE_LAST_ATTRIBUTE_INDEX = samples.getAtts().size() - 1;
    }

    @Override
    public double[] classifySample(final Sample sample) {
        if (rebuildNeeded) {
            throw new IllegalStateException("The classifier has been modified " +
                    "(new sample/-s have been added) and has to be rebuilt");
        }

        final WeightedDecisions decisions = new WeightedDecisions();
        for (int classifierIndex = 0; classifierIndex < baseClassifiersWeights.length; ++classifierIndex) {
            final double[] decision = complexClassifier[classifierIndex].classifySample(sample);
            decisions.addDecision(decision, baseClassifiersWeights[classifierIndex]);
        }

        return decisions.getFinalDecision();
    }

    private class WeightedDecisions {
        private List<WeightedDecision> decisions = new ArrayList<WeightedDecision>();


        public void addDecision(final double[] decision, final double decisionWeight) {
            if (!updateExistingDecisionWeight(decision, decisionWeight)) {
                decisions.add(new WeightedDecision(decision, decisionWeight));
            }
        }

        private boolean updateExistingDecisionWeight(final double[] decisionToFind, final double decisionWeight) {
            for (final WeightedDecision weightedDecision : decisions) {
                if (weightedDecision.equals(decisionToFind)) {
                    weightedDecision.weight += decisionWeight;
                    return true;
                }
            }

            return false;
        }

        public double[] getFinalDecision() {
            Collections.sort(decisions);
            return decisions.get(0).decision;
        }

        private class WeightedDecision implements Comparable<WeightedDecision> {
            double[] decision;
            double weight;

            
            private WeightedDecision(double[] decision, double decisionWeight) {
                this.decision = decision;
                this.weight = decisionWeight;
            }

            public boolean equals(final double[] decisionToFind) {
                for (int i = 0; i < decision.length; ++i) {
                    if (decision[i] != decisionToFind[i]) {
                        return false;
                    }
                }

                return true;
            }

            public int compareTo(WeightedDecision other) {
                return (int) (other.weight - this.weight);
            }
        }
    }

    @Override
    public void rebuild() {
        resetSamplesProbabilities();

        try {
            for (int classifierIndex = 0; classifierIndex < baseClassifiersClasses.length; ++classifierIndex) {
                /* Create an instance of the current base classifier
                 * by calling its constructor with the modified sample set */
                final Classifier classifierInstance = getClassifierInstance(classifierIndex);

                /* Rebuild the base classifier to train it over the modified samples set */
                classifierInstance.rebuild();

                updateClassifierWeightAndSamplesProbabilities(classifierInstance, classifierIndex);

                complexClassifier[classifierIndex] = classifierInstance;
            }

            rebuildNeeded = false;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The base classifiers have to provide " +
                    "a public constructor accepting Samples object as its only parameter", e);
        } catch (SecurityException e) {
            throw new RuntimeException("The base classifiers have to provide " +
                    "a public constructor accepting Samples object as its only parameter", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("The base classifiers have to provide " +
                    "a public constructor accepting Samples object as its only parameter", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("The base classifiers have to provide " +
                    "a public constructor accepting Samples object as its only parameter", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("The base classifiers have to provide " +
                    "a public constructor accepting Samples object as its only parameter", e);
        }
    }

    private Samples getSamplesWithProbability() {

        final Samples result = new Samples(samples.getAtts());
        final WeightedSamples weightedSamples = new WeightedSamples();

        final int samplesCount = samples.size();
        for (int i = 0; i < samplesCount; i++) {
            result.add(weightedSamples.getRandomSample());
        }

        return result;
    }

    private class WeightedSamples {
        private WeightedSample[] weightedSamples;


        public WeightedSamples() {
            final int samplesCount = samples.size();

            weightedSamples = new WeightedSample[samplesCount];

            for (int i = 0; i < samplesCount; i++) {
                weightedSamples[i] = new WeightedSample(samples.get(i), samplesProbabilities[i]);
            }

            Arrays.sort(weightedSamples);

            for (int i = 1; i < samplesCount; i++) {
                weightedSamples[i].boundary += weightedSamples[i-1].boundary;
            }
        }

        public Sample getRandomSample() {
            final double random = Math.random();

            int sampleIndex = 1;
            while (sampleIndex < weightedSamples.length) {
                if (weightedSamples[sampleIndex].boundary > random) {
                    break;
                }
                
                sampleIndex++;
            }

            return weightedSamples[sampleIndex].sample;
        }

        private class WeightedSample implements Comparable<WeightedSample> {
            Sample sample;
            double boundary;


            public WeightedSample(Sample sample, double boundary) {
                this.sample = sample;
                this.boundary = boundary;
            }

            public int compareTo(WeightedSample other) {
                return (int) (other.boundary - this.boundary);
            }
        }
    }

    private void updateClassifierWeightAndSamplesProbabilities(final Classifier classifierInstance, final int classifierIndex) {
        final int samplesCount = samples.size();

        /* Calculate the error of the current base classifier
         * when classifying the original samples set */
        double classifierError = 0;
        final int[] classificationCorrectness = new int[samplesCount];

        for (int sampleIndex = 0; sampleIndex < samplesCount; ++sampleIndex) {
            final Sample sample = samples.get(sampleIndex);

            final Object targetValue = sample.get(SAMPLE_LAST_ATTRIBUTE_INDEX);
            final Object calculatedValue = classifierInstance.classifySampleAsObject(sample);

            /* Compare the expected classification class with the calculated one;
             * if they're different, add the probability of the current sample
             * to the current base classifier's error value */
            if (!targetValue.toString().equals(calculatedValue.toString())) {
                classifierError += samplesProbabilities[sampleIndex];
                classificationCorrectness[sampleIndex] = 1;
            } else {
                classificationCorrectness[sampleIndex] = -1;
            }
        }

        final double newClassifierWeight = 0.5 * Math.log((1 - classifierError) / classifierError);
        baseClassifiersWeights[classifierIndex] = newClassifierWeight;

        updateSampleProbabilities(classificationCorrectness, newClassifierWeight);
    }

    private void updateSampleProbabilities(final int[] classifierCorrectness, final double newClassifierWeight) {
        final int samplesCount = samples.size();
        double samplesProbabilitiesSum = 0;

        for (int sampleIndex = 0; sampleIndex < samplesCount; sampleIndex++) {
            final double newSampleProbability = samplesProbabilities[sampleIndex]
                    * Math.exp(classifierCorrectness[sampleIndex] * newClassifierWeight);
            samplesProbabilities[sampleIndex] = newSampleProbability;
            samplesProbabilitiesSum += newSampleProbability;
        }

        /* Normalize sample probabilities */
        for (int sampleIndex = 0; sampleIndex < samplesCount; sampleIndex++) {
            samplesProbabilities[sampleIndex] /= samplesProbabilitiesSum;
        }
    }

    private Classifier getClassifierInstance(int baseClassifierIndex) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SecurityException, IllegalArgumentException, InstantiationException {
        /* Build samples set taking into account their probability distribution */
        final Samples samplesWithProbability = getSamplesWithProbability();

        /* Create an instance of the current base classifier
         * by calling its constructor with the modified sample set */
        return (Classifier) baseClassifiersClasses[baseClassifierIndex].getConstructor(CLASSIFIER_CONSTRUCTOR_PARAMETERS).newInstance(samplesWithProbability);
    }

    private void resetSamplesProbabilities() {
        final int samplesCount = samples.size();
        final double defaultSampleProbability = 1 / samplesCount;

        for (int i = 0; i < samplesCount; ++i) {
            samplesProbabilities[i] = defaultSampleProbability;
        }
    }

    @Override
    public Classifier copy() {
        BoostedClassifier result = null;

        try {
            final Object[] baseClasifiersCopy = new Object[baseClassifiersClasses.length];
            System.arraycopy(baseClassifiersClasses, 0, baseClasifiersCopy, 0, baseClassifiersClasses.length);

            result = new BoostedClassifier((Class<Classifier>[]) baseClasifiersCopy, samples);
            result.rebuild();
        } catch (Exception ex) {
            Logger.getLogger(BoostedClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }

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
