package apw.temp_nitric.gui;

import apw.temp_nitric.logic.Selector;
import javax.swing.JPanel;

/**
 *
 * @author Nitric
 */
public abstract class SelectorCreatorPanel extends JPanel {
    public abstract Selector getSelector();
}
