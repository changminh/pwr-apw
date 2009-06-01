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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
1.   [Start] Generate random population of n chromosomes (suitable solutions for the problem)
2. [Fitness] Evaluate the fitness f(x) of each chromosome x in the population
3. [New population] Create a new population by repeating following steps until the new population is complete
1. [Selection] Select two parent chromosomes from a population according to their fitness (the better fitness, the bigger chance to be selected)
2. [Crossover] With a crossover probability cross over the parents to form a new offspring (children). If no crossover was performed, offspring is an exact copy of parents.
3. [Mutation] With a mutation probability mutate new offspring at each locus (position in chromosome).
4. [Accepting] Place new offspring in a new population
4. [Replace] Use new generated population for a further run of algorithm
5. [Test] If the end condition is satisfied, stop, and return the best solution in current population
6. [Loop] Go to step 2


 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class FormerGeneticAlgorithm {

    int evolution;
    int maxEvolutions = 2;
    int chromosomes = 4;
    int length = 2;

    // temporary variables
    double[][] population;
    List<double[]> sortedByFitness;
    double minF, maxF;

//    public static void main(String args[]) {
//        FormerGeneticAlgorithm ga = new FormerGeneticAlgorithm();
//        ga.evolve();
//    }

    /**
     * Compares fitness values so that they can be sorted
     * in <em>descending</em> order.
     */
    private static class FitnessComparator implements Comparator<double[]> {

        public int compare(double[] o1, double[] o2) {
            return Double.compare(o2[o2.length - 1], o1[o1.length - 1]);
        }
    };
    private FitnessComparator fitnessComparator = new FitnessComparator();

    private void createSortedPopulation() {
        sortedByFitness = new ArrayList<double[]>();
        sortedByFitness.addAll(Arrays.asList(population));
        Collections.sort(sortedByFitness, fitnessComparator);
    }

    private double[][] crossOver(double[][] parents) {
        return parents;
    }

    private double fitness(double[] x) {

        // To jest chyba (!) paraboloida o środku w
        return 100 - (x[0] * x[0] + x[1] * x[1]);
    }

    private boolean isStopConditionSatisfied() {
        return evolution >= maxEvolutions;
    }

    public FormerGeneticAlgorithm() {
        init();

    }

    public void init() {
        // each chromosome has one double value more to
        // represent fitness
        population = new double[chromosomes][length + 1];
        for (int i = 0; i < chromosomes; i++)
            for (int j = 0; j < length; j++)
                population[i][j] = Math.random();
    }

    public void evolve() {
        updateFitnessArray();
        createSortedPopulation();

        
        double[][] parents = selectParentChromosomes();
        double[][] offspring = crossOver(parents);
        mutate(offspring);

    } /*
    1. [Start] Generate random population of n chromosomes (suitable solutions
    for the problem)
    2. [Fitness] Evaluate the fitness f(x) of each chromosome x in the population
    3. [New population] Create a new population by repeating following steps
    until the new population is complete
    1. [Selection] Select two parent chromosomes from a population according
    to their fitness (the better fitness, the bigger chance to be selected)
    2. [Crossover] With a crossover probability cross over the parents to
    form a new offspring (children). If no crossover was performed,
    offspring is an exact copy of parents.
    3. [Mutation] With a mutation probability mutate new offspring at each
    locus (position in chromosome).
    4. [Accepting] Place new offspring in a new population
    4. [Replace] Use new generated population for a further run of algorithm
    5. [Test] If the end condition is satisfied, stop, and return the best solution in current population
    6. [Loop] Go to step 2 */


    public FormerGeneticAlgorithm(Builder b) {
    }

    private void mutate(double[][] offspring) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Selects two parent chromosomes.
     * They are selected in order of precedence, that is by fitness;
     * @return an array of 2 parent chromosomes
     */
    private double[][] selectParentChromosomes() {
        double[][] p = new double[2][];
        SelectionPool sp = new SelectionPool(population);
        p[0] = sp.poll();
        p[1] = sp.poll();
        return p;
    }

    private void updateFitnessArray() {
        double fit = fitness(population[0]);
        minF = maxF = fit;

        for (int i = 1; i < chromosomes; i++) {
            fit = fitness(population[i]);
            population[i][length] = fit;
            if (fit > maxF)
                maxF = fit;
            if (fit < minF)
                minF = fit;
        }
    }

    public class Builder {

        private int i = 0;

        public Builder() {
        }

        public void i(int j) {
            i = j;
        }
    }
}
