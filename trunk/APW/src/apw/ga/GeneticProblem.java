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
package apw.ga;

import static apw.ga.PlotUtils.*;

/**
 * Minimal self-explanatory example of GeneticAlgorithm.
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class GeneticProblem {

    public static void main(String args[]) {
        // intermiediate result collector
        DefaultGACallback dc = new DefaultGACallback();

        // fitness function definition
        FitnessFunction ff = new FitnessFunction() {

            public double evalFitness(Object[] args) {
                Double x = (Double) args[0];
                Double y = (Double) args[1];
                double fit = 2 - x * x - y * y;
                return fit / 2;
            }
        };

        GeneticAlgorithm.builder().
                crossoverProb(0.9d).            // crossover probability
                mutationProb(0.01d).            // mutation probability
                populationSize(10).             // population size
                gene(2, true).                  // define 1st gene Gray coded...
                range(-1, 1).                   //  using 2 bits in range [-1,1]
                gene(2, true).                  // define 2nd gene
                range(-1, 1).                   //  in range [-1, 1]
                build().                        // End of GA definition
                setFitnessFunction(ff).         // use defined fitness function
                setFittestCallback(dc).         // collect intermiediate values
                evolve(40);                     // evolve 40 generations

        // Open frames presenting GA run
        plotFitnessFunction(ff, -1, 1);
        presentPlot(dc);
    }
}
