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
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class SelectionPoolTest {

    double[][] test = new double[][]{
        new double[]{1, 1},
        new double[]{2, 2},
        new double[]{3, 3}, // new double[]{4, 4}
    };
    Map<String, Double> cor = new HashMap<String, Double>() {

        {
            put("123", 1.0d / 15.0d);
            put("132", 1.0d / 10.0d);
            put("213", 1.0d / 12.0d);
            put("231", 1.0d / 4.0d);
            put("321", 1.0d / 3.0d);
            put("312", 1.0d / 6.0d);
        }
    };

    /**
     * Test of poll method, of class SelectionUtility.
     */
    @Test
    public void testPoll() {
        int takes = 1000000;
        Map<String, Integer> run = new HashMap();

        for (int i = 0; i < takes; i++) {
            SelectionPool su = new SelectionPool(test);
            int j = 0;
            StringBuilder sb = new StringBuilder();

            while (!su.isEmpty()) {
                double[] d = su.poll();
                int id = (int) d[0];
                sb.append(id);
                id++;
                j++;
            }
            String s = sb.toString();
            if (run.containsKey(s))
                run.put(s, run.get(s) + 1);
            else
                run.put(s, 1);
        }

        System.out.println("runs " + run);
        String[] sss = new String[run.size()];
        int i = 0;
        for (String ss : run.keySet()) {
            //System.out.println(ss + "=" + ((double) run.get(ss) / (double) takes));
            double val = ((double) run.get(ss) / (double) takes);
            double got = (double) cor.get(ss);
            sss[i++] = ss + "=" + val + " (" + cor.get(ss) + ", e=" + Math.abs(got-val);
            Assert.assertEquals(got, val, 0.001d);
        }
        Arrays.sort(sss);
        for (String ss : sss)
            System.out.println(ss);
    }
}