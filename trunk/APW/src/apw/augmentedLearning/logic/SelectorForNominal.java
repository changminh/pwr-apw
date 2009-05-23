/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

import java.util.HashSet;

/**
 *
 * @author Nitric
 */
public class SelectorForNominal extends Selector {
    protected SelectorTypeForNominal type;
    protected HashSet<String> values = new HashSet<String>();

    protected SelectorForNominal(int attributeId, boolean isNegated, SelectorTypeForNominal type, HashSet<String> set) {
        this.attributeId = attributeId;
        this.type = type;
        this.forNominalAttribute = true;
        if (set != null)
            this.values = set;
    }

    public static SelectorForNominal getSelEmpty(int attributeId) {
        return new SelectorForNominal(attributeId, false, SelectorTypeForNominal.EMPTY, null);
    }

    public static SelectorForNominal getSelUniversal(int attributeId) {
        return new SelectorForNominal(attributeId, false, SelectorTypeForNominal.UNIVERSAL, null);
    }

    public static SelectorForNominal getSelSet(int attributeId, boolean isNegated, HashSet<String> set) {
        return new SelectorForNominal(attributeId, isNegated, SelectorTypeForNominal.SET, set);
    }

    public boolean covers(String attributeValue) {
        return values.contains(attributeValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id = " + attributeId + ", ");
        switch (type) {
            case EMPTY : return sb.append("empty").toString();
            case UNIVERSAL: return sb.append("universal ").toString();
            case SET: return sb.append(values).toString();
        }
        return null;
    }

    public boolean isUniversal() {
        return type == SelectorTypeForNominal.UNIVERSAL;
    }

    public boolean isEmpty() {
        return type == SelectorTypeForNominal.EMPTY;
    }

    @Override
    public Selector getNegation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
