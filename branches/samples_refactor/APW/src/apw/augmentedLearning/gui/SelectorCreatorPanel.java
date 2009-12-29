package apw.augmentedLearning.gui;

import apw.augmentedLearning.logic.Selector;
import javax.swing.JPanel;

/**
 *
 * @author Nitric
 */
public abstract class SelectorCreatorPanel extends JPanel {
    public abstract Selector getSelector();
}
