/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

/**
 *
 * @author Nitric
 */
public enum SelectorTypeForNumbers {

    ALL_VALUES ("Dowolna wartość"),
    GREATER_THAN ("Większe od"),
    GREATER_OR_EQUAL ("Większe lub równe"),
    EQUAL ("Równe"),
    LOWER_THAN ("Mniejsze od"),
    LOWER_OR_EQUAL ("Mniejsze lub równe"),
    BELONGS_INCLUDING ("Jest z przedziału"),
    NONE_VALUE ("Żadna wartość");

    private SelectorTypeForNumbers(String description) {
        this.description = description;
    }

    private String description;

    @Override
    public String toString() {
        return description;
    }
}
