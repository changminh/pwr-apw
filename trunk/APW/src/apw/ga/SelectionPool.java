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
import static java.util.Arrays.*;

/**
 * Random sampling without replacement class. Backed by
 * {@link DistrGenerator}.
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class SelectionPool {

    ArrayList<double[]> a;
    DistrGenerator dg;

    /**
     * double [] is assumed as a sampling object.
     * The last element of array is interpreted as a
     * sampling probability.
     */
    public SelectionPool(double[][] d) {
        a = new ArrayList<double[]>();
        a.addAll(asList(d));
        dg = new DistrGenerator(a);
    }

    /**
     * Samples an object according to its probability.
     * Returned object is removed from this pool.
     * @return object from pool
     */
    public double[] poll() {
        int i = dg.nextInt();
        double[] r = a.remove(i);
        if (!a.isEmpty())
            dg = new DistrGenerator(a);
        return r;
    }

    /**
     * Is the pool empty?
     * @return true if the pool is empty, false otherwise
     */
    public boolean isEmpty() {
        return a.isEmpty();
    }
}
