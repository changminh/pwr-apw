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

import apw.core.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static apw.core.algorithms.DistanceMetrics.getEuclideanDistance;


/**
 *
 * @author Waldemar Szostak < wszostak@wp.pl >
 */
public final class KMeansAlgorithm {
	public static double[][] findClusterCentres(final Samples samples, final int noOfCentres) {
		System.out.print("Looking for " + noOfCentres + " cluster centres: ");
		final Centroid[] centroids = new Centroid[noOfCentres];

		int centroidId = 0;
		for (final Integer sampleId : getRandomSampleIds(samples, noOfCentres)) {
			centroids[centroidId++] = new Centroid(samples.get(sampleId).toDoubleArray());
		}

		boolean centroidsChanged;
		do {
			centroidsChanged = false;
			System.out.print(".");

			/* Assign each input vector to the nearest centroid */
			for (final Sample sample : samples) {
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
				if (centroid.recalculateCentre()) {
					centroidsChanged = true;
				}
			}
		} while (centroidsChanged);

		final double[][] result = new double[noOfCentres][];
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

		return result;
	}

	private static class Centroid {
		List<Sample> samples = new ArrayList<Sample>();
		double[] centre;


		public Centroid(final double[] centre) {
			this.centre = centre;
		}

		public double getDistance(final Sample sample) {
			return getEuclideanDistance(centre, sample.toDoubleArray());
		}

		public boolean recalculateCentre() {
			final double[] newCentre = new double[centre.length];
			boolean centreChanged = false;

			for (int i = 0; i < newCentre.length; i++) {
				newCentre[i]  = 0;

				for (final Sample sample : samples) {
					newCentre[i] += sample.toDoubleArray()[i];
				}

				newCentre[i] /= samples.size();
				centreChanged = centreChanged || newCentre[i] != centre[i];
			}

//			System.out.println(Arrays.toString(centre) + " -> " + Arrays.toString(newCentre));

			centre = newCentre;
			samples = new ArrayList<Sample>();

			return centreChanged;
		}
	}
}