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

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class DistrGenerator {

    //long seed = 1234567L;
    private Random r = new Random();
    double[] d = new double[]{
        0.5,
        0.5d};
    double[] s = new double[d.length];

    public DistrGenerator() {
        double sum = 0;
        for (int i = 0; i < d.length; i++) {
            sum += d[i];
            s[i] = sum;
        }
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
        double u = r.nextDouble();


        int i = 0, k;
        int j = d.length - 1;
        do {
            k = (i + j) / 2;
            if (u > s[k])
                i = k + 1;
            else
                j = k;
        } while (i < j);
        return i;
    }

    public static void main(String[] s) {
        DistrGenerator dg = new DistrGenerator();

        int[] t = new int[dg.d.length];
        for (int i = 0; i < t.length; i++)
            t[i] = 0;
        int n;
        for (n = 0; n < 100000000; n++)
            t[dg.nextInt()]++;

        System.out.println("instance no:     " + n);
        System.out.println("instances:       " + Arrays.toString(t));

        double[] p = new double[t.length];
        for (int i = 0; i < p.length; i++)
            p[i] = ((double) t[i]) / (double) n;
        System.out.println("probabilities:   " + Arrays.toString(p));

        for (int i = 0; i < p.length; i++)
            p[i] = Math.abs(p[i] - dg.d[i]);
        System.out.println("error:           " + Arrays.toString(p));

        double sum = 0;
        for (int i = 0; i < p.length; i++)
            sum += p[i];
        System.out.println("error summed:    " + sum);

        double lastRun = 3.3480000000000835E-4d;
        System.out.println("is ok?           " + (lastRun == sum));
        System.out.println("*************************************************");
    }
}