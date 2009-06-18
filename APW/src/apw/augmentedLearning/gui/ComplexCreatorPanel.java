/* 
 * Created on 2009-05-21, 10:06:34
 */

package apw.augmentedLearning.gui;

import apw.augmentedLearning.logic.Complex;
import apw.augmentedLearning.logic.DataFile;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

/**
 *
 * @author Nitric
 */
public class ComplexCreatorPanel extends javax.swing.JPanel {

    int amount = 1;
    DataFile dataFile = null;
    private ComplexCreatorFrame parentFrame;
    private SelectorCreatorPanel[] panels;

    public ComplexCreatorPanel() {
        initPanels();
    }

    public ComplexCreatorPanel(ComplexCreatorFrame parentFrame, DataFile dataFile) {
        this.dataFile = dataFile;
        amount = dataFile.getAttributesCount();
        initPanels();
    }

    public Complex getComplex() {
        Complex complex = new Complex();
        for (SelectorCreatorPanel panel : panels) {
            complex.addSelector(panel.getSelector());
        }
        return complex;
    }

    public void setSample(int sample) {
        Object[] objects = dataFile.getRawObjects()[sample];
        for (int i = 0; i < dataFile.getAttributesCount(); i++) {
            if (objects[i] == null)
                panels[i].setEnabled(false);
        }
        SelectorForNominalCreatorPanel panel = (SelectorForNominalCreatorPanel) panels[dataFile.getClassAttributeIndex()];
        panel.setSingleValue(((String) objects[dataFile.getClassAttributeIndex()]));
        panel.setEnabled(false);
    }

    private void initPanels() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        ParallelGroup pg1;
        SequentialGroup sg1, sg2;
        Vector<Integer> nominals = null;
        ArrayList<String> names = null;

        if (dataFile != null) {
            nominals = dataFile.getNominals();
            names = dataFile.getAttributesNames();
        }

        /***** Horizontal *****/
        pg1 = layout.createParallelGroup(Alignment.LEADING);
        panels = new SelectorCreatorPanel[amount];

        for (int i = 0; i < amount; i++) {
            if (dataFile != null) {
                if (nominals.contains(i))
                    panels[i] = 
                            new SelectorForNominalCreatorPanel(
                            i,
                            names.get(i),
                            dataFile.getNominalValuesOfAttribute(i),
                            parentFrame);
                else
                    panels[i] = new SelectorForNumberCreatorPanel(i, names.get(i));
            }
            else
                panels[i] = new SelectorForNumberCreatorPanel(i, "Atr " + i);
            pg1.addComponent(
                    panels[i],
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE
                );
        }

        sg1 = layout.createSequentialGroup();
        sg1.addContainerGap()
           .addGroup(pg1)
           .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(sg1)
                );

        /* javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                   .addComponent(selectorForNominalCreatorPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                   .addComponent(selectorForNominalCreatorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                   .addComponent(selectorForNominalCreatorPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                   .addComponent(selectorForNominalCreatorPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                 .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        ); */

        /***** Vertical *****/

        sg2 = layout.createSequentialGroup();
        sg2.addContainerGap();
        for (int i = 0; i < amount - 1; i++) {
            sg2.addComponent(
                    panels[i],
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE
                );
            sg2.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
        }
        sg2.addComponent(panels[amount - 1],
                javax.swing.GroupLayout.PREFERRED_SIZE,
                javax.swing.GroupLayout.DEFAULT_SIZE,
                javax.swing.GroupLayout.PREFERRED_SIZE
            );
        sg2.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(sg2));


        /* layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectorForNominalCreatorPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectorForNominalCreatorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectorForNominalCreatorPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectorForNominalCreatorPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        ); */
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
