/*
 *  Copyright (c) 2009, Wrocław University of Technology
 *  All rights reserved.
 *  Redistribution  and use in source  and binary  forms,  with or
 *  without modification,  are permitted provided that the follow-
 *  ing conditions are met:
 * 
 *   • Redistributions of source code  must retain the above copy-
 *     right  notice, this list  of conditions and  the  following
 *     disclaimer.
 *   • Redistributions  in binary  form must  reproduce the  above
 *     copyright notice, this list of conditions and the following
 *     disclaimer  in  the  documentation and / or other materials
 *     provided with the distribution.
 *   • Neither  the name of the  Wrocław University of  Technology
 *     nor the names of its contributors may be used to endorse or
 *     promote products derived from this  software without speci-
 *     fic prior  written permission.
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRI-
 *  BUTORS "AS IS" AND ANY  EXPRESS OR IMPLIED WARRANTIES, INCLUD-
 *  ING, BUT NOT  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTA-
 *  BILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 *  NO EVENT SHALL THE COPYRIGHT HOLDER OR  CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCURE-
 *  MENT OF SUBSTITUTE  GOODS OR SERVICES;  LOSS OF USE,  DATA, OR
 *  PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER  CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING  NEGLIGENCE  OR  OTHERWISE) ARISING IN ANY WAY
 *  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSI-
 *  BILITY OF SUCH DAMAGE.
 */
package apw.gui.par;

import apw.classifiers.knn.KNNProperties2;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class PropertyPane extends JDialog {

    public static final int DEFAULT_NUMBER_OF_ROWS = 10;
    private static final int DEFAULT_NUMBER_OF_COLUMNS = 70;
    JOptionPane test;
    BorderLayout borderLayout;
    JScrollPane scrollPane;
    JPanel bottomPanel;
    JPanel mainPanel;
    JPanel messagePanel;
//    JPanel enclosingPanel;
    JSplitPane splitPane;
    JButton okButton;
    JButton cancelButton;
    JToggleButton showMessagesPaneButton;
    PropertyModel model;

    public PropertyPane(PropertyModel model) {
        super();
        this.model = model;
        initialize();
    }

    private void initialize() {
        borderLayout = new BorderLayout();
        setLayout(borderLayout);

        okButton = new JButton(new AbstractAction("OK") {

            public void actionPerformed(ActionEvent e) {
                okButtonClicked();
            }
        });

        cancelButton = new JButton(new AbstractAction("Cancel") {

            public void actionPerformed(ActionEvent e) {
                cancelButtonClicked();
            }
        });

        showMessagesPaneButton = new JToggleButton(new AbstractAction("Show Messages") {

            public void actionPerformed(ActionEvent e) {
                messagePanel.setVisible(showMessagesPaneButton.isSelected());
            }
        });

        bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(showMessagesPaneButton);
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);
        getContentPane().
                add(bottomPanel, BorderLayout.SOUTH);

        messagePanel = new JPanel(new BorderLayout());
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        // enclosingPanel = new JPanel(new BorderLayout());
        mainPanel = new JPanel(new GridBagLayout());
        getContentPane().
                add(splitPane, BorderLayout.CENTER);

        splitPane.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        splitPane.add(messagePanel, BorderLayout.SOUTH);

        JEditorPane messageArea =
                new JEditorPane();
        messageArea.setDocument(new HTMLDocument());
        messageArea.setPreferredSize(new Dimension(10, 100));
        JScrollPane sp = new JScrollPane(messageArea);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagePanel.add(messageArea, BorderLayout.CENTER);
        // The message panel section is wildly broken!
        // If the panel is resized the textpane enlarges accordingly and then
        // refuses to automatically shrink while making the enlosing panel
        // smaller.
        // Consider using JSplitPane instead!

        boolean DEFAULT_MESSAGE_PANEL_VISIBILITY = true;
        showMessagesPaneButton.setSelected(DEFAULT_MESSAGE_PANEL_VISIBILITY);
        messagePanel.setVisible(DEFAULT_MESSAGE_PANEL_VISIBILITY);

        buildCenterView();
        pack();
        setMinimumSize(getSize());
        // TODO: revise behavior for dialog size bigger than screen, e.g.
    }

    private void okButtonClicked() {
        // TODO: add handling code
        System.out.println("ok clicked");
    }

    private void cancelButtonClicked() {
        // TODO: add handling code
        System.out.println("cancel clicked");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    PropertyPane pp = new PropertyPane(
                            new PropertyModel(new KNNProperties2()));
                    pp.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    pp.setVisible(true);
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(PropertyPane.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    private void buildCenterView() {
        // TODO: replace with real implementation
        createMockupProperties();

    }

    private void createMockupProperties() {
        // use as a template for real implementation
        for (int i = 0; i < 14; i++) {
            // labelz
            JLabel label = new JLabel("label " + i);
            GridBagConstraints constr = new GridBagConstraints();
            constr.gridx = 0;
            constr.gridy = i;
            constr.anchor = GridBagConstraints.LINE_START;
            mainPanel.add(label, constr);

            // buttonz
            JTextField field = new JTextField("field " + i);
            constr = new GridBagConstraints();
            constr.gridx = 1;
            constr.gridy = i;
            constr.anchor = GridBagConstraints.CENTER;
            mainPanel.add(field, constr);
        }
    }
//    public static void main(String args[]) {
//        for (int i = 0; i < o.length; i++) {
//            Object object = o[i];
//            System.out.println(object.getClass().
//                    getName() + ": " +
//                    mapTypeToGUIComponent(object).
//                    getClass().
//                    getName());
//        }
//    }
    static Object[] o = new Object[]{
        "test",
        1.0d,
        123L
    };
}
