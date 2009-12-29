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
import apw.core.Evaluator;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import apw.gui.ResultPanel;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author Waldemar Szostak < wszostak@wp.pl >
 */
public class BaggedClassifier extends Classifier {
    final static Class[] CLASSIFIER_CONSTRUCTOR_PARAMETERS = new Class[]{Samples.class};
    
    private Class<Classifier>[] baseClassifiersClasses;
    private Classifier[] complexClassifier;
    private Samples samples;

    private boolean rebuildNeeded;


    public BaggedClassifier(final Class<Classifier>[] baseClassifiersClasses, final Samples samples) {
        super(samples);

        if (samples.size() == 0) {
            throw new RuntimeException("The samples object cannot be empty");
        }

        this.baseClassifiersClasses = baseClassifiersClasses;
        this.samples = samples;
        this.rebuildNeeded = true;
    }

    @Override
    public double[] classifySample(Sample sample) {
        if (rebuildNeeded) {
            throw new IllegalStateException("The classifier has been modified " +
                    "(new sample/-s have been added) and has to be rebuilt");
        }

        final WeightedDecisions decisions = new WeightedDecisions();
        for (int classifierIndex = 0; classifierIndex < complexClassifier.length; ++classifierIndex) {
            final double[] decision = complexClassifier[classifierIndex].classifySample(sample);
            decisions.addDecision(decision);
        }

        return decisions.getFinalDecision();
    }
    private class WeightedDecisions {
        private List<WeightedDecision> decisions = new ArrayList<WeightedDecision>();


        public void addDecision(final double[] decision) {
            if (!updateExistingDecisionWeight(decision)) {
                decisions.add(new WeightedDecision(decision));
            }
        }

        private boolean updateExistingDecisionWeight(final double[] decisionToFind) {
            for (final WeightedDecision weightedDecision : decisions) {
                if (weightedDecision.equals(decisionToFind)) {
                    ++weightedDecision.votesCount;
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
            int votesCount = 1;


            private WeightedDecision(double[] decision) {
                this.decision = decision;
            }

            public boolean equals(final double[] decisionToFind) {
	            return Arrays.equals(decision, decisionToFind);
            }

            public int compareTo(WeightedDecision other) {
                return other.votesCount - this.votesCount;
            }
        }
    }


    @Override
    public void rebuild() {
        try {
            for (int classifierIndex = 0; classifierIndex < baseClassifiersClasses.length; ++classifierIndex) {
                /* Create an instance of the current base classifier
                 * by calling its constructor with the modified sample set */
                final Classifier classifierInstance = getClassifierInstance(classifierIndex);

                /* Rebuild the base classifier to train it over the modified samples set */
                classifierInstance.rebuild();

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

    private Classifier getClassifierInstance(int baseClassifierIndex) throws NoSuchMethodException,
                                                                             InvocationTargetException,
                                                                             IllegalAccessException,
                                                                             SecurityException,
                                                                             IllegalArgumentException,
                                                                             InstantiationException {
        /* Build samples set taking into account their probability distribution */
        final Samples samplesWithProbability = getSamplesWithProbability();

        /* Create an instance of the current base classifier
         * by calling its constructor with the modified sample set */
        return (Classifier) baseClassifiersClasses[baseClassifierIndex].getConstructor(CLASSIFIER_CONSTRUCTOR_PARAMETERS).newInstance(samplesWithProbability);
    }

    private Samples getSamplesWithProbability() {

        final Samples result = new Samples(samples.getAtts());

        final int samplesCount = samples.size();
        for (int i = 0; i < samplesCount; i++) {
            result.add(samples.get((int) (Math.random() * samplesCount)));
        }

        return result;
    }

    @Override
    public Classifier copy() {
        final Object[] baseClasifiersCopy = new Object[baseClassifiersClasses.length];
        System.arraycopy(baseClassifiersClasses, 0, baseClasifiersCopy, 0, baseClassifiersClasses.length);

        final BaggedClassifier result = new BaggedClassifier((Class<Classifier>[]) baseClasifiersCopy, samples);
        result.rebuild();

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

	public static void main(String[] args) throws Exception {
		File f = null;
		if (args.length > 0 && !args[0].isEmpty()) {
			f = new File(args[0]);
		} else {
			// Ta sekcja jest po to, żeby móc wybrać łatwo zbiór testowy
			JFileChooser jf = new JFileChooser("data/");
			jf.showOpenDialog(null);
			f = jf.getSelectedFile();
		}

        if (f != null) {
            Samples samples = new ARFFLoader(f).getSamples();

            BoostedClassifier boostedClassifier = new BoostedClassifier(
					new Class[] {}, samples);

			boostedClassifier.rebuild();

            // Evaluator to obiekt, który liczy miary jakości klasyfikacji
            Evaluator e = new Evaluator(boostedClassifier, samples);

            // To metoda statyczna prezentująca miary jakości w oknie Swing
            ResultPanel.showResultFrame(e);
        }
    }
}
