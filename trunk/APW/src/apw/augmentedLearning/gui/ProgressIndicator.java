/*
 * ProgresIndicator.java
 *
 * Created on 2009-05-25, 23:48:47
 */

package apw.augmentedLearning.gui;

import javax.swing.JFrame;

/**
 *
 * @author Nitric
 */
public class ProgressIndicator extends javax.swing.JFrame {
    private int max;
    /** Creates new form ProgresIndicator */
    public ProgressIndicator() {
        initComponents();
    }

    public ProgressIndicator(final int max, final JFrame parent) {
        initComponents();
        ProgressIndicator.this.max = max;
        setLocationRelativeTo(parent);
        jpb_progres.setMaximum(max);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jl_tytul = new javax.swing.JLabel();
        jpb_progres = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jl_tytul.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jl_tytul.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_tytul.setText("Proszę czekać, trwa pozyskiwanie reguł...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpb_progres, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                    .addComponent(jl_tytul, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jl_tytul)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpb_progres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProgressIndicator().setVisible(true);
            }
        });
    }

    public void setProgress(int seedsLeft) {
        try {
            jpb_progres.setValue(max - seedsLeft);
        }
        catch(Exception e) { }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jl_tytul;
    private javax.swing.JProgressBar jpb_progres;
    // End of variables declaration//GEN-END:variables

}