/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.controls;

import javax.swing.JComboBox;

/**
 *
 * @author Nitric
 */
public class ComboSelectorForNumber extends JComboBox {

    public ComboSelectorForNumber() {
        super(apw.augmentedLearning.logic.SelectorTypeForNumbers.values());
    }
}
