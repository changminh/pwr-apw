/*
 * WrongRuleWarning.java
 *
 * Created on 2009-06-19, 16:40:21
 */

package apw.augmentedLearning.gui;

import apw.augmentedLearning.logic.RuleAcquisition;

/**
 *
 * @author Nitric
 */
public class WrongRuleWarning extends javax.swing.JFrame {

    private PreviewOfSample preview;
    private RuleAcquisition parent;

    /** Creates new form WrongRuleWarning */
    public WrongRuleWarning() {
        initComponents();
    }

    public WrongRuleWarning(RuleAcquisition parent, PreviewOfSample preview) {
        this.preview = preview;
        this.parent = parent;
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jb_correct = new javax.swing.JButton();
        jb_skipSample = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Wprowadzona reguła pokrywa negatywne przykłady!");

        jb_correct.setText("Poprawię regułę");
        jb_correct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_correctActionPerformed(evt);
            }
        });

        jb_skipSample.setText("Pomijam bieżący przykład");
        jb_skipSample.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_skipSampleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jb_correct)
                        .addGap(18, 18, 18)
                        .addComponent(jb_skipSample, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_correct)
                    .addComponent(jb_skipSample))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_correctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_correctActionPerformed
        decision(true);
}//GEN-LAST:event_jb_correctActionPerformed

    private void jb_skipSampleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_skipSampleActionPerformed
        decision(false);
}//GEN-LAST:event_jb_skipSampleActionPerformed

    private void decision(boolean b) {
        preview.dispose();
        dispose();
        try {
            parent.ruleCorrection(b);
        }
        catch (InterruptedException ex) { System.err.println("Oj, niedobrze!"); }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jb_correct;
    private javax.swing.JButton jb_skipSample;
    // End of variables declaration//GEN-END:variables

}