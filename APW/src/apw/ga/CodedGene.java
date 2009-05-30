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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
final class CodedGene {

    int bits;
    BitSet set;
    boolean[] Gray;
    int[] pos, len;
    Double[] min, max;

    /**
     * TEST Constructor
     * @param bits
     */
    CodedGene(int bits) {
        this.bits = bits;
        set = new BitSet(bits);
        Gray = new boolean[]{true};
        pos = new int[]{0};
        len = new int[]{bits};
    }

    /**
     * Enocdes a number from range [min, max]
     * 
     * @param d number to encode
     * @param i gene number
     */
    public void encode(double d, int i) {
        // normalize to range [0, 1]
        d -= min[i];
        d /= max[i];

        // Max no of bits
        int m = 1 << len[i];

        // now the value goes in range [0, max no of bits]
        int n = (int) (m * d);

        encodeShifted(n, Gray[i], pos[i], len[i]);
    }

    /**
     * Encode number <code>n</code> at location <code>i</code>
     *
     * @param n number to be encoded
     * @param i gene index
     */
    public void encode(int n, int i) {
        encodeShifted(n, Gray[i], pos[i], len[i]);
    }

    void initRandomPopulation() {
    }

    /**
     * Decode gene with index <code>i</code>
     *
     * @param n number to be encoded
     * @param i gene index
     */
    public int decodeInt(int i) {
        return decodeShifted(Gray[i], pos[i], len[i]);
    }

    /**
     * Decode gene with index <code>i</code>
     *
     * @param n number to be encoded
     * @param i gene index
     */
    public double decodeDouble(int i) {
        int n = decodeShifted(Gray[i], pos[i], len[i]);
        double d = (double) n / (double) (1 << len[i]);
        return min[i] + d  * max[i];
    }

    /**
     * more appropriately distributed bit version of encode (theres no sign bit,
     * what helps preserve mutation it's real properties).
     *
     * @param n an integer number to encode
     * @param Gray use Gray coding
     * @param signed coding allows negative values
     * @param start adress of coding string in bitset
     * @param len bit number used to code
     */
    void encodeShifted(int n,
            boolean Gray,
            int start, int N) {

        // are there enough bits to represent num?
        if (1 << N <= n) // 2^b <= num?
            exc("Not enough bits to store the number " + n + " (max=" + (1 << N) + ").");

        // Gray'ize
        if (Gray) n = btg(n);

        // actual encoding - LSB first
        for (int j = 0; j < N; j++) {
            set.set(start + j, n % 2 != 0);
            n /= 2;
        }
    }

    /**
     * Decode a number from BitSet.
     *
     * @param Gray use Gray coding
     * @param signed coding allows negative values
     * @param start adress of coding string in bitset
     * @param len bit number used to code
     * @return decoded number
     */
    int decodeShifted(
            boolean Gray,
            int start, int N) {
        // decode starting with LSB
        int n = 0, k = 1;
        for (int j = start; j < start + N; j++) {
            if (set.get(j)) n += k;
            k <<= 1;
        }

        // decode Gray code
        if (Gray) n = gtb(n);

        return n;
    }

    // Utilities
    /**
     * IllegalArgumentException thrower
     * @param s message
     */
    private static final void exc(String s) {
        throw new IllegalArgumentException(s);
    }

    /**
     * Convert Gray code to straight representations.
     *
     * @parameter n Gray coded number
     * @result normal (straight) representation
     */
    static int gtb(int n) {
        for (int i = (n >> 1); i != 0; i >>= 1)
            n ^= i;
        return n;
    }

    /**
     * Convert binary string to Gray code.
     *
     * @parameter n number to encode
     * @result Gray encoded representation
     */
    static int btg(int n) {
        return n ^ (n >> 1);
    }

    public CodedGene() {
    }

    public static final TestBuilder builder() {
        return new TestBuilder();
    }

    public static class TestBuilder {

        public TestBuilder() {
        }
        ArrayList<Boolean> grays = new ArrayList();
        ArrayList<Boolean> sign = new ArrayList();
        ArrayList<Integer> pos = new ArrayList();
        ArrayList<Integer> len = new ArrayList();
        ArrayList<Double> min = new ArrayList();
        ArrayList<Double> max = new ArrayList();
        int no = 0;
        /** not what you think */
        int cum = 0;

        public CodedGene build() {
            CodedGene c = new CodedGene();
            c.Gray = new boolean[no];
            c.pos = new int[no];
            c.len = new int[no];
            c.max = new Double[no];
            c.min = new Double[no];
            c.set = new BitSet(cum);
            c.bits = cum;
            int i = 0;
            for (Boolean b : grays)
                c.Gray[i++] = b;
            i = 0;
            for (Integer p : pos)
                c.pos[i++] = p;
            i = 0;
            for (Integer p : len)
                c.len[i++] = p;
            i = 0;
            for (Double d : max)
                c.max[i++] = d;
            i = 0;
            for (Double d : min)
                c.min[i++] = d;
            return c;
        }

        /**
         * Use this builder method to define a gene to code integer value.
         * Use decode(int
         * 
         * @param bits
         * @param Gray
         * @return
         */
        public TestBuilder gene(int bits, boolean Gray) {
            grays.add(Gray);
            pos.add(cum);
            len.add(bits);
            min.add(null);
            max.add(null);
            cum += bits;
            no++;
            return this;
        }

        public TestBuilder range(double minimum, double maximum) {
            min.set(no - 1, minimum);
            max.set(no - 1, maximum);
            return this;
        }
    }

    void trashTables() {
        System.out.println("Grays " + Arrays.toString(Gray));
        System.out.println("Lengs " + Arrays.toString(len));
        System.out.println("Posss " + Arrays.toString(pos));
    }
}
