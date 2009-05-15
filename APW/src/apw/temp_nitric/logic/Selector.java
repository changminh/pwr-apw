/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.temp_nitric.logic;

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

    public abstract boolean isUniversal();
}
