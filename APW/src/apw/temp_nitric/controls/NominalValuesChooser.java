/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Created on 2009-05-04, 20:38:43
 */

package apw.temp_nitric.controls;

import apw.temp_nitric.gui.ComplexCreatorFrame;
import apw.temp_nitric.gui.SelectorForNominalCreatorPanel;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/**
 *
 * @author Nitric
 */
public class NominalValuesChooser extends javax.swing.JFrame {

    private HashSet<String> set;
    private ComplexCreatorFrame parentFrame;
    private SelectorForNominalCreatorPanel parentPanel;

    /***/
    // TODO: USUNĄĆ!!!!
    public JButton getJb_gotowe() {
        return jb_gotowe;
    }

    public void setJb_gotowe(JButton jb_gotowe) {
        this.jb_gotowe = jb_gotowe;
    }

    public JScrollPane getJsp_valuesPanel() {
        return jsp_valuesPanel;
    }

    public void setJsp_valuesPanel(JScrollPane jsp_valuesPanel) {
        this.jsp_valuesPanel = jsp_valuesPanel;
    }

    public NominalValuesChooserPanel getNominalValuesChooserPanel() {
        return nominalValuesChooserPanel;
    }

    public void setNominalValuesChooserPanel(NominalValuesChooserPanel nominalValuesChooserPanel) {
        this.nominalValuesChooserPanel = nominalValuesChooserPanel;
    }

    public ComplexCreatorFrame getParentFrame() {
        return parentFrame;
    }

    public void setParentFrame(ComplexCreatorFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public SelectorForNominalCreatorPanel getParentPanel() {
        return parentPanel;
    }

    public void setParentPanel(SelectorForNominalCreatorPanel parentPanel) {
        this.parentPanel = parentPanel;
    }

    public HashSet<String> getSet() {
        return set;
    }

    public void setSet(HashSet<String> set) {
        this.set = set;
    }

    /***/
    
    public NominalValuesChooser() {
        initComponents();
    }

    public NominalValuesChooser(
            ComplexCreatorFrame pf,
            SelectorForNominalCreatorPanel pp,
            HashSet<String> set,
            String title) {
        
        this.parentFrame = pf;
        this.parentPanel = pp;
        this.set = set;
        myInitComponents();
        setTitle(title);
        setLocationRelativeTo(pf);
    }

    private void myInitComponents() {
        jb_gotowe = new javax.swing.JButton();
        jsp_valuesPanel = new javax.swing.JScrollPane();
        nominalValuesChooserPanel = new apw.temp_nitric.controls.NominalValuesChooserPanel(set);
        jb_selectAll = new javax.swing.JButton();
        jb_deselectAll = new javax.swing.JButton();
        jb_cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JComponent component = set.size() > 5 ?
            jsp_valuesPanel : nominalValuesChooserPanel;
        int height = Math.min(set.size() * 30, 133);                                   // 133

        jb_gotowe.setText("Gotowe");
        jb_gotowe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_gotoweActionPerformed(evt);
            }
        });

        jsp_valuesPanel.setViewportView(nominalValuesChooserPanel);

        jb_selectAll.setText("Wszystkie");
        jb_selectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_selectAllActionPerformed(evt);
            }
        });

        jb_deselectAll.setText("Żadne");
        jb_deselectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_deselectAllActionPerformed(evt);
            }
        });

        jb_cancel.setText("Anuluj");
        jb_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(component, javax.swing.GroupLayout.PREFERRED_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jb_gotowe, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jb_selectAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jb_deselectAll, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jb_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jb_selectAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jb_deselectAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jb_cancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jb_gotowe))
                    .addComponent(component, javax.swing.GroupLayout.PREFERRED_SIZE, height, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jb_gotowe = new javax.swing.JButton();
        jsp_valuesPanel = new javax.swing.JScrollPane();
        nominalValuesChooserPanel = new apw.temp_nitric.controls.NominalValuesChooserPanel(set);
        jb_selectAll = new javax.swing.JButton();
        jb_deselectAll = new javax.swing.JButton();
        jb_cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jb_gotowe.setText("Gotowe");
        jb_gotowe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_gotoweActionPerformed(evt);
            }
        });

        jsp_valuesPanel.setViewportView(nominalValuesChooserPanel);

        jb_selectAll.setText("Wszystkie");
        jb_selectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_selectAllActionPerformed(evt);
            }
        });

        jb_deselectAll.setText("Żadne");
        jb_deselectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_deselectAllActionPerformed(evt);
            }
        });

        jb_cancel.setText("Anuluj");
        jb_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jsp_valuesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jb_gotowe, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jb_selectAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jb_deselectAll, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jb_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jb_selectAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jb_deselectAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jb_cancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jb_gotowe))
                    .addComponent(jsp_valuesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_gotoweActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_gotoweActionPerformed
        HashSet<String> allowedValues = nominalValuesChooserPanel.getSelectedValues();
        if (allowedValues.size() == 0)
            JOptionPane.showMessageDialog(this, "Musi być wybrana co najmniej jedna wartość!");
        else {
            parentPanel.setAllowedValues(nominalValuesChooserPanel.getSelectedValues());
            dispose();
        }
}//GEN-LAST:event_jb_gotoweActionPerformed

    private void jb_selectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_selectAllActionPerformed
        nominalValuesChooserPanel.setAllCheckboxes(true);
    }//GEN-LAST:event_jb_selectAllActionPerformed

    private void jb_deselectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_deselectAllActionPerformed
        nominalValuesChooserPanel.setAllCheckboxes(false);
}//GEN-LAST:event_jb_deselectAllActionPerformed

    private void jb_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cancelActionPerformed
        dispose();
}//GEN-LAST:event_jb_cancelActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NominalValuesChooser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jb_cancel;
    private javax.swing.JButton jb_deselectAll;
    private javax.swing.JButton jb_gotowe;
    private javax.swing.JButton jb_selectAll;
    private javax.swing.JScrollPane jsp_valuesPanel;
    private apw.temp_nitric.controls.NominalValuesChooserPanel nominalValuesChooserPanel;
    // End of variables declaration//GEN-END:variables

}
