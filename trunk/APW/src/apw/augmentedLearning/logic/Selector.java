/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

/**
 *
 * @author Nitric
 */
public abstract class Selector {
    protected int attributeId;
    protected boolean forNominalAttribute = false;  // So we can easily determine selector's type
    protected boolean isNegated = false;

    public boolean isForNominalAttribute() {
        return forNominalAttribute;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public boolean isNegated() {
        return isNegated;
    }

    public abstract String textRepresetation();

    public abstract boolean isUniversal();
    public abstract boolean isEmpty();
    public abstract Selector getNegation();
    public abstract RelationBetweenSets isMoreGeneralThan(Selector other);
    public abstract Selector intersection(Selector other);
    public abstract boolean covers(Object o);
}
