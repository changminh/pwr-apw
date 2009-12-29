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
package apw.ga;

import java.util.ArrayList;

/**
 * Discrete probability distribution random number generator.
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class DistrGenerator {

    private double[] s;
    final double max;

    /**
     * The distribution will be normalized.
     * @param distribution
     */
    public DistrGenerator(double[] d) {
        this.s = new double[d.length];
        double sum = 0;
        for (int i = 0; i < d.length; i++) {
            sum += d[i];
            s[i] = sum;
        }
        max = sum;
    }

    DistrGenerator(GeneticAlgorithm.Chromosome[] c) {
        this.s = new double[c.length];
        double sum = 0;
        for (int i = 0; i < c.length; i++) {
            sum += c[i].f;
            s[i] = sum;
        }
        max = sum;
    }

    DistrGenerator(ArrayList<double[]> d) {
        this.s = new double[d.size()];
        //this.r = new Random(System.nanoTime());
        double sum = 0;
        int last = d.get(0).length - 1;
        for (int i = 0; i < d.size(); i++) {
            sum += d.get(i)[last];
            s[i] = sum;
        }
        max = sum;
    }

    /**
     * A method which should run like this one:
     * <pre>
     *        int i;
     *        for (i = 0; i < d.length; i++)
     *            if (s[i] >= u)
     *                return i;
     * </pre>
     * @return
     */
    public int nextInt() {
        double u = Math.random() * max;

        int i = 0, k;
        int j = s.length - 1;

        // binary search
        do {
            k = (i + j) / 2;
            if (u > s[k])
                i = k + 1;
            else
                j = k;
        } while (i < j);
        return i;
    }
}
