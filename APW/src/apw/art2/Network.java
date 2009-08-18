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

package apw.art2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author nitric
 */
public class Network {
    private int dimension;
    private int cycleCounter = 1;
    private boolean reset = false;
    private boolean resonance = false;
    private int winnerIndex;
    private static int neuronCounter = 0;
    private double initialBottomUpLimit;
    private double a, b, c, d, e, rho, theta;
    private double[] actualInput;
    private double updateDenominator;
    private AttentionalSubsystem attentional = new AttentionalSubsystem();
    private OrientingSubsystem orienting = new OrientingSubsystem();
    private HashSet<Integer> banned = new HashSet<Integer>();

    private Random rand = new Random();

    public Network(int dimension, double a, double b, double c, double d, double e, 
                   double rho, double theta) {
        this.dimension = dimension;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.rho = rho;
        this.theta = theta;
        checkParameters();
        initialize();
    }

    public void process(double[] input) {
        winnerIndex = -1;
        resetOutput();
        actualInput = input;
        outer:
        while(!resonance) {
            attentional.processF1();                    // Needs to be repeated
            attentional.processF1();
            orienting.process();
            inner:
            while(!resonance) {
                vigilanceTest();
                if (reset) {
                    cycleCounter = 1;
                    for (Neuron n : attentional.y)
                        if (n.output > 0)
                            banned.add(n.id);
                    continue outer;
                }
                // As we are here, it means that there is no reset signal
                if (cycleCounter == 1) {
                    cycleCounter++;
                    // step 11
                    int counter = 0;
                    double maxNet = Double.NEGATIVE_INFINITY;
                    double temp;
                    for (Neuron n : attentional.y) {
                        temp = n.process();
                        if (temp > maxNet && !banned.contains(n.id)) {
                            maxNet = temp;
                            winnerIndex = counter;
                        }
                        counter++;
                    }
                    for (Neuron n : attentional.y)
                        n.setWinner(winnerIndex);
                    attentional.updatePQ();
                    attentional.processF1();
                    continue inner;
                }
                else {
                    resonance = true;
                    attentional.y.get(winnerIndex).updateWeights();
                }
            }
        }
    }

    public static double vectorLength(double[] vector) {
        double sum = 0.d;
        for (double dd : vector)
            sum += dd * dd;
        return Math.sqrt(sum);
    }

    public static double[] scaleVector(double[] vector, double scale) {
        double[] result = new double[vector.length];
        for (int i = 0; i < vector.length; i++)
            result[i] = vector[i] * scale;
        return result;
    }

    private void checkParameters() {
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    private void initialize() {
        // Calculate bootom-up initial weights limit:
        initialBottomUpLimit = (0.5d / ((1 - d) * Math.sqrt(dimension)));
        for (int i = 0; i < 5; i++)
            attentional.y.add(new Neuron(neuronCounter++));
        updateDenominator = 1.d - d;
    }

    private double f(double param) {
        return param > theta ? param : 0.d;
    }

    private boolean vigilanceTest() {
        return (reset = rho > e + vectorLength(orienting.r));
    }

    private void resetOutput() {
        attentional.clearOutput();
        orienting.clearOutput();
    }

    class AttentionalSubsystem {

        private double[] w, x, v, u, p, q;                      // 6 sublayers of F1 layer
        private ArrayList<Neuron> y = new ArrayList<Neuron>();  // F2 layer

        public AttentionalSubsystem() {
            clearOutput();
        }

        public void processF1() {
            processW();
            processX();
            processV();
            processU();
            processP();
            processQ();
        }

        private void clearOutput() {
            w = new double[dimension];
            x = new double[dimension];
            v = new double[dimension];
            u = new double[dimension];
            p = new double[dimension];
            q = new double[dimension];
            // Reseting output of neurons:
            for (Neuron n: y)
                n.setWinner(-1);
        }

        private void updatePQ() {
            processP();
            processQ();
        }

        private void processW() {
            for (int i = 0; i < dimension; i++) {
                attentional.w[i] = actualInput[i] + a * attentional.u[i];
            }
        }

        private void processX() {
            double denominator = e + vectorLength(attentional.w);
            for (int i = 0; i < dimension; i++) {
                attentional.x[i] = attentional.w[i] / denominator;
            }
        }

        private void processV() {
            for (int i = 0; i < dimension; i++) {
                attentional.v[i] = f(attentional.x[i]) + b * f(attentional.q[i]);
            }
        }

        private void processU() {
            double denominator = e + vectorLength(attentional.v);
            for (int i = 0; i < dimension; i++)
                attentional.u[i] = attentional.v[i] / denominator;
        }

        private void processP() {
            System.out.print("procesP: ");
            if (winnerIndex != -1) {
                System.out.println("F2 active!");
                Neuron winner = attentional.y.get(winnerIndex);
                for (int i = 0; i < dimension; i++)
                    attentional.p[i] = attentional.u[i] + d * winner.topDown[i];
            }
            else {
                System.out.println("F2 inactive!");
                for (int i = 0; i < dimension; i++)
                    attentional.p[i] = attentional.u[i];
            }
        }

        private void processQ() {
            double denominator = e + vectorLength(attentional.p);
            for (int i = 0; i < dimension; i++)
                attentional.q[i] = attentional.p[i] / denominator;
        }
    }

    class OrientingSubsystem {

        private double[] r;

        public OrientingSubsystem() {
            clearOutput();
        }

        private void process() {
            double denominator =
                    e + vectorLength(attentional.u) + vectorLength(scaleVector(attentional.p, c));
            for (int i = 0; i < dimension; i++)
                r[i] = (attentional.u[i] + c * attentional.p[i]) / denominator;
        }

        private void clearOutput() {
            this.r = new double[dimension];
        }
    }

    class Neuron {
        private double[] bottomUp;
        private double[] topDown;
        private double net = 0.d;
        private double output = 0.d;
        private int id;

        public Neuron(int id) {
            this.id = id;
            bottomUp = new double[dimension];
            topDown = new double[dimension];
            for (int i = 0; i < dimension; i++)
                bottomUp[i] = /* rand.nextDouble() * */ initialBottomUpLimit;
        }

        private double process() {
            for (int i = 0; i < dimension; i++) {
                net += attentional.p[i] * bottomUp[i];
            }
            return net;
        }

        public void setWinner(int id) {
            output = this.id == id ? d : 0.d;
        }

        public void updateWeights() {
            for (int i = 0; i < dimension; i++)
                bottomUp[i] = topDown[i] = attentional.u[i] / updateDenominator;
        }
    }

    public static String print(double[] t) {
        StringBuilder sb = new StringBuilder("[");
        for (double d : t)
            sb.append(t + " ");
        return sb.append("]").toString();
    }

    public static void main(String[] args) {
        Network n = new Network(5, 10.d, 10.d, 0.1d, 0.9d, 0.d, 0.95, 0.2);
        n.process(new double[] {0.2d, 0.7d, 0.1d, 0.5d, 0.4d});
    }
}
