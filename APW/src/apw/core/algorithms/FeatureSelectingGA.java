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
package apw.core.algorithms;

import apw.classifiers.Classifier;
import apw.core.Attribute;
import apw.core.Sample;
import apw.core.Samples;
import apw.ga.FitnessFunction;
import apw.ga.GeneticAlgorithm;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Waldemar Szostak < wszostak@wp.pl >
 */
public class FeatureSelectingGA {
	private final static Class[] CLASSIFIER_CONSTRUCTOR_PARAMETERS = new Class[]{Samples.class};

	private final static float CROSSOVER_PROBABILITY = 0.9f;
	private final static float MUTATION_PROBABILITY = 0.1f;
	private final static int POPULATION_SIZE = 50;
	private final static int GENERATION_COUNT = 50;

	private Classifier evaluatorClassifierClass;
	private Samples samples;
	private FitnessFunction fitnessFunction;
	private final boolean[] FEATURES;
	private final int FEATURES_COUNT;
	private final int CLASS_ATTR_INDEX;


	private FeatureSelectingGA(final Class<Classifier> evaluatorClassifierClass, final Samples samples)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		this.FEATURES = samples.getSelected();
		this.FEATURES_COUNT = FEATURES.length - 1;
		this.CLASS_ATTR_INDEX = samples.getClassAttributeIndex();

		this.evaluatorClassifierClass = evaluatorClassifierClass.getConstructor(CLASSIFIER_CONSTRUCTOR_PARAMETERS).newInstance(samples);
		this.samples = samples;

		final int noOfConditionalAtts = samples.getAtts().size()-1;	// = the number of features
		if (noOfConditionalAtts > 32) {
			throw new RuntimeException("Samples with more than 32 attributes are not supported");
		}

		this.fitnessFunction = getFitnessFunction();

		
        GeneticAlgorithm.
                defineGenotype().integer(noOfConditionalAtts, true).endDefinition().
                fitnessFunction(fitnessFunction).
                crossoverProb(CROSSOVER_PROBABILITY).
                mutationProb(MUTATION_PROBABILITY).
                populationSize(POPULATION_SIZE).
                evolve(GENERATION_COUNT);
	}

	private FitnessFunction getFitnessFunction() {
		return new FitnessFunction() {
			public double evalFitness(Object[] args) {
				final int chromosome = (Integer) args[0];

				if (chromosome == 0) {	// can't be no features
					return 0;
				}

				int noOfBitsOn = 0;
				int floatingBit = 1;
				int featureIndex = 0;

				for (int i = 0; i < FEATURES_COUNT; ++i) {
					if (featureIndex == CLASS_ATTR_INDEX) {
						++featureIndex;	// skip the class attribute
					}

					if ((chromosome & floatingBit) == floatingBit) {
						++noOfBitsOn;
						FEATURES[featureIndex] = true;
					} else {
						FEATURES[featureIndex] = false;
					}

					floatingBit <<= 1;
					++featureIndex;
				}

				evaluatorClassifierClass.rebuild();
				final double classificationError = getClassificationErrorRate();

				return  2 - ((double) noOfBitsOn) / FEATURES_COUNT - classificationError;
			}
		};
	}

	private double getClassificationErrorRate() {
        int classifiedIncorrectly = 0;

        for (final Sample sample : samples) {
            final double targetValue = (Double) sample.classAttributeRepr();
            final double calculatedValue = (Double) samples.getClassAttribute()
					.getRepresentation(evaluatorClassifierClass.classifySampleAsObject(sample));

            /* Compare the expected classification class with the calculated one */
            if (targetValue != calculatedValue) {
                ++classifiedIncorrectly;
            }
        }

		return ((double) classifiedIncorrectly) / samples.size();
	}

	public static ArrayList<Attribute> getAttributes(final Class<Classifier> evaluatorClassifierClass, final Samples samples)
		throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		final FeatureSelectingGA selectorInstance = new FeatureSelectingGA(evaluatorClassifierClass, samples);
		final ArrayList<Attribute> result = selectorInstance.samples.getSelectedAtts();	// all attributes selected and not the class attribute

		selectorInstance.resetAttsSelectedStatus();

		return result;
	}

	private void resetAttsSelectedStatus() {
		final boolean[] newSelected = new boolean[samples.getAtts().size()];
		Arrays.fill(newSelected, true);
		samples.setSelected(newSelected);
	}
}