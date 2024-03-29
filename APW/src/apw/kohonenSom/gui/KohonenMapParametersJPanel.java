package apw.kohonenSom.gui;

import apw.kohonenSom.gui.KohonenMapJFrame;
import apw.kohonenSom.KohonenNetwork;
import javax.swing.SpinnerNumberModel;

public class KohonenMapParametersJPanel extends javax.swing.JPanel {

    private KohonenNetwork creator;
    private KohonenMapJFrame parent;

    public KohonenMapParametersJPanel(KohonenNetwork creator, KohonenMapJFrame parent) {
        initComponents();

        this.creator = creator;
        this.parent = parent;

        maxR_js.setModel(new SpinnerNumberModel(0, 0, 100, 1));
        maxR_js.setValue(5);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        neighbourhoodType = new javax.swing.ButtonGroup();
        distanceType = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        alfaMax_jtf = new javax.swing.JTextField();
        maxR_js = new javax.swing.JSpinner();
        minR_js = new javax.swing.JSpinner();
        maxT_js = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        maxX_js = new javax.swing.JSpinner();
        maxY_js = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        rectNeighType_jrb = new javax.swing.JRadioButton();
        gaussNeighType_jrb = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        euclDistType_jrb = new javax.swing.JRadioButton();
        skalDistType_jrb = new javax.swing.JRadioButton();
        manhDistType_jrb = new javax.swing.JRadioButton();
        lDistType_jrb = new javax.swing.JRadioButton();
        cancel_jb = new javax.swing.JButton();
        create_jb = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tworzenie sieci: Parametry mapy Kohonena");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Wspołczynniki uczenia"));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Początkowy współczynnik uczenia (Alfa max):");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Maksymalny promień sąsiedztwa (R max):");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Minimalny promień sąsiedztwa (R min):");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Liczba epok trenowania (T max):");
        jLabel8.setDoubleBuffered(true);

        alfaMax_jtf.setText("0.7");
        alfaMax_jtf.setName("alfaMaxField"); // NOI18N

        maxR_js.setName("rMax"); // NOI18N
        maxR_js.setValue(10);

        minR_js.setName("rMin"); // NOI18N
        minR_js.setValue(2);

        maxT_js.setName("tMax"); // NOI18N
        maxT_js.setValue(1000);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                            .addGap(5, 5, 5))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                            .addGap(5, 5, 5)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(3, 3, 3)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(alfaMax_jtf, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(maxT_js, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(minR_js, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(maxR_js, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(alfaMax_jtf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(minR_js, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(maxT_js, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(maxR_js, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Parametry mapy"));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Szerokość mapy (X max):");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Wysokość mapy (Y max):");

        maxX_js.setName("xMax"); // NOI18N
        maxX_js.setValue(30);

        maxY_js.setName("yMax"); // NOI18N
        maxY_js.setValue(30);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(maxY_js)
                    .addComponent(maxX_js, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(maxX_js, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(maxY_js, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Rodzaj sąsiedztwa"));

        neighbourhoodType.add(rectNeighType_jrb);
        rectNeighType_jrb.setText("Prostokatny");
        rectNeighType_jrb.setName("rectNeigh"); // NOI18N
        rectNeighType_jrb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rectNeighType_jrbActionPerformed(evt);
            }
        });

        neighbourhoodType.add(gaussNeighType_jrb);
        gaussNeighType_jrb.setText("Gaussowski");
        gaussNeighType_jrb.setName("gaussNeigh"); // NOI18N
        gaussNeighType_jrb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gaussNeighType_jrbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rectNeighType_jrb)
                    .addComponent(gaussNeighType_jrb))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rectNeighType_jrb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(gaussNeighType_jrb)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Rodzaj odległości"));

        distanceType.add(euclDistType_jrb);
        euclDistType_jrb.setText("Euklidesowa");
        euclDistType_jrb.setName("euclDist"); // NOI18N
        euclDistType_jrb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                euclDistType_jrbActionPerformed(evt);
            }
        });

        distanceType.add(skalDistType_jrb);
        skalDistType_jrb.setText("Skalarna");
        skalDistType_jrb.setName("skalDist"); // NOI18N
        skalDistType_jrb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skalDistType_jrbActionPerformed(evt);
            }
        });

        distanceType.add(manhDistType_jrb);
        manhDistType_jrb.setText("Manhattan");
        manhDistType_jrb.setName("manhDist"); // NOI18N
        manhDistType_jrb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manhDistType_jrbActionPerformed(evt);
            }
        });

        distanceType.add(lDistType_jrb);
        lDistType_jrb.setText("L");
        lDistType_jrb.setName("lDist"); // NOI18N
        lDistType_jrb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lDistType_jrbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(euclDistType_jrb)
                    .addComponent(manhDistType_jrb)
                    .addComponent(skalDistType_jrb)
                    .addComponent(lDistType_jrb))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(euclDistType_jrb)
                .addGap(3, 3, 3)
                .addComponent(skalDistType_jrb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(manhDistType_jrb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lDistType_jrb)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        cancel_jb.setText("Anuluj");
        cancel_jb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_jbActionPerformed(evt);
            }
        });

        create_jb.setText("Dalej");
        create_jb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_jbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cancel_jb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(create_jb, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(12, 12, 12))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancel_jb)
                    .addComponent(create_jb))
                .addContainerGap(16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rectNeighType_jrbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rectNeighType_jrbActionPerformed
        //TODO
}//GEN-LAST:event_rectNeighType_jrbActionPerformed

    private void gaussNeighType_jrbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gaussNeighType_jrbActionPerformed
        //TODO
}//GEN-LAST:event_gaussNeighType_jrbActionPerformed

    private void euclDistType_jrbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_euclDistType_jrbActionPerformed
        //TODO
}//GEN-LAST:event_euclDistType_jrbActionPerformed

    private void skalDistType_jrbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skalDistType_jrbActionPerformed
        //TODO
}//GEN-LAST:event_skalDistType_jrbActionPerformed

    private void manhDistType_jrbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manhDistType_jrbActionPerformed
        //TODO
}//GEN-LAST:event_manhDistType_jrbActionPerformed

    private void lDistType_jrbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lDistType_jrbActionPerformed
        //TODO
}//GEN-LAST:event_lDistType_jrbActionPerformed

    private void cancel_jbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_jbActionPerformed
        //TODO
}//GEN-LAST:event_cancel_jbActionPerformed

    private void create_jbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_jbActionPerformed
        int maxR = (Integer)(this.maxR_js.getValue());
        int minR = (Integer)(this.minR_js.getValue());
        int maxX = (Integer)(this.maxX_js.getValue());
        int maxY = (Integer)(this.maxY_js.getValue());
        int maxT = (Integer)(this.maxT_js.getValue());
        
        //TODO

        //TODO

        // parent.parametersSet();
    }//GEN-LAST:event_create_jbActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField alfaMax_jtf;
    private javax.swing.JButton cancel_jb;
    private javax.swing.JButton create_jb;
    private javax.swing.ButtonGroup distanceType;
    private javax.swing.JRadioButton euclDistType_jrb;
    private javax.swing.JRadioButton gaussNeighType_jrb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton lDistType_jrb;
    private javax.swing.JRadioButton manhDistType_jrb;
    private javax.swing.JSpinner maxR_js;
    private javax.swing.JSpinner maxT_js;
    private javax.swing.JSpinner maxX_js;
    private javax.swing.JSpinner maxY_js;
    private javax.swing.JSpinner minR_js;
    private javax.swing.ButtonGroup neighbourhoodType;
    private javax.swing.JRadioButton rectNeighType_jrb;
    private javax.swing.JRadioButton skalDistType_jrb;
    // End of variables declaration//GEN-END:variables

}
