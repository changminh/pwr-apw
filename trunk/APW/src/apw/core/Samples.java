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
package apw.core;

import apw.core.util.ArrayIterator;
import apw.core.util.FastVector;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class Samples implements List<Sample> {

    FastVector data;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** @inheritdoc */
    public int size() {
        return data.getSize();
    }

    /** @inheritdoc */
    public boolean isEmpty() {
        return data.getSize() > 0;
    }

    /** @inheritdoc */
    public boolean contains(Object o) {
        Object os[] = data.getData();
        for (int i = 0; i < os.length; i++)
            if (os[i].equals(o))
                return true;
        return false;
    }

    /** @inheritdoc */
    public Iterator<Sample> iterator() {
        ArrayIterator it = new ArrayIterator(data.getData());
        return it;
    }

    /** @inheritdoc */
    public Object[] toArray() {
        return data.getData();
    }

    /** Operation not supported */
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** @inheritdoc */
    @SuppressWarnings("unused")
    public boolean add(Sample e) {
        try {
            data.addElement(e);
        } catch (OutOfMemoryError err) {
            return false;
        }
        return true;
    }

    /** @inheritdoc */
    public boolean remove(Object o) {
        if (!(o instanceof Sample))
            throw new ClassCastException(
                    "this list accepts only Samples");
        Object os[] = data.getData();
        for (int i = 0; i < os.length; i++)
            if (os[i].equals(o)) {
                data.removeElementAt(i);
                return true;
            }
        return false;

    }

    /** @inheritdoc */
    public boolean containsAll(Collection<?> c) {
        boolean contains = true;
        for (Object object : c)
            contains &= contains(object);
        return contains;
    }

    public boolean addAll(Collection<? extends Sample> c) {
        boolean added = false;
        for (Object object : c)
            added |= add((Sample) object);
        return added;
    }

    public boolean removeAll(Collection<?> c) {
        boolean removed = false;
        for (Object object : c)
            removed |= remove(object);
        return removed;
    }

    public Sample get(int index) {
        return (Sample) data.getData()[index];
    }

    /** @inheritdoc */
    public Sample set(int index, Sample element) {
        if (index > data.getSize())
            throw new IndexOutOfBoundsException("index greater than no of samples");
        Sample old = (Sample) data.getData()[index];
        data.getData()[index] = element;
        return old;
    }

    /** @inheritdoc */
    public int indexOf(Object o) {
        Object os[] = data.getData();
        for (int i = 0; i < os.length; i++)
            if (os[i].equals(o))
                return i;
        return -1;
    }

    /** @inheritdoc */
    public int lastIndexOf(Object o) {
        Object os[] = data.getData();
        for (int i = os.length - 1; i >= 0; i--)
            if (os[i].equals(o))
                return i;
        return -1;
    }

    /** @inheritdoc */
    public Sample remove(int index) {
        return (Sample) data.removeElementAt(index);
    }

    /** Unsupported */
    public void add(int index, Sample element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addAll(int index, Collection<? extends Sample> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ListIterator<Sample> listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ListIterator<Sample> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Sample> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
