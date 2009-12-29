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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import apw.classifiers.Classifier;
import apw.core.Attribute;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import apw.core.util.PropertiesManager;
import apw.ga.FitnessFunction;
import apw.ga.GeneticAlgorithm;

/**
 *
 * @author Waldemar Szostak
 */
public class FeatureSelectingGA {
	private final static Class[] CLASSIFIER_CONSTRUCTOR_PARAMETERS = new Class[]{Samples.class};

	
	private static FitnessFunction getFitnessFunction(final Classifier evaluatorClassifierClass, final Samples samples, final float maxErrorRate) {
		return new FitnessFunction() {
			/** The so-far best classfication rate */
			private double maxCorrectClassificationRate = Double.MIN_VALUE;

			public double evalFitness(Object[] args) {
				final int chromosome = (Integer) args[0];
				/* At least one attribute has to be selected for the classification to be possible,
				 * so if none are selected, then return 0 as the fitness value */
				if (chromosome == 0) {
					return 0;
				}
				
				final double noOfBitsOn = updateSelectedFeatureArray(chromosome, samples);
				
				/* Force the new attribute set to take effect */
				samples.notifySamplesOfViewChange();

				evaluatorClassifierClass.rebuild();
				
				final double curCorrectClassificationRate = samples.getCorrectClassificationRate(evaluatorClassifierClass);

				/* If the current set of attributes ensures a better classification rate,
				 * then require at least that good a classification rate */
				if (curCorrectClassificationRate + maxErrorRate >= maxCorrectClassificationRate) {
					maxCorrectClassificationRate = curCorrectClassificationRate;

					return 2 - Math.pow(2, noOfBitsOn / samples.getSelected().length);
				} else {
					return 0;
				}

			}
		};
	}

	/** Process the information encoded in the chromosome,
	 * enabling or disabling the attributes depending on the value of the corresponding bit */
	private static int updateSelectedFeatureArray(final int chromosome, final Samples samples) {
		int noOfBitsOn = 0;
		int floatingBit = 1;
		int featureIndex = 0;
		
		for (int i = 1; i < samples.getSelected().length; ++i) {
			if (featureIndex == samples.getClassAttributeIndex()) {
				++featureIndex;	// skip the class attribute
			}

			if ((chromosome & floatingBit) == floatingBit) {
				++noOfBitsOn;
				samples.getSelected()[featureIndex] = true;
			} else {
				samples.getSelected()[featureIndex] = false;
			}

			floatingBit <<= 1;
			++featureIndex;
		}
		
		return noOfBitsOn;
	}
	
	public static List<Attribute> getSelectedAttributes(final Samples samples)
		throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		
		selectAttributes(samples);
		
		final List<Attribute> result = samples.getSelectedAtts();
		
		resetAttsSelectedStatus(samples);
		
		return result;
	}
	
	public static void selectAttributes(final Samples samples)
		throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		
		final int noOfConditionalAtts = samples.getAtts().size()-1;	// = the number of features
		if (noOfConditionalAtts > 32) {
			throw new RuntimeException("Samples with more than 32 attributes are not supported");
		}
		
		
		final Properties properties = PropertiesManager.getProperties("featureSelectingGA");

		final float crossoverProbability = Float.valueOf(properties.getProperty("crossoverProbability"));
		final float mutationProbability = Float.valueOf(properties.getProperty("mutationProbability"));
		final int populationSize = Integer.valueOf(properties.getProperty("populationSize"));
		final int generationCount = Integer.valueOf(properties.getProperty("generationCount"));
		final float maxErrorRate = Float.valueOf(properties.getProperty("maxErrorRate"));
		final Classifier evaluatorClassifier = getClassifierInstance(properties, samples);
		
		final int bitSet = (Integer) GeneticAlgorithm.
		        defineGenotype().integer(noOfConditionalAtts, true).endDefinition().
		        fitnessFunction(getFitnessFunction(evaluatorClassifier, samples, maxErrorRate)).
		        crossoverProb(crossoverProbability).
		        mutationProb(mutationProbability).
		        populationSize(populationSize).
		        evolve(generationCount)[0];

		updateSelectedFeatureArray(bitSet, samples);
	}

	private static Classifier getClassifierInstance(
			final Properties properties, final Samples samples)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		final Class<Classifier> classifierToUse;
		try {
			classifierToUse = (Class<Classifier>) FeatureSelectingGA.class.getClassLoader().loadClass(
					properties.getProperty("classifierToUse"));
		
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Classifier class could not be loaded ");
		}
		
		final Classifier evaluatorClassifier = classifierToUse.getConstructor(CLASSIFIER_CONSTRUCTOR_PARAMETERS).newInstance(samples);
		return evaluatorClassifier;
	}

	public static void main(String[] args) throws Exception {
		List<Attribute> l = FeatureSelectingGA.getSelectedAttributes(new ARFFLoader(new File("data/iris.arff")).getSamples());
		System.out.println(l.toString());
	}

	private static void resetAttsSelectedStatus(Samples samples) {
		final boolean[] newSelected = new boolean[samples.getAtts().size()];
		Arrays.fill(newSelected, true);
		samples.setSelected(newSelected);
	}
}