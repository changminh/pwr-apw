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

/*
 * MainFrame.java
 *
 * Created on 2009-06-22, 00:36:01
 */
package apw.gui;

import apw.classifiers.Classifier;
import apw.classifiers.SVMClassifier;
import apw.classifiers.c4_5.C4_5;
import apw.classifiers.fuzzyRuleClassifier.FuzzyRuleClassifier;
import apw.classifiers.id3.ID3;
import apw.classifiers.knn.KNN;
import apw.core.Evaluator;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.awt.Component;
import java.awt.FileDialog;
import java.io.File;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class MainFrame extends javax.swing.JFrame {

    private JRadioButton last;

    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();
        sampleUpdate();
        classifierUpdate();

    }

    private void classifierUpdate() {
        evalBtn.setEnabled(classifier != null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton1 = new javax.swing.JButton();
        KNN_RB = new javax.swing.JRadioButton();
        ID3_RB = new javax.swing.JRadioButton();
        C4_5_RB = new javax.swing.JRadioButton();
        SVM_RB = new javax.swing.JRadioButton();
        evalBtn = new javax.swing.JButton();
        Bag_RB = new javax.swing.JRadioButton();
        Boost_RB = new javax.swing.JRadioButton();
        Fuzzy_RB = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Load Samples");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(KNN_RB);
        KNN_RB.setText("KNN");
        KNN_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KNN_RBActionPerformed(evt);
            }
        });

        buttonGroup1.add(ID3_RB);
        ID3_RB.setText("ID3");
        ID3_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ID3_RBActionPerformed(evt);
            }
        });

        buttonGroup1.add(C4_5_RB);
        C4_5_RB.setText("C4.5");
        C4_5_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                C4_5_RBActionPerformed(evt);
            }
        });

        buttonGroup1.add(SVM_RB);
        SVM_RB.setText("SVM");
        SVM_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SVM_RBActionPerformed(evt);
            }
        });

        evalBtn.setText("Evaluate");
        evalBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evalBtnActionPerformed(evt);
            }
        });

        buttonGroup1.add(Bag_RB);
        Bag_RB.setText("bag");
        Bag_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Bag_RBActionPerformed(evt);
            }
        });

        buttonGroup1.add(Boost_RB);
        Boost_RB.setText("boost");
        Boost_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boost_RBActionPerformed(evt);
            }
        });

        buttonGroup1.add(Fuzzy_RB);
        Fuzzy_RB.setText("Fazi");
        Fuzzy_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Fuzzy_RBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Bag_RB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Boost_RB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Fuzzy_RB))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(KNN_RB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ID3_RB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(C4_5_RB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(SVM_RB))
                    .addComponent(jButton1)
                    .addComponent(evalBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(KNN_RB)
                    .addComponent(ID3_RB)
                    .addComponent(C4_5_RB)
                    .addComponent(SVM_RB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Bag_RB)
                    .addComponent(Boost_RB)
                    .addComponent(Fuzzy_RB))
                .addGap(5, 5, 5)
                .addComponent(evalBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            FileDialog fd = new FileDialog(this);
            fd.setDirectory(fd.getDirectory() + "/data");
            fd.setVisible(true);
            if (fd.getFile() != null)
                s = new ARFFLoader(new File(fd.getDirectory() + "/" + fd.getFile())).getSamples();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString(),
                    "An exception occured", JOptionPane.ERROR_MESSAGE);
        }
        sampleUpdate();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void KNN_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KNN_RBActionPerformed
        try {
            classifier = new KNN(s, neighbours, KNN.RANKING_BASED_VOTING);
            last = (JRadioButton) evt.getSource();
        } catch (Exception e) {
            warn(e.getMessage());
            if (last != null)
                last.setSelected(true);
        }
        classifierUpdate();
}//GEN-LAST:event_KNN_RBActionPerformed

    private void ID3_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ID3_RBActionPerformed
        try {
            classifier = new ID3(s);
            last = (JRadioButton) evt.getSource();
        } catch (Exception e) {
            warn(e.getMessage());
            if (last != null)
                last.setSelected(true);
        }
        classifierUpdate();
}//GEN-LAST:event_ID3_RBActionPerformed

    private void evalBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evalBtnActionPerformed
        try {
            if (classifier == null)
                warn("Classifier not specified.");
            if (s == null)
                warn("Samples not loaded.");
            if (s != null & classifier != null) {
                Evaluator e = new Evaluator(classifier, s);
                ResultPanel.showResultDialog(e, this,
                        classifier.getClass().getCanonicalName() + " classifying \"" + s.getName() + "\"");
            }
        } catch (Exception e) {
            warn(e.toString());
            e.printStackTrace();
        }
}//GEN-LAST:event_evalBtnActionPerformed

    private void Bag_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Bag_RBActionPerformed
        warn("Not yet implemented");
        if (last != null)
            last.setSelected(true);
        classifierUpdate();
}//GEN-LAST:event_Bag_RBActionPerformed

    private void Boost_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boost_RBActionPerformed
        warn("Not yet implemented");
        if (last != null)
            last.setSelected(true);
        classifierUpdate();
}//GEN-LAST:event_Boost_RBActionPerformed

    private void Fuzzy_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Fuzzy_RBActionPerformed
        try {
            classifier = new FuzzyRuleClassifier(s);
            last = (JRadioButton) evt.getSource();
        } catch (Exception e) {
            warn(e.getMessage());
            if (last != null)
                last.setSelected(true);
        }
        classifierUpdate();
}//GEN-LAST:event_Fuzzy_RBActionPerformed

    private void C4_5_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_C4_5_RBActionPerformed
        try {
            classifier = new C4_5(s);
            last = (JRadioButton) evt.getSource();
        } catch (Exception e) {
            warn(e.getMessage());
            if (last != null)
                last.setSelected(true);
        }
        classifierUpdate();
}//GEN-LAST:event_C4_5_RBActionPerformed

    private void SVM_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SVM_RBActionPerformed
        try {
            classifier = new SVMClassifier(s);
            last = (JRadioButton) evt.getSource();
        } catch (Exception e) {
            warn(e.getMessage());
            if (last != null)
                last.setSelected(true);
        }
        classifierUpdate();
    }//GEN-LAST:event_SVM_RBActionPerformed
    Samples s;
    int neighbours = 4;
    Classifier classifier = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new MainFrame().setVisible(true);
                }
            });
        } catch (Exception ex) {
            warn(null, "Troubles setting LAF");
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sampleUpdate() {
        for (AbstractButton a : Collections.list(buttonGroup1.getElements()))
            a.setEnabled(s != null);
        if(s==null) setTitle("Classifier tester.");
        else setTitle("Classifier tester. Ralation \"" + s.getName() + "\"");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Bag_RB;
    private javax.swing.JRadioButton Boost_RB;
    private javax.swing.JRadioButton C4_5_RB;
    private javax.swing.JRadioButton Fuzzy_RB;
    private javax.swing.JRadioButton ID3_RB;
    private javax.swing.JRadioButton KNN_RB;
    private javax.swing.JRadioButton SVM_RB;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton evalBtn;
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables

    private static void warn(Component c, String string) {
        JOptionPane.showMessageDialog(c, string, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    private void warn(String string) {
        JOptionPane.showMessageDialog(this, string, "Exception", JOptionPane.ERROR_MESSAGE);
    }
}