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

    /**
     *
     * @param other
     * @return -1, when complex is NOT more general than 'other';
     * 0, when complexes cannot be compared (for explanation look inside method implementation)
     * 1, when this complex IS more general than the 'other'.
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
                if (ts.forNominalAttribute) {
                    if (!os.forNominalAttribute)
                        throw new RuntimeException ("Incompatible selectors (id = " + i + ")");
                    if (((SelectorForNominal)ts).values.containsAll(((SelectorForNominal)os).values)) {
                        if (((SelectorForNominal)os).values.containsAll(((SelectorForNominal)ts).values))
                            ;
                        else
                            oneSelectorMoreGeneral = true;
                        continue;
                    }
                    else if (((SelectorForNominal)os).values.containsAll(((SelectorForNominal)ts).values)) {
                        oneSelectorLessGeneral = true;
                    }
                    else
                        return NOT_COMPARABLE;
                }
                else {
                    switch (((SelectorForNumber)ts).isMoreGeneralThan((SelectorForNumber) os)) {
                        case NOT_COMPARABLE: return NOT_COMPARABLE;
                        case MORE_GENERAL:
                            oneSelectorMoreGeneral = true;
                            break;
                        case LESS_GENERAL:
                            oneSelectorLessGeneral = true;
                        case EQUAL:
                            continue;
                    }
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
}
