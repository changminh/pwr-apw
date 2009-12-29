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

import java.util.Random;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class OnePlusOne {

    public static void main(String[] args) {
        double[] x = new double[2], t = null;
        double[] y = new double[2];
        double c1 = 0.82d, c2 = 1.2d, s = 1.0d;
        Random r = new Random(System.nanoTime());

        x[0] = r.nextDouble() * 2 - 1;
        x[1] = r.nextDouble() * 2 - 1;

        double lF =F(x), nF;
        int k = 0, c = 0, j = 0;
        do {
            // tworzenie nowego osobnika
            for (int i = 0; i < x.length; i++)
                y[i] = x[i] + s * r.nextGaussian();

            nF = F(y);
            // czy jest lepszy?
            if (nF > lF) {
                t = x;
                x = y;
                y = t;
                c++;
                lF = nF;
            }

            // sprawdzanie liczby sukcesów co 5 iteracji
            if (k == 5) {
                // brak sukcesów -> zwiększamy zasięg
                if (c == 0)
                    s = s * c1;

                // więcej niż 1/5 sukcesów -> zmniejszamy zasięg
                else if (c > 1)
                    s = s * c2;

                // reset liczników liczby sukcesów
                k = 0;
                c = 0;
            }
            k++;
            j++;
            System.out.println(j+";"+F(x) + ";" + s);
        } while (j < 1000);

    }

    // 2 - x^2 - y^2
    public static double F(double[] c) {
        return 2 - c[0] * c[0] - c[1] * c[1];
    }
}
