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
 * ResultPanel.java
 *
 * Created on 2009-06-21, 18:42:30
 */
package apw.gui;

import apw.classifiers.ClassifierTest;
import apw.classifiers.knn.KNN;
import apw.core.Evaluator;
import apw.core.Evaluator.Measures;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.util.Enumeration;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class ResultPanel extends javax.swing.JPanel {

    Evaluator evaluator;

    public TableModel getConfusionMatrixTableModel() {
        if (evaluator == null)
            return null;

        return new DefaultTableModel() {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0)
                    return String.class;
                else
                    return Integer.class;
            }

            @Override
            public int getColumnCount() {
                return evaluator.classes.length + 1;
            }

            @Override
            public int getRowCount() {
                return evaluator.classes.length;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public String getColumnName(int column) {
                if (column == 0)
                    return "Classified as =>";
                return evaluator.classes[column - 1].className;
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (column == 0)
                    return evaluator.classes[row].className;
                else {
                    column--;
                    return Integer.valueOf(evaluator.confMtx[row][column]);
                }
            }
        };
    }

    private static double round(double value, int n) {
        double mul = Math.pow(10, n);
        double multpd = value * mul;
        int rnd = ((int) Math.round(multpd));
        value = (double) rnd / mul;
        return value;
    }
    public static final int NUMBER_OF_DIGITS_AFTER_PERIOD = 3;
    public static final int DEFAULT_MARGIN_WIDTH = 2;

    public TableModel getMeasuresMatrixTableModel() {
        if (evaluator == null)
            return null;
        return new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public int getColumnCount() {
                return 10;
            }

            @Override
            public int getRowCount() {
                return evaluator.classes.length + 1;
            }

            @Override
            public Object getValueAt(int row, int column) {
                Measures m;
                if (row == evaluator.classes.length) {
                    if (column == 0)
                        return "<html><i>weighted";
                    if (column == 1)
                        return "—";
                    m = evaluator.weighted;
                } else
                    m = evaluator.classes[row];
                Object o = getCoreValueAt(m, column);
                if (o instanceof Double) {
                    Double d = (Double) o;
                    if (d == 0)
                        return new Integer(0);
                    if (d == 1)
                        return new Integer(1);
                    double e = round(d, NUMBER_OF_DIGITS_AFTER_PERIOD);
                    return Double.valueOf(e);
                }
                return o;
            }

            public Object getCoreValueAt(Measures m, int column) {
                switch (column) {
                    case 0:
                        return m.className;
                    case 1:
                        return m.instances;
                    case 2:
                        return m.accuracy;
                    case 3:
                        return m.TP;
                    case 4:
                        return m.FN;
                    case 5:
                        return m.TN;
                    case 6:
                        return m.FP;
                    case 7:
                        return m.recall;
                    case 8:
                        return m.precision;
                    case 9:
                        return m.fScore;
                    default:
                        return null;
                }
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "className";
                    case 1:
                        return "instances";
                    case 2:
                        return "accuracy";
                    case 3:
                        return "TP";
                    case 4:
                        return "FN";
                    case 5:
                        return "TN";
                    case 6:
                        return "FP";
                    case 7:
                        return "recall";
                    case 8:
                        return "precision";
                    case 9:
                        return "fScore";
                    default:
                        return null;
                }
            }
        };
    }

    /** Creates new form ResultPanel */
    public ResultPanel() {
        initComponents();
    }

    public ResultPanel(Evaluator evaluator) {
        this.evaluator = evaluator;
        initComponents();
        updateLabels();
        updateVerticalHeader(jTable1);
        packAllColumns(jTable1);
        packAllColumns(jTable2);
    }

    public static void main(String[] args) {
        try {

            // LAF part
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // APW part
            ARFFLoader loader = new ARFFLoader(new File("data/soybean.arff"));
            Samples s = loader.getSamples();
            Samples[] samples = ClassifierTest.divide(s, 0.7);
            KNN knn = new KNN(samples[0], 4, KNN.SIMPLE_VOTING);
            Evaluator e = new Evaluator(knn, samples[1]);
            ResultPanel rp = new ResultPanel(e);


            // Swing part
            JFrame frame = new JFrame("Result evaluator panel test.");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(rp);
            frame.pack();
            frame.setVisible(true);

        } catch (Exception ex) {
            // there was a list of 6 exceptions...
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.toString(),
                    "An exception occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateLabels() {
        if (evaluator == null)
            return;
        // weighted value labels deprecated
        // no op
    }

    public static void showResultFrame(Evaluator e) {
        ResultPanel rp = new ResultPanel(e);

        // Swing part
        JFrame frame = new JFrame("Result evaluator panel test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(rp);
        frame.pack();
        frame.setVisible(true);
    }

    public static void showResultDialog(Evaluator e, Frame fame) {
        showResultDialog(e, fame, "Result evaluator panel test");
    }

    static void showResultDialog(Evaluator e, Frame fame, String name) {
        ResultPanel rp = new ResultPanel(e);

        // Swing part
        JDialog frame = new JDialog(fame, name);
        frame.setLocationRelativeTo(fame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(rp);
        frame.pack();
        frame.setVisible(true);
    }

    private static void packAllColumns(JTable table) {
        packAllColumns(table, DEFAULT_MARGIN_WIDTH);
    }

    private static void packAllColumns(JTable table, int margin) {
        for (int i = 0; i < table.getColumnCount(); i++)
            packColumn(table, i, margin);
    }

    private static void packColumn(JTable table, int vColIndex, int margin) {
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;

        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null)
            renderer = table.getTableHeader().getDefaultRenderer();
        Component comp = renderer.getTableCellRendererComponent(
                table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

        // Get maximum width of column data
        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(
                    table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }

        // Add margin
        width += 2 * margin;

        // Set the width
        col.setPreferredWidth(width);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new BetterJTable(getConfusionMatrixTableModel());
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new BetterJTable(getMeasuresMatrixTableModel());
        jPanel3 = new javax.swing.JPanel();

        jScrollPane1 = BetterJTable.createStripedJScrollPane(jTable1);
        /*
        jScrollPane1.setViewportView(jTable1);
        */

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Confusion Matrix", jPanel1);

        jScrollPane2 = BetterJTable.createStripedJScrollPane(jTable2); /*
        jScrollPane2.setViewportView(jTable2);
        */

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Metrics", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 438, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Summary", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables

    /**
     * Make all except first column header vertical.
     * @param table
     */
    private static void updateVerticalHeader(JTable table) {
        Enumeration enumer = table.getColumnModel().getColumns();
        enumer.nextElement();
        while (enumer.hasMoreElements()) {
            TableColumn tc = (TableColumn) enumer.nextElement();
            tc.setHeaderRenderer(new VerticalColumnHeaderRenderer());
        }
    }
}
