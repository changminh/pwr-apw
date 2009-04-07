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

import apw.core.util.ArrayIterator;
import apw.core.util.EmptyIterator;
import apw.core.util.FastVector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class Samples implements List<Sample> {

    // BEGIN View attributes ****************************
    private void notifySamplesOfViewChange() {
        for (int i = 0; i < data.getSize(); i++)
            ((Sample) data.get(i)).viewObsolete = true;
    }
    int classAttributeIndex;
    boolean selected[];

    public void setClassAttributeIndex(int index) {
        if (index == classAttributeIndex)
            return;
        classAttributeIndex = index;
        notifySamplesOfViewChange();
    }

    public void setSelected(int index, boolean select) {
        selected[index] = select;
        notifySamplesOfViewChange();
    }

    public boolean isSelected(int index) {
        return selected[index];
    }

    public Attribute getClassAttribute() {
        if (classAttributeIndex < 0 || classAttributeIndex > atts.size())
            throw new IllegalStateException("Class not set.");
        return atts.get(classAttributeIndex);
    }
    // END   View attributes ****************************

    public Samples(ArrayList<Attribute> atts) {
        this.atts = atts;
    }
    FastVector data;
    ArrayList<Attribute> atts;
    String name;

    public String getName() {
        return name;
    }

    public ArrayList<Attribute> getAtts() {
        return atts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(FastVector data) {
        if (data == null)
            return;
        this.data = data;
        for (int i = 0; i < data.getSize(); i++)
            ((Sample) data.get(i)).setSamples(this);
        selected = new boolean[atts.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = true;
        classAttributeIndex = atts.size() - 1;
    }

    @Override
    public int size() {
        if (data == null)
            return 0;
        return data.getSize();
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.getSize() < 1;
    }

    @Override
    public boolean contains(Object o) {
        if (data == null)
            return false;
        Object os[] = data.getData();
        for (int i = 0; i < os.length; i++)
            if (os[i].equals(o))
                return true;
        return false;
    }

    @Override
    public Iterator<Sample> iterator() {
        if (data == null)
            return new EmptyIterator<Sample>();
        ArrayIterator it = new ArrayIterator(data.getData(), size());
        return it;
    }

    @Override
    public Object[] toArray() {
        if (data == null)
            return null;
        Sample[] r = new Sample[size()];
        System.arraycopy(data.getData(), 0, r, 0, r.length);
        return data.getData();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @SuppressWarnings("unused")
    public boolean add(Sample e) {
        try {
            data.addElement(e);
        } catch (OutOfMemoryError err) {
            return false;
        }
        return true;
    }

    @Override
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

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean contains = true;
        for (Object object : c)
            contains &= contains(object);
        return contains;
    }

    @Override
    public boolean addAll(Collection<? extends Sample> c) {
        boolean added = false;
        for (Object object : c)
            added |= add((Sample) object);
        return added;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removed = false;
        for (Object object : c)
            removed |= remove(object);
        return removed;
    }

    @Override
    public Sample get(int index) {
        return (Sample) data.getData()[index];
    }

    @Override
    public Sample set(int index, Sample element) {
        if (index > data.getSize())
            throw new IndexOutOfBoundsException("index greater than no of samples");
        Sample old = (Sample) data.getData()[index];
        data.getData()[index] = element;
        return old;
    }

    @Override
    public int indexOf(Object o) {
        Object os[] = data.getData();
        for (int i = 0; i < os.length; i++)
            if (os[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Object os[] = data.getData();
        for (int i = os.length - 1; i >= 0; i--)
            if (os[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public String toString() {
        Iterator<Sample> i = iterator();
        if (!i.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            Sample e = i.next();
            sb.append(e);
            if (!i.hasNext())
                return sb.append(']').toString();
            sb.append(", ");
        }

    }

    @Override
    public Sample remove(int index) {
        return (Sample) data.removeElementAt(index);
    }

    @Override
    public void add(int index, Sample element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends Sample> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<Sample> listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<Sample> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Sample> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    TableModel tm;

    public AbstractTableModel getTableModel() {
        if (tm == null)
            tm = new TableModel();
        return tm;
    }

    private class TableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return size();
        }

        @Override
        public int getColumnCount() {
            return atts.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return ((Sample) data.get(rowIndex)).get(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return atts.get(column).getName();
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Object.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Sample s = (Sample) data.get(rowIndex);
            s.set(columnIndex, aValue);
        }
    };
}
