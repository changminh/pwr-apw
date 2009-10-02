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
public class ART2_Util {
    public static void checkParameters(double a, double b, double c, double d, double rho, double theta) {
        StringBuilder sb = new StringBuilder();
        boolean error = false;
        String s1 = "0 &lt;&lt; a, b";
        String s2 = "0 &lt; c &lt;&lt; 1";
        String s3 = "0 &le; d &le; 1";
        String s4 = "cd &le; 1 - d";
        String s5 = "0 &le; theta &le; 1";
        String s6 = "0.7071 &lt; rho &lt; 1";
        sb.append("<html><body>The following conditions MUST be satisfied: <br><br>");
        // Condition 1:
        if (a <= 0.d || b <= 0.d) {
            sb.append("<u>" + s1 + "</u><br>");
            error = true;
        }
        else
            sb.append(s1 + "<br>");
        // Condition 2:
        if (c <= 0.d || c >= 1.d) {
            sb.append("<u>" + s2 + "</u><br>");
            error = true;
        }
        else
            sb.append(s2 + "<br>");
        // Condition 3:
        if (d <= 0.d || d >= 1.d) {
            sb.append("<u>" + s3 + "</u><br>");
            error = true;
        }
        else
            sb.append(s3 + "<br>");
        // Condition 4:
        if (c * d > 1.d - d) {
            sb.append("<u>" + s4 + "</u><br>");
            error = true;
        }
        else
            sb.append(s4 + "<br>");
        // Condition 5:
        if (theta < 0.d || theta > 1.d) {
            sb.append("<u>" + s5 + "</u><br>");
            error = true;
        }
        else
            sb.append(s5 + "<br>");
        // Condition 6:
        if (rho <= (0.5d * Math.sqrt(2.d)) || rho > 1.d) {
            sb.append("<u>" + s6 + "</u><br>");
            error = true;
        }
        else
            sb.append(s6 + "<br>");
        sb.append("</body></html>");
        if (error)
            throw new IllegalArgumentException(sb.toString());
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

    public static String print(double[] t) {
        StringBuilder sb = new StringBuilder("[");
        for (double d : t)
            sb.append(d + " ");
        return sb.substring(0, sb.length() - 1) + "]".toString();
    }

    public static ArrayList<Integer> shuffle(int size, boolean reallyShuffle) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (reallyShuffle) {
            HashSet<Integer> shuffled = new HashSet<Integer>();
            Random r = new Random();
            while(result.size() != size) {
                int i = r.nextInt(size);
                if (shuffled.add(i)) {
                    result.add(i);
                }
            }
        }
        else {
            for (int i = 0; i < size; i++)
                result.add(i);
        }
        return result;
    }
}
