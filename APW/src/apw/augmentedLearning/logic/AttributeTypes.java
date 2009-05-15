/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;


/**
 *
 * @author nitric
 */
public enum AttributeTypes {
    INTEGER ("Liczba całkowita"),
    REAL_NUMBER ("Liczba rzeczywista"),
    NOMINAL ("Wartość nominalna");

    private final String humanReadable;
    
    AttributeTypes(String s) {
        humanReadable = s;
    }

    @Override
    public String toString() {
        return humanReadable;
    }

    public String stringRepresentation() {
        switch(this) {
            case INTEGER: return "integer";
            case REAL_NUMBER: return "real";
            case NOMINAL: return "nominal";
        }
        return null;
    }
}
