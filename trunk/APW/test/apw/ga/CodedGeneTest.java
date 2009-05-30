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

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;
import static apw.ga.CodedGene.*;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class CodedGeneTest {

    public CodedGeneTest() {
    }
    // CodedGene c = new CodedGene(5);
    CodedGene c = CodedGene.builder().
            gene(5, true).
            gene(5, true).
            build();

    @Test
    public void test() {
        int i;
        for (i = 0; i < 1000; i++)
            assertEquals(i, btg(gtb(i)));
        System.out.println("Done " + i + " iterations");
    }

    /**
     * Test coding
     */
    @Test
    public void testGrayEncodingWIP2() {
        for (int i = 0; i <= 31; i++) {
            c.encodeShifted(i, true, 0, 5);
            assertEquals(i, c.decodeShifted(true, 0, 5));
        }
    }

    @Test
    public void builderTest() {
        CodedGene cd = CodedGene.builder().
                gene(7, true).
                gene(5, true).
                gene(5, true).
                build();

        for (int i = 0; i <= 31; i++) {
            cd.encode(i, 0);
            cd.encode(i, 1);
            cd.encode(i, 2);

            assertEquals(i, cd.decodeInt(0));
            assertEquals(i, cd.decodeInt(1));
            assertEquals(i, cd.decodeInt(2));
        }
    }

    @Test
    public void testSimulatedRealUsage() {
        int bits = 30;
        int limit = 1 << bits;
        CodedGene cd = CodedGene.builder().
                gene(1, false).
                gene(bits, true).
                build();
        Random r = new Random(System.currentTimeMillis());
        int l = 0, b = 0, i;
        cd.encode(b, 0);
        cd.encode(l, 1);
        for (i = 0; i < 1000; i++) {
            // change gray coded signed number
            if (r.nextBoolean()) {
                l = r.nextInt(limit);
                cd.encode(l, 1);
            }
            // change nominal value
            if (r.nextBoolean()) {
                b = r.nextInt(2);
                cd.encode(b, 0);
            }
            assertEquals(l, cd.decodeInt(1));
            assertEquals(b, cd.decodeInt(0));
        }
        System.out.println("Properly simulated " + i +
                " changes (on " + bits + " bits).");
    }

    @Test
    public void testDoublesUsage() {
        double minValue = 0.0d;
        double maxValue = 100.0d;
        int bits = 2;
        double delta = 25d;
        double maxError = 0;
        CodedGene cd = CodedGene.builder().
                gene(bits, true).
                range(minValue, maxValue).
                build();
        Random r = new Random(System.currentTimeMillis());
        double l = 0, dif = 0;
        int i;
        cd.encode(l, 0);
        for (i = 0; i < 1000; i++) {
            // change gray coded signed number
            if (r.nextBoolean()) {
                l = r.nextDouble() * maxValue;
                cd.encode(l, 0);
            }
            cd.decodeDouble(0);
            dif = l - cd.decodeDouble(0);
            if (maxError < dif) maxError = dif;
            assertEquals(l, cd.decodeDouble(0), delta);
        }
        System.out.println("Properly simulated double " + i +
                " changes (on " + bits + " bits) with delta=" + delta +
                ", maximum error=" + maxError);
    }
}