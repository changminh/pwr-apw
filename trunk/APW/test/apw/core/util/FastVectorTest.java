/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apw.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rekin
 */
public class FastVectorTest {

    private FastVector f = new FastVector();
    /** Initial data used for testing */
    static Object[] inst = new Object[]{
        new Integer(0),
        new Integer(1),
        new Integer(2)
    };

    @Before
    public void setUp() {
        for (int i = 0; i < inst.length; i++)
            f.addElement(inst[i]);
    }

    /**
     * Test of addElement method, of class FastVector.
     */
    @Test
    public void testAddElement() {
        Object element = new Integer(4);
        int oldSize = f.getSize();
        f.addElement(element);
        assertEquals(f.getSize(), oldSize + 1);
        assertEquals(element, f.getData()[oldSize]);
    }

    /**
     * Test of removeElement method, of class FastVector.
     */
    @Test
    public void testRemoveElement() {
        // test when element exist in FastVector
        Object element = inst[0];
        int oldSize = f.getSize();
        boolean result = f.removeElement(element);
        assertEquals(result, true);
        assertEquals(oldSize - 1, f.getSize());

        // test when element doesn't exist in FastVector
        oldSize = f.getSize();
        element = Integer.MAX_VALUE;
        result = f.removeElement(element);
        assertEquals(result, false);
        assertEquals(oldSize, f.getSize());
    }

    /**
     * Test of getSize method, of class FastVector.
     */
    @Test
    public void testGetSize() {
        int startSize = f.getSize();
        Object element;
        for (int i = 0; i < 10; i++) {
            element = new Integer(i);
            f.addElement(element);
            assertEquals(f.getSize(), ++startSize);
        }
        for (int i = 0; i < 10; i++) {
            element = new Integer(i);
            f.removeElement(element);
            assertEquals(f.getSize(), --startSize);
        }
    }

    /**
     * Test of getData method, of class FastVector.
     */
    @Test
    public void testGetData() {

        Object[] data = f.getData();
        for (int i = 0; i < inst.length; i++)
            assertEquals(data[i], inst[i]);
    }

    /**
     * Test of removeElementAt method, of class FastVector.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void testRemoveElementAt() {
        List<Object> l = Arrays.asList(inst);
        // mutable list
        List<Object> lm = new ArrayList<Object>();
        lm.addAll(l);

        assertEquals(lm.remove(0), f.removeElementAt(0));
        assertEquals(lm.remove(0), f.removeElementAt(0));
        assertEquals(lm.remove(0), f.removeElementAt(0));
        assertEquals(lm.remove(0), f.removeElementAt(0));
    }
}