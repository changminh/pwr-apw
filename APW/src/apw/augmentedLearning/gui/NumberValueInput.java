/*
 * NumberValueInput.java
 *
 * Created on 2009-06-20, 18:30:23
 */

package apw.augmentedLearning.gui;

/**
 *
 * @author Nitric
 */
public class NumberValueInput extends ValueInputPanel {

    /** Creates new form NumberValueInput */
    public NumberValueInput() {
        initComponents();
    }

    public NumberValueInput(String attributeName) throws NumberFormatException {
        System.out.println("attributeName = " + attributeName);
        initComponents();
        jl_attName.setText(attributeName);
        jtf_attValue.setText("");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtf_attValue = new javax.swing.JTextField();
        jl_attName = new javax.swing.JLabel();

        jl_attName.setText("Nazwa atrybutu:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jl_attName, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jtf_attValue, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtf_attValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jl_attName, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jl_attName;
    private javax.swing.JTextField jtf_attValue;
    // End of variables declaration//GEN-END:variables

    @Override
    public Object getValue() throws NumberFormatException {
        String s;
        if ((s = jtf_attValue.getText()).equals(""))
            return null;
        else
            return Double.parseDouble(s);
    }
}
