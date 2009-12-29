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
package apw.core.util;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * It makes the Sample class implementation prettier.
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public abstract class SampleListAdapter implements List<Object>{


    public boolean isEmpty() {
        return false;
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean add(Object e) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean addAll(Collection<? extends Object> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean addAll(int index, Collection<? extends Object> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void add(int index, Object element) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Object remove(int index) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public ListIterator<Object> listIterator() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public ListIterator<Object> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public List<Object> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
