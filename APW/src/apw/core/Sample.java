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
package apw.core;

import apw.core.util.SampleListAdapter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class Sample extends SampleListAdapter {

    ArrayList<Attribute> atts;
    Object[] vals;
    int size;

    public Sample(ArrayList<Attribute> atts, Object[] values) {
        if (atts.size() != values.length)
            throw new IllegalArgumentException("Attributes and values must be of same size");
        this.vals = values;
        this.atts = atts;
        size = values.length;
    }

    public int size() {
        return size;
    }

    public boolean contains(Object o) {
        for (int i = 0; i < vals.length; i++)
            if (vals[i].equals(o))
                return true;
        return false;
    }

    public Iterator<Object> iterator() {
        return new Iterator<Object>() {

            int i = 0;

            public boolean hasNext() {
                return i < size;
            }

            public Object next() {
                return get(i++);
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public Object[] toArray() {
        Object[] array = new Object[size];
        for (int i = 0; i < size; i++)
            array[i] = get(i);
        return array;
    }

    public Object get(int i) {
        return atts.get(i).getInterpretation(vals[i]);
    }

    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (get(i).equals(o))
                return i;
        return -1;
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Object e;
        for (int id = 0;; id++) {
            e = get(id);
            sb.append(e);
            if (id == size - 1)
                return sb.append(']').toString();
            sb.append(", ");
        }
    }

    @Override
    public Object set(int index, Object element) {
        Object old = atts.get(index).getInterpretation(element);
        vals[index] = atts.get(index).getRepresentation(element);
        return old;
    }
}
