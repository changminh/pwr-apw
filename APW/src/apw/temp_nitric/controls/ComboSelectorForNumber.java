/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.temp_nitric.controls;

import javax.swing.JComboBox;

/**
 *
 * @author Nitric
 */
public class ComboSelectorForNumber extends JComboBox {

    public ComboSelectorForNumber() {
        super(apw.temp_nitric.logic.SelectorTypeForNumbers.values());
    }
}
