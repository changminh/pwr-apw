/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.temp_nitric.logic;

/**
 *
 * @author Nitric
 */
public enum SelectorTypeForNominal {
    UNIVERSAL ("Dowolna wartość"),
    SET ("Zbiór"),
    EMPTY ("Żadna wartość");           // At the moment I'm not sure that (1) and (3) would be useful...
    
    SelectorTypeForNominal(String description) {
        this.description = description;
    }

    private String description;

    @Override
    public String toString() {
        return description;
    }
}
