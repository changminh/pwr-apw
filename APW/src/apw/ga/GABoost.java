/*
 *  Copyright (c) 2009, Wrocław University of Technology
 *  All rights reserved.
 *  Redistribution  and use in source  and binary  forms,  with or
 *  without modification,  are permitted provided that the follow-
 *  ing conditions are met:
 * grg
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
package apw.ga;

import apw.classifiers.Classifier;
import apw.core.Sample;
import apw.core.Samples;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class GABoost extends Classifier {

    GeneticAlgorithm ga;
    int weightsNo;

    public GABoost(Samples s) {
        super(s);
        defineGA(s);
    }

    @Override
    public double[] classifySample(Sample s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addSamples(Samples s) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addSample(Sample s) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rebuild() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Classifier copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void defineGA(Samples s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Calulates a fraction number of correct classified examples by
     * this classifier
     * @return correct rate
     */
    private double getCorrectRatio() {
        throw new UnsupportedOperationException();
    }

    /**
     * Extract the weight table from GA representation.
     * @return
     */
    private double [] getWeights() {
        throw new UnsupportedOperationException();
    }
}
