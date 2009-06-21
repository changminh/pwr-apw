/*
 * SampleInputFrame.java
 *
 * Created on 2009-06-20, 21:10:46
 */

package apw.augmentedLearning.gui;

import apw.augmentedLearning.logic.*;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import static javax.swing.GroupLayout.*;

/**
 *
 * @author Nitric
 */
public class SampleInputFrame extends javax.swing.JFrame {

    private DataFile dataFile;
    private LoadingSamplesMain advisor;

    /** Creates new form SampleInputFrame */
    public SampleInputFrame() {
        myInitComponents();
    }
    
    public SampleInputFrame(LoadingSamplesMain advisor) {
        this.advisor = advisor;
        this.dataFile = advisor.getDataFile();
        myInitComponents();
        setLocationRelativeTo(null);
    }

    private void myInitComponents() {
        jsp = new javax.swing.JScrollPane();
        sampleInputPanel = new apw.augmentedLearning.gui.SampleInputPanel(dataFile);
        jb_exit = new javax.swing.JButton();
        jb_ok = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jsp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setViewportView(sampleInputPanel);

        jb_exit.setText("Zakończ");
        jb_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_exitActionPerformed(evt);
            }
        });

        jb_ok.setText("OK");
        jb_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_okActionPerformed(evt);
            }
        });

        // We must decide if we need the scroll pane
        JComponent component;
        int heightOfPanel = 44 * dataFile.getAttributesCount();
        component = heightOfPanel > 440 ? jsp : sampleInputPanel;

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(component, javax.swing.GroupLayout.Alignment.LEADING, DEFAULT_SIZE, 451, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jb_ok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jb_exit)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(component, javax.swing.GroupLayout.DEFAULT_SIZE, heightOfPanel, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_exit)
                    .addComponent(jb_ok))
                .addContainerGap())
        );
        pack();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp = new javax.swing.JScrollPane();
        sampleInputPanel = new apw.augmentedLearning.gui.SampleInputPanel();
        jb_exit = new javax.swing.JButton();
        jb_ok = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jsp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setViewportView(sampleInputPanel);

        jb_exit.setText("Zakończ");
        jb_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_exitActionPerformed(evt);
            }
        });

        jb_ok.setText("OK");
        jb_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_okActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jsp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jb_ok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jb_exit)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jsp, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_exit)
                    .addComponent(jb_ok))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_okActionPerformed
        int i = 0;
        Object result[] = new Object[dataFile.getAttributesCount()];
        try {
            for (i = 0; i < dataFile.getAttributesCount(); i++)
                if (i == dataFile.getClassAttributeIndex())
                    result[i] = null;
                else
                    result[i] = sampleInputPanel.getValue(i);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Błędna wartość dla atrybutu nr " + (i + 1));
            return;
        }
        advisor.classify(result);
    }//GEN-LAST:event_jb_okActionPerformed

    private void jb_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_exitActionPerformed

    }//GEN-LAST:event_jb_exitActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SampleInputFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jb_exit;
    private javax.swing.JButton jb_ok;
    private javax.swing.JScrollPane jsp;
    private apw.augmentedLearning.gui.SampleInputPanel sampleInputPanel;
    // End of variables declaration//GEN-END:variables

}
