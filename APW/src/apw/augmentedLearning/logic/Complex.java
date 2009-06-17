/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

import java.util.HashMap;
import static apw.augmentedLearning.logic.RelationBetweenSets.*;


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
            throw new IllegalArgumentException
                    ("Complex already contains selector for attribute #" + i + ", use 'alterSelector()'.");
        else
            selectors.put(i, selector);
    }

    public boolean alterSelector(Selector selector) {
        boolean result = selectors.containsKey(selector.getAttributeId());
        selectors.put(selector.getAttributeId(), selector);
        return result;
    }

    public Selector getSelector(int attributeNumber) {
        return selectors.get(attributeNumber);
    }

    public boolean covers(Object[] values) {
        for (int i = 0; i < selectors.size(); i++) {
            if (!selectors.get(i).covers(values[i])) {
                // System.out.println(" ==> nie pokrywam");
                return false;
            }
        }
        // System.out.println(" ==> pokrywam");
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Selector sel : selectors.values())
            sb.append(sel + "\n");
        return sb.toString();
    }

    /**
     *
     * @param other Complex to be compared with instance
     * @return one of the RelationBetweenSets values
     */
    public RelationBetweenSets isMoreGeneralThan(Complex other) {
        Selector ts, os;                    // Selector for 'this', Selector for 'other'
        boolean oneSelectorMoreGeneral = false;
        boolean oneSelectorLessGeneral = false;
        for (int i = 0; i < selectors.size(); i++) {
            if (oneSelectorLessGeneral && oneSelectorMoreGeneral)
                return NOT_COMPARABLE;
            os = other.getSelector(i);
            if ((ts = selectors.get(i)).isUniversal()) {
                if (os.isUniversal())
                    continue;
                else
                    oneSelectorMoreGeneral = true;
            }
            else {
                if (os.isUniversal()) {
                    oneSelectorLessGeneral = true;
                    continue;
                }
                switch (ts.isMoreGeneralThan(os)) {
                        case NOT_COMPARABLE: return NOT_COMPARABLE;
                        case MORE_GENERAL: oneSelectorMoreGeneral = true; break;
                        case LESS_GENERAL: oneSelectorLessGeneral = true; break;
                        case EQUAL: continue;
                }
            }
        }
        if (oneSelectorLessGeneral) {
            if (oneSelectorMoreGeneral)
                return NOT_COMPARABLE;
            else
                return LESS_GENERAL;
        }
        else
            if (oneSelectorMoreGeneral)
                return MORE_GENERAL;
            else
                return EQUAL;
    }

    public Complex intersection(Complex other) {
        Complex result = new Complex();
        for (int i = 0; i < selectors.size(); i++) {
            result.addSelector(getSelector(i).intersection(other.getSelector(i)));
        }
        return result;
    }
}
