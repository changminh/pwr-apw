/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

import static apw.augmentedLearning.logic.RelationBetweenSets.*;
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

    protected SelectorForNumber(int attributeId, SelectorTypeForNumbers type, double lower, double upper) {
        this.type = type;
        this.attributeId = attributeId;
        this.lowerLimit = lower;
        this.upperLimit = upper;
    }

    public static SelectorForNumber getSelGT(int attributeId, double lowerLimit) {
        return new SelectorForNumber(
                attributeId,
                SelectorTypeForNumbers.GREATER_THAN,
                lowerLimit,
                Double.POSITIVE_INFINITY
        );
    }

    /* public static SelectorForNumber getSelGE(int attributeId, boolean isNegated, double lowerLimit) {
        return new SelectorForNumber(
                attributeId,
                isNegated, 
                SelectorTypeForNumbers.GREATER_OR_EQUAL,
                lowerLimit,
                Double.POSITIVE_INFINITY
        );
    } */

    public static SelectorForNumber getSelEQ(int attributeId, double value) {
        return new SelectorForNumber(attributeId, SelectorTypeForNumbers.EQUAL, value, value);
    }

    /* public static SelectorForNumber getSelLT(int attributeId, boolean isNegated, double upperLimit) {
        return new SelectorForNumber(
                attributeId,
                isNegated, 
                SelectorTypeForNumbers.LOWER_THAN,
                Double.NEGATIVE_INFINITY,
                upperLimit
        );
    } */

    public static SelectorForNumber getSelLE(int attributeId, double upperLimit) {
        return new SelectorForNumber(
                attributeId,
                SelectorTypeForNumbers.LOWER_OR_EQUAL,
                Double.NEGATIVE_INFINITY,
                upperLimit
        );
    }

    public static SelectorForNumber getSelBelongs(int attributeId, double lowerLimit, double upperLimit) {
        return new SelectorForNumber(
                attributeId,
                SelectorTypeForNumbers.BELONGS_RIGHT_INCLUDING,
                lowerLimit,
                upperLimit
        );
    }

    public static SelectorForNumber getUniversalSelector (int attributeId) {
        return new SelectorForNumber(
            attributeId, SelectorTypeForNumbers.ALL_VALUES, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public static SelectorForNumber getEmptySelector (int attributeId) {
        return new SelectorForNumber(
            attributeId, SelectorTypeForNumbers.NONE_VALUE, Double.NaN, Double.NaN);
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

    public boolean isEmpty() {
        return type == SelectorTypeForNumbers.NONE_VALUE;
    }

    @Override
    public Selector getNegation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RelationBetweenSets isMoreGeneralThan(SelectorForNumber o) {
        switch (type) {
            case ALL_VALUES: {                                                  // Whole 'branch' tested
                if (o.isUniversal())
                    return EQUAL;                   
                else
                    return MORE_GENERAL;            
            } 
            case BELONGS_RIGHT_INCLUDING: {                                     // Whole 'branch' tested
                switch (o.type) {
                    case ALL_VALUES:                
                        return LESS_GENERAL;        
                    case BELONGS_RIGHT_INCLUDING: {
                        if (this.lowerLimit <= o.lowerLimit) {
                            if (this.lowerLimit == o.lowerLimit) {
                                // this.lower == o.lower
                                if (this.upperLimit <= o.upperLimit) {
                                    if (this.upperLimit == o.upperLimit) {
                                        // this.upper == o.upper
                                        return EQUAL;
                                    }
                                    else {
                                        // this.upper < o.upper
                                        return LESS_GENERAL;
                                    }
                                }
                                else {
                                    // this.upper > o.upper
                                    return MORE_GENERAL;
                                }
                            }
                            else {
                                // this.lower < o.lower
                                if (this.upperLimit <= o.upperLimit) {
                                    if (this.upperLimit == o.upperLimit) {
                                        // this.upper == o.upper
                                        return MORE_GENERAL;
                                    }
                                    else {
                                        // this.upper < o.upper
                                        return NOT_COMPARABLE;
                                    }
                                }
                                else {
                                    // this.upper > o.upper
                                    return MORE_GENERAL;
                                }
                            }
                        }
                        else {
                            // this.lower > o.lower
                            if (this.upperLimit <= o.upperLimit) {
                                if (this.upperLimit == o.upperLimit) {
                                    // this.upper == o.upper
                                    return LESS_GENERAL;
                                }
                                else {
                                    // this.upper < o.upper
                                    return LESS_GENERAL;
                                }
                            }
                            else {
                                // this.upper > o.upper
                                return NOT_COMPARABLE;
                            }
                        }
                    }
                    case EQUAL:         // remember, that in that case 'o.lowerLimit == o.upperLimit'
                        return (o.lowerLimit > this.lowerLimit && o.upperLimit <= this.upperLimit) ?
                            MORE_GENERAL : NOT_COMPARABLE;
                    case GREATER_THAN:
                        return this.lowerLimit >= o.lowerLimit ? LESS_GENERAL : NOT_COMPARABLE;
                    case LOWER_OR_EQUAL:
                        return this.upperLimit <= o.upperLimit ? LESS_GENERAL : NOT_COMPARABLE;
                    case NONE_VALUE:                        
                        return MORE_GENERAL;
                }
            }
            case EQUAL: {
                switch (o.type) {
                    case NONE_VALUE: return MORE_GENERAL;
                    case ALL_VALUES: return LESS_GENERAL;                       // OK
                    case EQUAL: return o.lowerLimit == lowerLimit ? EQUAL : NOT_COMPARABLE;
                    case BELONGS_RIGHT_INCLUDING:
                        return lowerLimit > o.lowerLimit && lowerLimit <= o.upperLimit ?
                            LESS_GENERAL : NOT_COMPARABLE;
                    case GREATER_THAN: return lowerLimit > o.lowerLimit ? LESS_GENERAL : NOT_COMPARABLE;
                    case LOWER_OR_EQUAL: return lowerLimit <= o.upperLimit ? LESS_GENERAL : NOT_COMPARABLE;
                }
            }
            case GREATER_THAN: {
                switch (o.type) {
                    case NONE_VALUE: return MORE_GENERAL;                       // OK
                    case ALL_VALUES: return LESS_GENERAL;                       // OK
                    case EQUAL: return o.lowerLimit > lowerLimit ? MORE_GENERAL : NOT_COMPARABLE;
                    case BELONGS_RIGHT_INCLUDING:                               // OK
                        return o.lowerLimit >= lowerLimit ? MORE_GENERAL : NOT_COMPARABLE;
                    case GREATER_THAN:                                          // OK
                        if (o.lowerLimit == lowerLimit)
                            return EQUAL;
                        else return o.lowerLimit > lowerLimit ? MORE_GENERAL : LESS_GENERAL;
                    case LOWER_OR_EQUAL: return NOT_COMPARABLE;                 // OK
                }
            }
            case LOWER_OR_EQUAL: {
                switch (o.type) {
                    case NONE_VALUE: return MORE_GENERAL;                       // OK
                    case ALL_VALUES: return LESS_GENERAL;                       // OK
                    case EQUAL: return o.lowerLimit <= upperLimit ? MORE_GENERAL : NOT_COMPARABLE;
                    case BELONGS_RIGHT_INCLUDING:                               // OK
                        return o.upperLimit <= upperLimit ? MORE_GENERAL : NOT_COMPARABLE;
                    case GREATER_THAN: return NOT_COMPARABLE;                   // OK
                    case LOWER_OR_EQUAL:                                        // OK
                        if (o.upperLimit == upperLimit) return EQUAL;
                        else return o.upperLimit > upperLimit ? LESS_GENERAL : MORE_GENERAL;
                }
            }
            case NONE_VALUE: {                                                  // OK
                return o.isEmpty() ? EQUAL : LESS_GENERAL;
            }
        }
        throw new RuntimeException("This should never happen!!!");
    }
}
