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

package apw.art2.working;

import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import static apw.art2.ART2_Util.*;

/**
 *
 * @author nitric
 */
public class Network {
    private Attentional as;
    private Orienting os;
    private int dimension;
    private double a;
    private double b;
    private double c;
    private double d;
    private double e;
    private double alpha;
    private double rho;
    private double theta;
    private int learningIterations = -1;
    private double init;
    private double[] input;
    private boolean reset = true;
    private boolean learningMode = true;
    private boolean debug = false;
    private Samples samples;

    public Network(int dim, double a, double b, double c, double d, double e, 
                    double alpha, double rho, double theta, int learningIterations) {
        this.dimension = dim;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.alpha = alpha;
        this.rho = rho;
        this.theta = theta;
        this.learningIterations = learningIterations;
        input = new double[dimension];
        init = 0.5d / ((1.d - d) * Math.sqrt(dimension));
        System.out.println("Init bottomUp: " + init);
        System.out.println("learningIterations = " + learningIterations);
        as = new Attentional();
        os = new Orienting();
    }

    public int process(double[] vector) {
        reset = true;
        as.prepare();
        for (int i = 0; i < dimension; i++)
            input[i] = vector[i];
        as.processU();
        as.processW();
        as.processP();
        as.processX();
        as.processQ();
        as.processV();
        // Second time:
        as.processU();
        as.processW();
        as.processP();
        as.processX();
        as.processQ();
        as.processV();
        while (reset) {
            as.tournament();
            os.vigilanceTest();
            if (reset) {
                as.getWinner().disable();
            }
            else {
                as.processW();
                as.processX();
                as.processQ();
                as.processV();
            }
        }
        as.getWinner().updateWeights();
        as.processU();
        as.processW();
        as.processP();
        as.processX();
        as.processQ();
        as.processV();
        return as.winner;
    }

    class Attentional {
        private double[] w = new double[dimension];
        private double[] x = new double[dimension];
        private double[] u = new double[dimension];
        private double[] v = new double[dimension];
        private double[] p = new double[dimension];
        private double[] q = new double[dimension];
        ArrayList<Neuron> f2 = new ArrayList<Neuron>();
        private int winner = -1;

        public Attentional() {
            f2.add(new Neuron());
        }

        private void prepare() {
            // Reset all sublayers in F1:
            for (double[] layer : new double[][] {w, x, u, v, p, q})
                for (int i = 0; i < dimension; i++)
                    layer[i] = 0.d;
            // Reset all neurons in F2:
            winner = -1;
            for (Neuron n : f2)
                n.reset();
        }

        private void processU() {
            double denominator = e + vectorLength(v);
            if (denominator == 0.d)
                for (int i = 0; i < dimension; i++)
                    u[i] = 0.d;
            else
                for (int i = 0; i < dimension; i++)
                    u[i] = v[i] / denominator;
            if (debug)
                System.out.println("U = " + print(u));
        }

        private void processW() {
            for (int i = 0; i < dimension; i++)
                w[i] = input[i] + a * u[i];
            if (debug)
                System.out.println("W = " + print(w));
        }

        private void processP() {
            if (as.winner != -1)
                for (int i = 0; i < dimension; i++)
                    p[i] = u[i] + d * as.getWinner().topDown[i];
            else
                for (int i = 0; i < dimension; i++)
                    p[i] = u[i];
            if (debug)
                System.out.println("P = " + print(p));
        }

        private void processX() {
            double denominator = e + vectorLength(w);
            if (denominator == 0.d)
                for (int i = 0; i < dimension; i++)
                    x[i] = 0.d;
            else
                for (int i = 0; i < dimension; i++)
                    x[i] = w[i] / denominator;
            if (debug)
                System.out.println("X = " + print(x));
        }

        private void processQ() {
            double denominator = e + vectorLength(p);
            if (denominator == 0.d)
                for (int i = 0; i < dimension; i++)
                    q[i] = 0.d;
            else
                for (int i = 0; i < dimension; i++)
                    q[i] = p[i] / denominator;
            if (debug)
                System.out.println("Q = " + print(q));
        }

        private void processV() {
            for (int i = 0; i < dimension; i++)
                v[i] = f(x[i]) + b * f(q[i]);
            if (debug)
                System.out.println("V = " + print(v));
        }

        private double f(double d) {
            return d > theta ? d : 0.d;
        }

        private Neuron getWinner() {
            return winner != -1 ? f2.get(winner) : null;
        }

        private int tournament() {
            winner = -1;
            double max = 0.d;
            double temp;
            for (int i = 0; i < f2.size(); i++) {
                if ((temp = f2.get(i).calculateOutput()) > max) {
                    winner = i;
                    max = temp;
                }
            }
            /* if (winner == -1) {
                Neuron n = new Neuron();
                f2.add(n);
                temp = n.calculateOutput();
                if (temp > max)
                    winner = f2.size() - 1;
            } */
            if (debug)
                System.out.println("Tournament won by " + winner);
            return winner;
        }
    }

    class Orienting {
        double[] r = new double[dimension];
        
        private void processR() {
            double denominator = e + vectorLength(as.u) + c * vectorLength(as.p);
            for (int i = 0; i < dimension; i++)
                r[i] = (as.u[i] + c * as.p[i]) / denominator;
            if (debug)
                System.out.println("R = " + print(r) + ", length = " + vectorLength(r));
            reset = vectorLength(r) < rho - e;
            if (debug)
                System.out.println("reset = " + reset);
        }

        private void vigilanceTest() {
            as.processU();
            as.processP();
            processR();
        }
    }

    class Neuron {
        private double[] bottomUp = new double[dimension];
        private double[] topDown = new double[dimension];
        private double output;
        private boolean enabled = true;

        Neuron() {
            for (int i = 0; i < dimension; i++)
                bottomUp[i] = init;
        }

        private double calculateOutput() {
            output = 0.d;
            for (int i = 0; i < dimension; i++)
                output += bottomUp[i] * as.p[i];
            if (debug)
            System.out.println("output = " + output);
            return getOutput();
        }

        public void updateWeights() {
            if (!learningMode)
                return;
            /* Fast learning:
                for (int i = 0; i < dimension; i++)
                    bottomUp[i] = topDown[i] = (1 / (1 - d)) * as.u[i]; */
            for (int iter = 0; iter < learningIterations; iter++) {
                for (int i = 0; i < dimension; i++) {
                    bottomUp[i] = alpha * d * as.u[i] + (1 + alpha * d * (d - 1)) * bottomUp[i];
                    topDown[i] = alpha * d * as.u[i] + (1 + alpha * d * (d - 1)) * topDown[i];
                }
                as.processU();
                as.processW();
                as.processP();
                as.processX();
                as.processQ();
                as.processV();
            }
            if (as.winner == as.f2.size() - 1)
                as.f2.add(new Neuron());    // Add new neuron:
            if (debug) {
                System.out.println("bottomUp = " + print(bottomUp));
                System.out.println("topDown = " + print(topDown));
            }
        }

        public double getOutput() {
            return enabled ? output : 0.d;
        }

        public void reset() {
            output = 0.d;
            enabled = true;
        }

        public void disable() {
            enabled = false;
        }
    }

    public int getPrototypesCount() {
        return as.f2.size();
    }

    public void learningMode(boolean b) {
        learningMode = b;
    }

    public Samples getSamples() {
        return samples;
    }

    public void setSamples(Samples samples) {
        this.samples = samples;
    }

    public static void main(String[] args)  throws Exception {
        Network n = new Network(2, 10, 10, 0.1, 0.9, 0, 0.6, 0.9, 0.01d, 100);
        Samples samples = new ARFFLoader(new File("data/moon.arff")).getSamples();
        ArrayList<double[]> inst = new ArrayList<double[]>();
        // n.process(new double[] {0.2, 0.7, 0.1, 0.5, 0.4});
        double[] temp;
        for (Sample s : samples) {
            temp = new double[2];
            for (int i = 0; i < 2; i++)
                temp[i] = (Double) s.get(i);
            inst.add(temp);
        }
        HashMap<String, Integer> results = new HashMap<String, Integer>();
        String s;
        for (int i = 0; i < inst.size(); i++) {
            s = samples.get(i).get(samples.getClassAttributeIndex()) + "_" + n.process(inst.get(i));
            if (results.containsKey(s)) {
                results.put(s, results.get(s) + 1);
            }
            else
                results.put(s, 1);
        }
        for (String str : results.keySet()) {
            System.out.println(str + " --> " + results.get(str));
        }
        System.out.println("");
    }
}
