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

import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javax.swing.JOptionPane;
import static apw.art2.ART2_Util.*;

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
    private double initialBottomUp;
    private double a, b, c, d, rho, theta;
    private final double e = 0.d;
    private double doubleTheta, thetaTheta;
    private double[] actualInput;
    private double updateDenominator;
    private AttentionalSubsystem attention;
    private OrientingSubsystem orienting;
    private HashSet<Integer> banned = new HashSet<Integer>();

    // Sublayer indexes:
    private static final int w = 0;
    private static final int x = 1;
    private static final int v = 2;
    private static final int u = 3;
    private static final int p = 4;
    private static final int q = 5;

    // Sublayler outputs: previous iteration and actual iteration
    private static final int prev = 0;
    private static final int pres = 1;

    private static final int sublayers = 6;

    private static final Random rand = new Random();

    public Network(int dimension, double a, double b, double c, double d, double rho, double theta) {
        this.dimension = dimension;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.rho = rho;
        this.theta = theta;
        try {
            ART2_Util.checkParameters(a, b, c, d, rho, theta);
        }
        catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new ExceptionInInitializerError();
        }
        attention = new AttentionalSubsystem();
        orienting = new OrientingSubsystem();
        initialize();
    }

    public int process(double[] input) {
        winnerIndex = -1;
        resonance = false;
        resetOutput();
        banned.clear();
        actualInput = input;
        outer:
        while(!resonance) {
            attention.processF1();                    // Needs to be repeated
            attention.processF1();
            orienting.process();
            inner:
            while(!resonance) {
                vigilanceTest();
                if (reset) {
                    // Reset...
                    cycleCounter = 1;
                    for (Neuron n : attention.f2)
                        if (n.output > 0)
                            banned.add(n.id);
                    continue outer;
                }
                // As we are here, it means that there is no reset signal
                if (cycleCounter == 1) {
                    cycleCounter++;
                    // No reset, but too early for resonance - there must be an additional pass
                    int counter = 0;
                    double maxNet = Double.NEGATIVE_INFINITY;
                    double temp;
                    // Competition:
                    winnerIndex = -1;
                    for (Neuron n : attention.f2) {
                        temp = n.process();
                        // System.out.println("temp = " + temp);
                        if (temp > maxNet && !banned.contains(n.id)) {
                            maxNet = temp;
                            winnerIndex = counter;
                        }
                        counter++;
                    }
                    // System.out.println("");
                    for (Neuron n : attention.f2)
                        n.setWinner(winnerIndex);
                    // Updating output of layers:
                    attention.updatePQ();
                    attention.processF1();
                    continue inner;
                }
                else {
                    // Resonance:
                    resonance = true;
                    attention.f2.get(winnerIndex).updateWeights();
                }
            }
        }
        return winnerIndex;
    }

    private void initialize() {
        // Calculate bootom-up initial weights limit:
        initialBottomUp = (1.015d / ((1 - d) * Math.sqrt(dimension)));            // 1.05 - Warning :P
        for (int i = 0; i < 150; i++)
            attention.f2.add(new Neuron(neuronCounter++));
        updateDenominator = 1.d - d;
        thetaTheta = theta * theta;
        doubleTheta = 2 * theta;
    }

    private double f(double par) {
        return par > theta ? par : 0.d;
            // par : (doubleTheta * par * par) / (par * par + thetaTheta);
    }

    private boolean vigilanceTest() {
        return (reset = rho > e + vectorLength(orienting.r));
    }

    private void resetOutput() {
        cycleCounter = 1;
        attention.clearOutput();
        orienting.clearOutput();
    }

    class AttentionalSubsystem {

        private double[][][] f1 = new double[2][sublayers][];    // output of 6 sublayers of F1 layer
        private ArrayList<Neuron> f2 = new ArrayList<Neuron>();  // F2 layer

        public AttentionalSubsystem() {
            for (int i = 0; i < sublayers; i++)
                f1[1][i] = new double[dimension];
            clearOutput();
        }

        private void processF1() {
            int counter = 0;
            boolean stabilization = false;
            while (!stabilization) {
                counter++;
                stabilization = true;
                for (int i = 0; i < sublayers; i++)
                    for (int j = 0; j < dimension; j++)
                        f1[prev][i][j] = f1[pres][i][j];
                processW();
                processX();
                processV();
                processU();
                processP();
                processQ();
                // Check whether there were changes of weights:
                for (int i = 0; i < sublayers; i++)
                    for (int j = 0; j < dimension; j++)
                        if (Math.abs(f1[prev][i][j] - f1[pres][i][j]) > 0.0001)
                            stabilization = false;
            }
            System.out.println("counter = " + counter);
        }

        private void clearOutput() {
            for (int i = 0; i < sublayers; i++) {
                f1[0][i] = f1[1][i];
                f1[1][i] = new double[dimension];
            }
            // Reseting output of neurons:
            for (Neuron n: f2)
                n.setWinner(-1);
        }

        private void updatePQ() {
            processP();
            processQ();
        }

        private void processW() {
            for (int i = 0; i < dimension; i++) {
                attention.f1[pres][w][i] = actualInput[i] + a * attention.f1[pres][u][i];
            }
        }

        private void processX() {
            double denominator = e + vectorLength(attention.f1[pres][w]);
            for (int i = 0; i < dimension; i++) {
                attention.f1[pres][x][i] = attention.f1[pres][w][i] / denominator;
            }
        }

        private void processV() {
            for (int i = 0; i < dimension; i++) {
                attention.f1[pres][v][i] = f(attention.f1[pres][x][i]) + b * f(attention.f1[pres][q][i]);
            }
        }

        private void processU() {
            double denominator = e + vectorLength(attention.f1[pres][v]);
            for (int i = 0; i < dimension; i++)
                attention.f1[pres][u][i] = attention.f1[pres][v][i] / denominator;
        }

        private void processP() {
            // System.out.print("procesP: ");
            if (winnerIndex != -1) {
                // System.out.println("F2 active!");
                Neuron winner = attention.f2.get(winnerIndex);
                for (int i = 0; i < dimension; i++)
                    attention.f1[pres][p][i] = attention.f1[pres][u][i] + d * winner.topDown[i];
            }
            else {
                // System.out.println("F2 inactive!");
                for (int i = 0; i < dimension; i++)
                    attention.f1[pres][p][i] = attention.f1[pres][u][i];
            }
        }

        private void processQ() {
            double denominator = e + vectorLength(attention.f1[pres][p]);
            for (int i = 0; i < dimension; i++)
                attention.f1[pres][q][i] = attention.f1[pres][p][i] / denominator;
        }
    }

    class OrientingSubsystem {

        private double[] r;

        public OrientingSubsystem() {
            clearOutput();
        }

        private void process() {
            double denominator =
                    e + vectorLength(attention.f1[pres][u]) + c * vectorLength(attention.f1[pres][p]);
            for (int i = 0; i < dimension; i++)
                r[i] = (attention.f1[pres][u][i] + c * attention.f1[pres][p][i]) / denominator;
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
                bottomUp[i] = initialBottomUp;
        }

        private double process() {
            net = 0.d;
            for (int i = 0; i < dimension; i++) {
                net += attention.f1[pres][p][i] * bottomUp[i];
            }
            return net;
        }

        public void setWinner(int id) {
            output = this.id == id ? d : 0.d;
        }

        public void updateWeights() {
            for (int i = 0; i < dimension; i++)
                bottomUp[i] = topDown[i] = attention.f1[pres][u][i] / updateDenominator;
        }
    }

    public static void main(String[] args) throws Exception {
        // Network n = new Network(4, 10, 10, 0.1d, 0.9d, 0.9, 0.01);
        // Samples samples = new ARFFLoader(new File("data/iris.arff")).getSamples();
        Network n = new Network(4, 10, 10, 0.1d, 0.9d, 0.999d, 0.01d);
        Samples samples = new ARFFLoader(new File("data/test.arff")).getSamples();
        ArrayList<double[]> inst = new ArrayList<double[]>();
        // n.process(new double[] {0.2, 0.7, 0.1, 0.5, 0.4});
        if (false)
            return;
        int[] results = new int[30];
        double[] temp;
        for (Sample s : samples) {
            temp = new double[4];
            for (int i = 0; i < 4; i++)
                temp[i] = (Double) s.get(i);
            inst.add(temp);
        }
        for (int i = 0; i < 0; i++)
            for (int j = 0; j < inst.size(); j++)
                n.process(inst.get(j));
        for (int j = 0; j < inst.size(); j++)
            results[n.process(inst.get(j))]++;
        for (int i = 0; i < results.length; i++)
            System.out.print(results[i] + " ");
        System.out.println("");
    }
}
