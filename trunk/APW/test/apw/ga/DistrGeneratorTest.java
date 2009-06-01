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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class DistrGeneratorTest {

    private static final double ALLOWED_ERROR = 0.01;
    private static final int SAMPLING_NO = 10000000;
    static double[] d = new double[]{
        0.01,
        0.05,
        0.10,
        0.15,
        0.20,
        0.25,
        0.01,
        0.01,
        0.01,
        0.02,
        0.19
    };
    static double[] d2 = new double[]{
        0.1,
        0.5,
        1,
        1.5,
        2,
        2.5,
        0.1,
        0.1,
        0.1,
        0.2,
        1.9
    };

    @Before
    public void setUp() {
    }

    /**
     * Test of nextInt method, of class DistrGenerator.
     */
    @Test
    public void testNextInt() {
        DistrGenerator dg = new DistrGenerator(d);

        int[] t = new int[d.length];
        for (int i = 0; i < t.length; i++)
            t[i] = 0;
        int n;
        for (n = 0; n < SAMPLING_NO; n++)
            t[dg.nextInt()]++;

        System.out.println("instance no:     " + n);
        System.out.println("instances:       " + Arrays.toString(t));

        double[] p = new double[t.length];
        for (int i = 0; i < p.length; i++)
            p[i] = ((double) t[i]) / (double) n;
        System.out.println("probabilities:   " + Arrays.toString(p));

        for (int i = 0; i < p.length; i++)
            p[i] = Math.abs(p[i] - d[i]);
        System.out.println("error:           " + Arrays.toString(p));

        double sum = 0;
        for (int i = 0; i < p.length; i++)
            sum += p[i];
        System.out.println("error summed:    " + sum);

        Assert.assertTrue(sum < ALLOWED_ERROR);
    }

    /**
     * Test of nextInt method, of class DistrGenerator.
     */
    @Test
    public void testNextInt2() {
        DistrGenerator dg = new DistrGenerator(d2);

        int[] t = new int[d.length];
        for (int i = 0; i < t.length; i++)
            t[i] = 0;
        int n;
        for (n = 0; n < SAMPLING_NO; n++)
            t[dg.nextInt()]++;

        System.out.println("instance no:     " + n);
        System.out.println("instances:       " + Arrays.toString(t));

        double[] p = new double[t.length];
        for (int i = 0; i < p.length; i++)
            p[i] = ((double) t[i]) / (double) n;
        System.out.println("probabilities:   " + Arrays.toString(p));

        for (int i = 0; i < p.length; i++)
            p[i] = Math.abs(p[i] - d[i]);
        System.out.println("error:           " + Arrays.toString(p));

        double sum = 0;
        for (int i = 0; i < p.length; i++)
            sum += p[i];
        System.out.println("error summed:    " + sum);

        Assert.assertTrue(sum < ALLOWED_ERROR);
    }
}