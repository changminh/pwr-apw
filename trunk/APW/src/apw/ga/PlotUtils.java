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

import java.util.Collection;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;
import static org.math.array.DoubleArray.*;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class PlotUtils {

    public static final void presentPlot(Collection<Double> doubleCollection, String s) {
        Plot2DPanel plot = new Plot2DPanel();

        // define the legend position
        plot.addLegend("SOUTH");

        // add a line plot to the PlotPanel
        plot.addLinePlot(s,
                convert(doubleCollection.toArray(new Double[]{})));

        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame(s);
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static final void presentPlot(DefaultGACallback fc) {
        Plot2DPanel plot = new Plot2DPanel();

        // define the legend position
        plot.addLegend("SOUTH");

        // add a line plot to the PlotPanel
        plot.addLinePlot("Fittest",
                convert(fc.fittest.toArray(new Double[]{})));
        plot.addLinePlot("Mean fit",
                convert(fc.meanFit.toArray(new Double[]{})));

        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("GA run details");
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    // grid version of the function
    public static double[][] f1(FitnessFunction ff, double[] x, double[] y) {
        double[][] z = new double[y.length][x.length];
        for (int i = 0; i < x.length; i++)
            for (int j = 0; j < y.length; j++)
                z[j][i] = ff.evalFitness(new Object[]{
                            x[i], y[j]
                        });
        return z;
    }

    public static double[] convert(Double[] c) {
        double[] d = new double[c.length];
        for (int i = 0; i < d.length; i++)
            d[i] = c[i];
        return d;
    }

    public static void plotFitnessFunction(FitnessFunction ff, double min, double max) {
        double[] x = increment(min, 0.1, max); // x = 0.0:0.1:1.0
        double[] y = increment(min, 0.1, max);// y = 0.0:0.05:1.0
        double[][] z1 = PlotUtils.f1(ff, x, y);


        // create your PlotPanel (you can use it as a JPanel) with a legend at SOUTH
        Plot3DPanel plot = new Plot3DPanel("SOUTH");

        // add grid plot to the PlotPanel
        plot.addGridPlot("Fitness function", x, y, z1);
        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);
    }
}
