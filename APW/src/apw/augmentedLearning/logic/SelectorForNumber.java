/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

/**
 *
 * @author Nitric
 */
public class SelectorForNumber extends Selector {
    protected SelectorTypeForNumbers type;
    protected double lowerLimit;
    protected double upperLimit;
    // TODO: Zabezpieczyć przed nieprawidłowymi zmianami!
    
    public double getLowerLimit() {
        return lowerLimit;
    }
    
    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public SelectorTypeForNumbers getType() {
        return type;
    }

    public void setType(SelectorTypeForNumbers type) {
        this.type = type;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    protected SelectorForNumber(int attributeId, boolean isNegated, SelectorTypeForNumbers type, double lower, double upper) {
        this.type = type;
        this.attributeId = attributeId;
        this.lowerLimit = lower;
        this.upperLimit = upper;
    }

    public static SelectorForNumber getSelGT(int attributeId, boolean isNegated, double lowerLimit) {
        return new SelectorForNumber(
                attributeId,
                isNegated, 
                SelectorTypeForNumbers.GREATER_THAN,
                lowerLimit,
                Double.POSITIVE_INFINITY
        );
    }

    public static SelectorForNumber getSelGE(int attributeId, boolean isNegated, double lowerLimit) {
        return new SelectorForNumber(
                attributeId,
                isNegated, 
                SelectorTypeForNumbers.GREATER_OR_EQUAL,
                lowerLimit,
                Double.POSITIVE_INFINITY
        );
    }

    public static SelectorForNumber getSelEQ(int attributeId, boolean isNegated, double value) {
        return new SelectorForNumber(attributeId, isNegated, SelectorTypeForNumbers.EQUAL, value, value);
    }

    public static SelectorForNumber getSelLT(int attributeId, boolean isNegated, double upperLimit) {
        return new SelectorForNumber(
                attributeId,
                isNegated, 
                SelectorTypeForNumbers.LOWER_THAN,
                Double.NEGATIVE_INFINITY,
                upperLimit
        );
    }

    public static SelectorForNumber getSelLE(int attributeId, boolean isNegated, double upperLimit) {
        return new SelectorForNumber(
                attributeId,
                isNegated,
                SelectorTypeForNumbers.LOWER_OR_EQUAL,
                Double.NEGATIVE_INFINITY,
                upperLimit
        );
    }

    public static SelectorForNumber getSelBelongs(int attributeId, boolean isNegated, double lowerLimit, double upperLimit) {
        return new SelectorForNumber(
                attributeId,
                isNegated, 
                SelectorTypeForNumbers.GREATER_OR_EQUAL,
                lowerLimit,
                upperLimit
        );
    }

    public static SelectorForNumber getUniversalSelector (int attributeId) {
        return new SelectorForNumber(
            attributeId, false, SelectorTypeForNumbers.ALL_VALUES, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public static SelectorForNumber getEmptySelector (int attributeId) {
        return new SelectorForNumber(
            attributeId, false, SelectorTypeForNumbers.NONE_VALUE, Double.NaN, Double.NaN);
    }

    @Override
    public String toString() {
        return
                "Id = " + attributeId + ", "
                + "type = " + type + ", "
                + "lower = " + lowerLimit + ", "
                + "greater = " + upperLimit;
    }

    public boolean isUniversal() {
        return type == SelectorTypeForNumbers.ALL_VALUES;
    }
}
