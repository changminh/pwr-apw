/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ComplexCreatorFrame.java
 *
 * Created on 2009-05-04, 12:11:51
 */

package apw.augmentedLearning.gui;

import apw.augmentedLearning.logic.Complex;
import javax.swing.JComponent;
import apw.augmentedLearning.logic.DataFile;
import apw.augmentedLearning.logic.RuleAcquisition;

/**
 *
 * @author Nitric
 */
public class ComplexCreatorFrame extends javax.swing.JFrame {

    private DataFile dataFile;
    private boolean conclusionMode = false;          // rule = if (assumption) then (conclusion)
    private LoadingSamples_Step3 parent = null;
    private RuleAcquisition acqusitor;
    private int currentSample;

    public ComplexCreatorFrame() {
        myInitComponents(false);
    }

    public Complex getComplex() {
        return complexCreatorPanel.getComplex();
    }

    public ComplexCreatorFrame(DataFile dataFile, boolean conclusionMode, LoadingSamples_Step3 parent) {
        this.dataFile = dataFile;
        this.conclusionMode = conclusionMode;
        this.parent = parent;
        myInitComponents(conclusionMode);
        setLocationRelativeTo(parent);
    }

    public ComplexCreatorFrame(DataFile dataFile, int currentSample, RuleAcquisition parent) {
        this.dataFile = dataFile;
        this.conclusionMode = false;
        this.acqusitor = parent;
        this.currentSample = currentSample;
        myInitComponents(conclusionMode);
        setLocationRelativeTo(null);
        // Additional operations
        jb_nextComplex.setEnabled(false);
        jb_ok.setText("Gotowe");
        complexCreatorPanel.setSample(currentSample);
    }

    private void myInitComponents(boolean conclusionMode) {
        jsp_ccpScrollPane = new javax.swing.JScrollPane();
        complexCreatorPanel = dataFile != null ?
            new apw.augmentedLearning.gui.ComplexCreatorPanel(this, dataFile) : new apw.augmentedLearning.gui.ComplexCreatorPanel();
        jl_title = new javax.swing.JLabel();
        jb_ok = new javax.swing.JButton();
        jb_nextComplex = new javax.swing.JButton();

        JComponent component = complexCreatorPanel.amount > 6 ?
            jsp_ccpScrollPane : complexCreatorPanel;

        int height = complexCreatorPanel.amount > 6 ? 236 : 40 * complexCreatorPanel.amount;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jsp_ccpScrollPane.setViewportView(complexCreatorPanel);

        jl_title.setFont(new java.awt.Font("Tahoma", 1, 14));
        jl_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        if (!conclusionMode) {
            jb_nextComplex.setText("Następny kompleks");
            jb_ok.setText("Przejdź do części przesłankowej");
            jl_title.setText("Wskaż dozwolone wartości atrybutów dla kompleksu:");
        }
        else {
            jb_nextComplex.setVisible(false);
            jb_ok.setText("Gotowe");
            jl_title.setText("Wskaż klasę dla krotek pokrytych wprowadzoną regułą:");
        }
        jb_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_okActionPerformed(evt);
            }
        });
        jb_nextComplex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_nextComplexActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(component, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                    .addComponent(jl_title, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jb_nextComplex)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jb_ok)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jl_title, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(component, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_ok)
                    .addComponent(jb_nextComplex))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jsp_ccpScrollPane = new javax.swing.JScrollPane();
        complexCreatorPanel = new apw.augmentedLearning.gui.ComplexCreatorPanel(null, dataFile);
        jl_title = new javax.swing.JLabel();
        jb_ok = new javax.swing.JButton();
        jb_nextComplex = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jsp_ccpScrollPane.setViewportView(complexCreatorPanel);

        jl_title.setFont(new java.awt.Font("Tahoma", 1, 14));
        jl_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_title.setText("Wskaż dozwolone wartości atrybutów (kompleks) części przesłankowej:");

        jb_ok.setText("Przejdź do części przesłankowej");
        jb_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_okActionPerformed(evt);
            }
        });

        jb_nextComplex.setText("Jeszcze jeden kompleks");
        jb_nextComplex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_nextComplexActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsp_ccpScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                    .addComponent(jl_title, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jb_nextComplex)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jb_ok)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jl_title, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jsp_ccpScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_ok)
                    .addComponent(jb_nextComplex))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_okActionPerformed
        // parent == null -> means that inserted rule is for particular sample with nulls
        if (parent != null)
            parent.addComplex(getComplex(), true, conclusionMode);
        else {
            acqusitor.addComplex(getComplex(), currentSample);
            dispose();
        }
}//GEN-LAST:event_jb_okActionPerformed

    private void jb_nextComplexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_nextComplexActionPerformed
        // parent == null -> means that inserted rule is for particular sample with nulls
        if (parent != null)
            parent.addComplex(getComplex(), conclusionMode, false);
        else {
            acqusitor.addComplex(getComplex(), currentSample);
            dispose();
        }
    }//GEN-LAST:event_jb_nextComplexActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ComplexCreatorFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private apw.augmentedLearning.gui.ComplexCreatorPanel complexCreatorPanel;
    private javax.swing.JButton jb_nextComplex;
    private javax.swing.JButton jb_ok;
    private javax.swing.JLabel jl_title;
    private javax.swing.JScrollPane jsp_ccpScrollPane;
    // End of variables declaration//GEN-END:variables

}
