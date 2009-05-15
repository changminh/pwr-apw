/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

import java.util.HashMap;

/**
 *
 * @author Nitric
 */

/**
 * Complex is a set of selectors - at most one selector is associated with one record's attribute.
 * @author Nitric
 */
public class Complex {
    private HashMap<Integer, Selector> selectors = new HashMap<Integer, Selector>();
    
    public void addSelector(Selector selector) {
        int i = selector.getAttributeId();
        if (selectors.containsKey(selector.getAttributeId()))
            throw new IllegalArgumentException("Complex already contains selector for attribute #" + i + "!");
        else
            selectors.put(i, selector);
    }

    public Selector getSelector(int attributeNumber) {
        return selectors.get(attributeNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Selector sel : selectors.values())
            sb.append(sel + "\n");
        return sb.toString();
    }
}
