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
 *     disclaimer  in  the  documentation and / or other materials
 *     provided with the distribution.
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
package apw.core;

import apw.classifiers.Classifier;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class Evaluator {

    private int N;
    private Classifier classifier;
    private Samples samples;

    public Classifier getClassifier() {
        return classifier;
    }

    public Samples getSamples() {
        return samples;
    }

    // Experimental
    public class Measure {

        public double ratio;
        public int value;
    }

    public class Instance {

        public int positive;
        public int negative;
    }
    public Measures[] classes;
    public Measures weighted;
    public int confMtx[][];

    public class Measures {

        public Instance correct;
        public Instance incorrect;
        public String className;
        public double TP;
        public double FP;
        public double TN;
        public double FN;
        public double accuracy;
        public double recall;
        public double precision;
        public double fScore;
        public int instances;

        public Measures() {
            correct = new Instance();
            incorrect = new Instance();
        }
    }

    public Evaluator(Classifier c, Samples ss) {
        this(getConfusionMatrix(c, ss), getNames(ss), c, ss);
    }

    private static String[] getNames(Samples ss) {
        if (!(ss.getClassAttribute() instanceof Nominal))
            throw new IllegalArgumentException(
                    "Evaluator can only be used with nominal class values");
        return ((Nominal) ss.getClassAttribute()).getSortedIKeys();
    }

    private static int indexOf(Object[] arr, Object o) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i].equals(o))
                return i;
        return -1;
    }

    private static int[][] getConfusionMatrix(Classifier c, Samples ss) {
        if (!(ss.getClassAttribute() instanceof Nominal))
            throw new IllegalArgumentException(
                    "Evaluator can only be used with nominal class values");
        int n = ((Nominal) ss.getClassAttribute()).getKeys().
                size();
        int[][] cm = new int[n][n];

        // fill cm
        for (Sample s : ss) {
            int i = indexOf(((Nominal) ss.getClassAttribute()).getSortedIKeys(), s.
                    classAttributeInt());
            int j = indexOf(((Nominal) ss.getClassAttribute()).getSortedIKeys(), c.
                    classifySampleAsObject(s));
//            	argmax(c.classifySample(s));

            /** wiersz i-ty,       kolumna j-ta */
            /** i-ty - oczekiwany, j-uzyskany   */
            cm[i][j]++;
        }
        return cm;
    }
    public int instances;

    public Evaluator(int[][] confusionMatrix) {
        this(confusionMatrix, new String[confusionMatrix.length]);
    }

    private static int argmax(double[] d) {
        int m = 0;
        for (int i = 0; i < d.length; i++)
            if (d[i] > d[m])
                m = i;
        return m;
    }

    public Evaluator(int[][] confusionMatrix,
            String[] classNames) {
        this(confusionMatrix, classNames, null, null);
    }

    private Evaluator(int[][] confusionMatrix,
            String[] classNames,
            Classifier c,
            Samples s) {
        this.samples = s;
        this.classifier = c;

        // Assertions
        if (confusionMatrix == null || classNames == null)
            throw new NullPointerException();
        if (confusionMatrix.length != confusionMatrix[0].length)
            throw new IllegalArgumentException(
                    "Confusion Matrix must be a square matrix");
        if (classNames.length != confusionMatrix.length)
            throw new IllegalArgumentException(
                    "For each row in Confusion Matrix there must " +
                    "exist a name in classNames array");

        // Initialization
        N = confusionMatrix.length;
        confMtx = confusionMatrix;
        classes = new Measures[N];
        weighted = new Measures();
        for (int i = 0; i < N; i++)
            classes[i] = new Measures();

        // count instances
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                instances += confusionMatrix[i][j];

        // count class distribution
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                classes[i].instances += confusionMatrix[i][j];

        // name classes
        for (int i = 0; i < N; i++)
            classes[i].className = classNames[i];

        // count number of correct predictions that an instance is positive
        for (int i = 0; i < N; i++)
            classes[i].correct.positive = confusionMatrix[i][i];

        // number of incorrect predictions that an instance is Positive % Negative
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (i != j) { // exclude correct Positives
                    classes[i].incorrect.positive +=
                            confusionMatrix[j][i];
                    classes[i].incorrect.negative +=
                            confusionMatrix[i][j];
                }

        // evaluate number of correct predictions that an instance is negative
        Measures m;
        for (int i = 0; i < N; i++) {
            m = classes[i];
            m.correct.negative = instances -
                    (m.correct.positive +
                    m.incorrect.negative +
                    m.incorrect.positive);
        }

        // count True(& False) Positive (& Negative) ratios
        double positiveCases, negativeCases, den;
        for (int i = 0; i < N; i++) {
            m = classes[i];
            positiveCases = m.correct.positive + m.incorrect.negative;
            negativeCases = m.correct.negative + m.incorrect.positive;
            // (TP) is the proportion of positive cases
            // that were correctly identified
            // TPR = TP / P = TP / (TP + FN)
            m.TP = m.correct.positive / positiveCases;

            // (FP) is the proportion of negatives cases
            // that were incorrectly classified as positive
            // FPR = FP / N = FP / (FP + TN)
            m.FP = m.incorrect.positive / negativeCases;

            // (TN) is defined as the proportion of negatives
            // cases that were classified correctly
            m.TN = m.correct.negative / negativeCases;

            // (FN) is the proportion of positives cases
            // that were incorrectly classified as negative
            m.FN = m.incorrect.negative / positiveCases;

            // recall is the same as true positive ratio
            m.recall = m.TP;

            // precision (P) is the proportion of the predicted positive
            // cases that were correct
            den = m.incorrect.positive + m.correct.positive;
            if (den == 0)
                m.precision = 0;
            else
                m.precision = (double) m.correct.positive /
                        den;

            // f Score
            den = m.precision + m.recall;
            if (den == 0)
                m.fScore = 0;
            else
                m.fScore = 2 * (m.precision * m.recall) / (den);

            m.accuracy = (double) m.correct.positive / m.instances;
        }

        // Count weighted values
        for (int i = 0; i < N; i++) {
            m = classes[i];
            if (m.instances > 0) {
                weighted.TP += m.TP * m.instances;
                weighted.FP += m.FP * m.instances;
                weighted.TN += m.TN * m.instances;
                weighted.FN += m.FN * m.instances;
                weighted.accuracy += m.accuracy * m.instances;
                weighted.recall += m.recall * m.instances;
                weighted.precision += m.precision * m.instances;
                weighted.fScore += m.fScore * m.instances;
            }
        }
        weighted.TP /= instances;
        weighted.FP /= instances;
        weighted.TN /= instances;
        weighted.FN /= instances;
        weighted.accuracy /= instances;
        weighted.recall /= instances;
        weighted.precision /= instances;
        weighted.fScore /= instances;
    }
}
