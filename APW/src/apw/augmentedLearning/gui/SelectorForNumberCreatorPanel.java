/*
 * SFNum.java
 *
 * Created on 2009-05-14, 23:39:09
 */

package apw.augmentedLearning.gui;

import apw.augmentedLearning.logic.SelectorForNumber;
import apw.augmentedLearning.logic.SelectorTypeForNumbers;
import static java.lang.Double.*;

/**
 *
 * @author Nitric
 */
public class SelectorForNumberCreatorPanel extends SelectorCreatorPanel {

    private String attributesName = "NoName";
    private int attributesNumber;
    private static boolean t = true;
    private static boolean f = false;
    
    public SelectorForNumberCreatorPanel() {
        initComponents();
        jcb_negation.setVisible(t);
    }

    public SelectorForNumberCreatorPanel(int attributesNumber, String attributesName) {
        this.attributesName = attributesName;
        this.attributesNumber = attributesNumber;
        initComponents();
        setControls(f, f, f, f);
        jl_operator.setText("");
        jl_attributeName.setText(attributesName);
    }


    @Override
    public void setEnabled(boolean b) {
        jcb_operator.setEnabled(b);
    }

    public SelectorForNumber getSelector() throws NumberFormatException {
        switch((SelectorTypeForNumbers)jcb_operator.getSelectedItem()) {
            case ALL_VALUES:
                return SelectorForNumber.getUniversalSelector(attributesNumber);
            case BELONGS_RIGHT_INCLUDING:
                return SelectorForNumber.getSelBelongs(
                            attributesNumber,
                            parseDouble(jtf_lower.getText()),
                            parseDouble(jtf_upper.getText())
                       );
            case EQUAL:
                return SelectorForNumber.getSelEQ(
                            attributesNumber,
                            parseDouble(jtf_upper.getText())
                       );
            /* case GREATER_OR_EQUAL:
                return SelectorForNumber.getSelGE(
                            attributesNumber,
                            jcb_negation.isSelected(),
                            parseDouble(jtf_upper.getText())
                       ); */
            case GREATER_THAN:
                return SelectorForNumber.getSelGT(
                            attributesNumber,
                            parseDouble(jtf_upper.getText())
                       );
            case LOWER_OR_EQUAL:
                return SelectorForNumber.getSelLE(
                            attributesNumber,
                            parseDouble(jtf_upper.getText())
                       );
            /* case LOWER_THAN:
                return SelectorForNumber.getSelLT(
                            attributesNumber,
                            jcb_negation.isSelected(),
                            parseDouble(jtf_upper.getText())); */
            case NONE_VALUE:
                return SelectorForNumber.getEmptySelector(attributesNumber);
        }
        return null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jcb_negation = new javax.swing.JCheckBox();
        jl_attributeName = new javax.swing.JLabel();
        jcb_operator = new apw.augmentedLearning.controls.ComboSelectorForNumber();
        jtf_lower = new javax.swing.JTextField();
        jl_operator = new javax.swing.JLabel();
        jtf_upper = new javax.swing.JTextField();

        jcb_negation.setText("Negacja");
        jcb_negation.setOpaque(false);
        jcb_negation.setEnabled(false);

        jl_attributeName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jl_attributeName.setText(attributesName);

        jcb_operator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcb_operatorActionPerformed(evt);
            }
        });

        jl_operator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_operator.setText("operator");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcb_negation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jl_attributeName, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcb_operator, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtf_lower, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jl_operator, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtf_upper, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jcb_negation, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addComponent(jl_attributeName)
                .addComponent(jcb_operator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jtf_lower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jl_operator)
                .addComponent(jtf_upper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jcb_operatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcb_operatorActionPerformed
        switch((SelectorTypeForNumbers)jcb_operator.getSelectedItem()) {
            case ALL_VALUES:
                setControls(f, f, f, f);
                jl_operator.setText("");
                break;
            case BELONGS_RIGHT_INCLUDING:
                setControls(t, t, t, t);
                jl_operator.setText("< " + attributesName + " <=");
                jcb_negation.setEnabled(t);
                break;
            case EQUAL:
                setControls(f, f, t, t);
                jl_operator.setText(attributesName + " =");
                break;
            /* case GREATER_OR_EQUAL:
                setControls(f, f, t, t);
                jl_operator.setText(attributesName + ">= ");
                break; */
            case GREATER_THAN:
                setControls(f, f, t, t);
                jl_operator.setText(attributesName + " >");
                break;
            case LOWER_OR_EQUAL:
                setControls(f, f, t, t);
                jl_operator.setText(attributesName + " <=");
                break;
            /* case LOWER_THAN:
                setControls(f, f, t, t);
                jl_operator.setText(attributesName + " <");
                break; */
            case NONE_VALUE:
                setControls(f, f, f, f);
                jl_operator.setText("");
                break;
        }
    }//GEN-LAST:event_jcb_operatorActionPerformed


    private void setControls(boolean lv, boolean le, boolean uv, boolean ue) {
        jtf_lower.setVisible(lv);
        jtf_upper.setVisible(uv);
        jtf_lower.setEnabled(le);
        jtf_upper.setEnabled(ue);
        jcb_negation.setEnabled(false);
        jcb_negation.setSelected(false);
        jcb_negation.setEnabled(false);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jcb_negation;
    private apw.augmentedLearning.controls.ComboSelectorForNumber jcb_operator;
    private javax.swing.JLabel jl_attributeName;
    private javax.swing.JLabel jl_operator;
    private javax.swing.JTextField jtf_lower;
    private javax.swing.JTextField jtf_upper;
    // End of variables declaration//GEN-END:variables

}