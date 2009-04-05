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

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class FastVector {

    private Object data[];
    private int capacity;
    private int increment;
    private int size;

    /**
     * Add an element to the end of the array.
     */
    public void addElement(Object element) {
        if (size >= capacity) {
            capacity += (increment == 0) ? capacity : increment;
            Object newData[] = new Object[capacity];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
        data[size++] = element;
    }

    public boolean removeElement(Object element) {
        for (int i = 0; i < size; i++)
            if (data[i].equals(element)) {
                removeElementAt(i);
                return true;
            }
        return false;
    }

    /**
     * Get number of ints currently stored in the array;
     */
    public int getSize() {
        return size;
    }

    /**
     * Get access to array data
     */
    public Object[] getData() {
        return data;
    }

    /**
     * Constructor.
     * @param initialCapacity Number of ints the object can hold
     * without reallocating the array.
     * @param capacityIncrement Once the array has grown beyond
     * its capacity, how much larger the reallocated array should be.
     */
    public FastVector(int initialCapacity, int capacityIncrement) {
        data = new Object[initialCapacity];
        capacity = initialCapacity;
        increment = capacityIncrement;
        size = 0;
    }

    /**
     * Constructor.
     * When the array runs out of space, its size is doubled.
     * @param initialCapacity Number of ints the object can hold
     * without reallocating the array.
     */
    public FastVector(int initialCapacity) {
        data = new Object[initialCapacity];
        capacity = initialCapacity;
        increment = 0;
        size = 0;
    }

    /**
     * Constructor.
     * The array is constructed with initial capacity of one integer.
     * When the array runs out of space, its size is doubled.
     */
    public FastVector() {
        data = new Object[1];
        capacity = 1;
        increment = 0;
        size = 0;
    }

    /**
     * Deletes an element from this vector.
     *
     * @param index the index of the element to be deleted
     */
    public Object removeElementAt(int index) {
        if (index >= size)
            throw new IllegalArgumentException("index greater or equal to size");

        Object removed = data[index];
        System.arraycopy(data, index + 1, data, index,
                size - index - 1);

        // clear the last reference
        data[size - 1] = null;
        size--;

        if (size < (data.length / 2)) {
            Object newData[] = new Object[size];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
            capacity = size;
        }
        return removed;
    }
}