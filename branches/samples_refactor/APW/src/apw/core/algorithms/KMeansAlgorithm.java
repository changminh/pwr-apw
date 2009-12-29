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

import static apw.core.algorithms.DistanceMetrics.getEuclideanDistance;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;


/**
 *
 * @author Waldemar Szostak
 */
public final class KMeansAlgorithm {
	public static double[][] findClusterCentres(final Samples samples, final int noOfCentres) {
		if (noOfCentres < 2) {
			throw new RuntimeException("At least 2 centres should be looked for.");
		}
		if (samples.size() < noOfCentres) {
			throw new RuntimeException("There are too few samples to find " + noOfCentres
					+ " centroids; at least that many samples are needed.");
		}

		System.out.print("Looking for " + noOfCentres + " cluster centres: ");	

		final double[][] result = new double[noOfCentres][];

		final Centroid[] centroids = new Centroid[noOfCentres];
		int centroidId = 0;
		/* Get several samples at random to be the cluster centres for the start */
		final Set<Integer> randomSampleIds = getRandomSampleIds(samples, noOfCentres);
		for (final int sampleId : randomSampleIds) {
			centroids[centroidId++] = new Centroid(samples.get(sampleId));
		}
		
		Samples samplesRest = samples.copyStructure();
		for (int i = 0; i < samples.size(); ++i) {
			if (!randomSampleIds.contains(i)) {
				samplesRest.add(samples.get(i));
			}
		}
		

		boolean centroidsChanged;
		do {
			centroidsChanged = false;
			System.out.print(".");

			/* Assign each input vector to the nearest centroid */
			for (final Sample sample : samplesRest) {
				Centroid candidatingCentroid = centroids[0];
				double curMinDistance = candidatingCentroid.getDistance(sample);

				for (centroidId = 1; centroidId < centroids.length; centroidId++) {
					final double possibNewMinDistance = centroids[centroidId].getDistance(sample);
					if (possibNewMinDistance < curMinDistance) {
						curMinDistance = possibNewMinDistance;
						candidatingCentroid = centroids[centroidId];
					}
				}

				candidatingCentroid.samples.add(sample);
			}
			
			for (final Centroid centroid : centroids) {
//				System.out.println("centre: " + Arrays.toString(centroid.centre));
//				System.out.println("diversity: " + centroid.getCentreClassDiversity());
//				System.out.println();
				
				if (centroid.recalculateCentre()) {
					centroidsChanged = true;
				}
			}
			
			/* The centres have been recalculated and its member samples were deleted,
			 * so from now on we have to be able to select from the entire set of samples */
			samplesRest = samples;
		} while (centroidsChanged);
		
		for (int i = 0; i < noOfCentres; i++) {
			result[i] = centroids[i].centre;
		}

		System.out.println(" done.");

		return result;
	}

	private static Set<Integer> getRandomSampleIds(final Samples samples, final int noOfCentres) {
		final Set<Integer> result = new HashSet<Integer>();
		final int samplesCount = samples.size();

		do {
			result.add((int) (Math.random() * samplesCount));
		} while (result.size() != noOfCentres);
		
		System.out.println(result.toString());

		return result;
	}

	private static class Centroid {
		List<Sample> samples = new ArrayList<Sample>();
		double[] centre;


		public Centroid(final Sample sample) {
			samples.add(sample);
			this.centre = sample.toDoubleArray();
		}

		double getDistance(final Sample sample) {
			final double[] sampleDoubles = sample.toDoubleArray();
			return getEuclideanDistance(centre, sampleDoubles);
		}

		public boolean recalculateCentre() {
			final double[] newCentre = new double[centre.length];
			boolean centreChanged = false;

			for (int i = 0; i < newCentre.length; i++) {
				for (final Sample sample : samples) {
					double[] t = sample.toDoubleArray();
					newCentre[i] += t[i];
				}

				newCentre[i] /= samples.size();
				centreChanged = centreChanged || newCentre[i] != centre[i];
			}

//			System.out.println(Arrays.toString(centre) + " -> " + Arrays.toString(newCentre));

			centre = newCentre;
			samples = new ArrayList<Sample>();

			return centreChanged;
		}
		
		public Map<Object, Integer> getCentreClassDiversity() {
			final Map<Object, Integer> result = new HashMap<Object, Integer>();
			
			for (final Sample s : samples) {
				final Object sampleClass = s.classAttributeInt();
				Integer noOfSamplesPerClass = result.get(sampleClass);
				if (noOfSamplesPerClass == null) {
					noOfSamplesPerClass = 0;
				}
				result.put(sampleClass, noOfSamplesPerClass+1);
			}
			
			
			return result;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Samples samples = new ARFFLoader(new File("/home/wsz/dev/workspace/apw/data/iris.arff")).getSamples();
		double[][] centres = findClusterCentres(samples, 150);

	}
}