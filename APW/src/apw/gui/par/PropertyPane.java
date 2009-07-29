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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class PropertyPane extends JDialog {

    JOptionPane test;
    BorderLayout borderLayout;
    JScrollPane scrollPane;
    JPanel bottomPanel;
    JPanel mainPanel;
    JButton okButton;
    JButton cancelButton;

    public PropertyPane() {
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

        bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(okButton);
        bottomPanel.add(cancelButton);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        getContentPane().add(new JScrollPane(mainPanel), BorderLayout.CENTER);

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

//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            java.awt.EventQueue.invokeLater(new Runnable() {
//
//                public void run() {
//                    PropertyPane pp = new PropertyPane();
//                    pp.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//                    pp.setVisible(true);
//                }
//            });
//        } catch (Exception ex) {
//            Logger.getLogger(PropertyPane.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
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

    public static void main(String args[]) {
        for (int i = 0; i < o.length; i++) {
            Object object = o[i];
            System.out.println(object.getClass().getName() + ": " +
                    mapTypeToGUIComponent(object).getClass().getName());
        }
    }
    static Object[] o = new Object[]{
        "test",
        1.0d,
        123L
    };


    /********* Dynamic type to component mapping section ahead ************/

    private interface TypeToGUIHandler {
        // TODO: the JComponent could be replaced by custom class
        // handling validation and etc

        public JComponent getComponentForType();
    }
    private static Map<Class, TypeToGUIHandler> typeMap = new HashMap();

    static {
        typeMap.put(Double.class, new TypeToGUIHandler() {

            public JComponent getComponentForType() {
                return new JSpinner();
            }
        });

        typeMap.put(Integer.class, new TypeToGUIHandler() {

            public JComponent getComponentForType() {
                return new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            }
        });
        
        typeMap.put(String.class, new TypeToGUIHandler() {

            public JComponent getComponentForType() {
                return new JTextField("Text Field");
            }
        });
    }

    private static JComponent mapTypeToGUIComponent(Object o) {
        // TODO: Change fallback value for something relevant
        JComponent comp = new JLabel("Default Type label");
        if (typeMap.containsKey(o.getClass()))
            comp = typeMap.get(o.getClass()).getComponentForType();
        return comp;
    }
}
