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
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class GeneticProblem {

    static ArrayList<Double> fitts = new ArrayList();

    public static void main(String args[]) {
        GeneticAlgorithm ga = GeneticAlgorithm.builder().
                crossoverProb(0.4d).
                mutationProb(0.01d).
                populationSize(10).
                gene(20, true).
                range(-1, 1).
                gene(20, true).
                range(-1, 1).
                build();

        ga.setFitnessFunction(new FitnessFunction() {

            public double evalFitness(Object[] args) {
                Double x = (Double) args[0], y = (Double) args[1];
                double fit = 2 - x * x - y * y;
                return fit / 2;
            }
        });
        ga.setFittestCallback(new GeneticAlgorithm.FittestCallback() {

            public void fittest(double f) {
                fitts.add(f);
            }
        });
        ga.evolve(40);
        //System.exit(0);
        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();

        // define the legend position
        plot.addLegend("SOUTH");

        // add a line plot to the PlotPanel
        plot.addLinePlot("Fittest value", convert(fitts.toArray(new Double[]{})));

        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public static double[] convert(Double[] c) {
        double[] d = new double[c.length];
        for (int i = 0; i < d.length; i++)
            d[i] = c[i];
        return d;
    }
}
