/*
 * PrototypesPanel.java
 *
 * Created on 2009-08-06, 13:24:21
 */

package apw.art2a.gui;

import apw.art2a.Instance;
import apw.art2a.Network;
import apw.core.Nominal;
import apw.core.Samples;
import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author nitric
 */
public class PrototypesPanel extends javax.swing.JPanel 
        implements TableModelListener, ItemListener {

    private Network network;
    private int initialSamplesSize;

    /** Creates new form PrototypesPanel */
    PrototypesPanel(Network network) {
        this.network = network;
        if (network.getSamples() != null)
        initialSamplesSize = network.getSamples().size();
        initComponents();
        jt_prototypes.getModel().addTableModelListener(this);
        jcb_view.addItemListener(this);
        jt_prototypes.setDefaultRenderer(Double.class, new DoubleRenderer());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp = new javax.swing.JScrollPane();
        jt_prototypes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jb_exit = new javax.swing.JButton();
        jb_commit = new javax.swing.JButton();
        jcb_view = new javax.swing.JComboBox();
        jb_saveResults = new javax.swing.JButton();

        jt_prototypes.setModel(network);
        jsp.setViewportView(jt_prototypes);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Created prototypes");

        jb_exit.setText("Close");
        jb_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_exitActionPerformed(evt);
            }
        });

        jb_commit.setText("Commit sample");
        jb_commit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_commitActionPerformed(evt);
            }
        });

        jcb_view.setModel(new DefaultComboBoxModel(apw.art2a.gui.ViewType.values()));

        jb_saveResults.setText("Save results");
        jb_saveResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_saveResultsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .addComponent(jsp, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jcb_view, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(jb_saveResults)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jb_commit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jb_exit)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsp, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_exit)
                    .addComponent(jb_commit)
                    .addComponent(jcb_view, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_saveResults)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jb_commitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_commitActionPerformed
        network.commitBuffer();
    }//GEN-LAST:event_jb_commitActionPerformed

    private void jb_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_exitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jb_exitActionPerformed

    private void jb_saveResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_saveResultsActionPerformed
        File file = new File("clustering_results___" + System.currentTimeMillis() + ".htm");
        FileWriter out;
        try {
            out = new FileWriter(file);
            out.append("<html><title>Clustering results: ART-2A</title>\n");
            out.append("<body>\n");

            // Write parameter's values:
            out.append("<center>");
            out.append("<font size=\"5\">");
            if (network.getSamples() != null)
                out.append("Dataset <b> " + network.getSamples().getName() + "</b> - ");
            out.append("Clustering results </font><br><br><br>");
            out.append("<a href=\"#presentation_order\"> Querying results - presentation order </a><br>");
            out.append("<a href=\"#dataset_order\"> Querying results - dataset order </a><br>");
            out.append("<a href=\"#cluster_content\"> Cluster's contetns </a><br>");
            out.append("<a href=\"#clusters_summary\"> Clusters - summary</a><br>");
            out.append("<br><br><b>Parameters:</b><br><br>\n");
            out.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"thin\">\n");
            out.append("<tr align=\"center\"><td><b>Name</b></td><td><b>Value</b></td>\n");
            out.append("<tr align=\"center\"><td>alpha</td><td>" + network.getAlpha() + "</td></tr>\n");
            out.append("<tr align=\"center\"><td>beta</td><td>" + network.getBeta() + "</td></tr>\n");
            out.append("<tr align=\"center\"><td>theta</td><td>" + network.getTheta() + "</td></tr>\n");
            out.append("<tr align=\"center\"><td>vigilance</td><td>" + network.getRho() + "</td></tr>\n");
            out.append("</table>");

            // Write clustering result for each sample:
            network.learningMode(false);
            HashMap<Integer, Integer> datasetOrder = new HashMap<Integer, Integer>();
            HashMap<Integer, TreeSet<Integer>> clusters = new HashMap<Integer, TreeSet<Integer>>();
            TreeMap<String, HashMap<Integer, Integer>> clustersContents
                    = new TreeMap<String, HashMap<Integer, Integer>>();
            out.append("<a name=\"presentation_order\"></a>");
            out.append("<br><br><b>Querying results - presentation order:</b><br><br>");
            out.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"thin\"><tr>\n");
            out.append("<td><center>Sample Id</center></td>");
            out.append("<td><center>Input vector</center></td>");
            Samples samples = null;
            boolean labelsAvailable = false;
            int classId = -1;
            if ((samples = network.getSamples()) != null && samples.getClassAttributeIndex() != -1) {
                out.append("<td><center>Real label</center></td>");
                samples = network.getSamples();
                classId = samples.getClassAttributeIndex();
                labelsAvailable = true;
            }
            out.append("<td><center>Cluster</td></center></tr>\n");
            ArrayList<Instance> instances = network.getInstances();
            Instance inst;
            int c;
            for (int i = 0; i < instances.size(); i++) {
                inst = instances.get(i);
                out.append("<tr align=\"center\"><td>" + i + "</td><td>" + inst
                        + "</td><td>");
                if (labelsAvailable) {
                    if (samples.size() > inst.getId())
                        out.append("" + samples.get(inst.getId()).get(classId) + "</td><td>");
                    else
                        out.append("</td><td>");
                }
                out.append((c = network.query(inst).getIndex()) + "</td></tr>\n");
                // Write results for 'future' summary:
                datasetOrder.put(inst.getId(), c);
                if (clusters.containsKey(c))
                    clusters.get(c).add(inst.getId());
                else {
                    TreeSet<Integer> temp = new TreeSet<Integer>();
                    temp.add(inst.getId());
                    clusters.put(c, temp);
                }
                if (labelsAvailable) {
                    int id = inst.getId();
                    if (id < initialSamplesSize) {
                    String key = samples.get(id).get(classId).toString();
                    if (!clustersContents.containsKey(key))
                        clustersContents.put(key, new HashMap<Integer, Integer>());
                    HashMap<Integer, Integer> map = clustersContents.get(key);
                    if (map.containsKey(c))
                        map.put(c, map.get(c) + 1);
                    else
                        map.put(c, 1);
                    }
                }
            }
            out.append("</table><br><br>");

            // Dataset ordering:
            if (samples != null) {
                out.append("<a name=\"dataset_order\"></a>");
                out.append("<b>Clustering results - dataset ordering:</b><br><br>");
                out.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"thin\"><tr>\n");
                out.append("<td><center>Sample Id</center></td>");
                out.append("<td><center>Sample</center></td>");
                out.append("<td><center>Cluster</td></center></tr>");
                for (int i = 0; i < samples.size(); i++) {
                    out.append("<tr align=\"center\">");
                    out.append("<td> " + i + "</td>");
                    out.append("<td> " + samples.get(i) + " </td>");
                    out.append("<td> " + datasetOrder.get(i) + "</td>");
                    out.append("</tr>\n");
                }
                out.append("</table><br><br>");
            }

            // Cluster contents:
            out.append("<a name=\"cluster_content\"></a>");
            out.append("<b>Clusters contents:</b><br><br>");
            out.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"thin\">\n");
            out.append("<tr align=\"center\">");
            out.append("<td><center>Sample Id</center></td>");
            out.append("<td><center>Sample</center></td>");
            out.append("</tr>");
            for (Integer i : clusters.keySet()) {
                out.append("<tr align=\"center\"><td colspan=\"3\" align=\"center\"> Cluster " + i + "</td></tr>");
                for (Integer j : clusters.get(i)) {
                    out.append("<tr align=\"center\">");
                    out.append("<td>" + j + "</td>");
                    if (samples != null && samples.get(j) != null)
                        out.append("<td>" + samples.get(j) + "</td>");
                    else
                        out.append("<td>" + instances.get(j) + "</td>");
                    out.append("</tr>\n");
                }
            }
            out.append("</table><br><br>\n");

            // Clusters summary:
            out.append("<a name=\"clusters_summary\"></a>");
            out.append("<b>Clusters summary: </b><br><br>");
            out.append("Passes: " + network.getPasses());
            out.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"thin\">\n");
            out.append("<tr align=\"center\">");
            out.append("<td> Cluster Id</td>");
            out.append("<td> Cluster size</td>");
            out.append("</tr>");
            for (Integer i : clusters.keySet()) {
                out.append("<tr align=\"center\">");
                out.append("<td> " + i + " </td>");
                out.append("<td> " + clusters.get(i).size() + "</td>");
                out.append("</tr>\n");
            }
            out.append("</table>");

            // Summary matrix - if labels are available:
            if (labelsAvailable) {
                out.append("<br><br><b>Matrix of the truth: </b><br><br>");
                out.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"thin\">\n");
                out.append("<tr align=\"center\">");
                out.append("<td></td>");
                for (int i = 0; i < network.getPrototypes().size(); i++)
                    out.append("<td> " + i + " </td>");
                out.append("</tr>");
                /****************** Preparing table *****************/
                Set<String> tempSet = ((Nominal)samples.getClassAttribute()).getKeys();
                String[] classes = new String[tempSet.size()];
                int counter = 0;
                for (String s : tempSet)
                    classes[counter++] = s;
                int rows = classes.length;
                int cols = network.getPrototypes().size() + 1;
                Object[][] matrix = new Object[rows][cols];
                boolean[][] bg = new boolean[rows][cols];
                for (int row = 0; row < classes.length; row++) {
                    matrix[row][0] = classes[row];
                }
                int max = 0;
                int idmax = -1;
                Integer temp;
                for (int col = 0; col < cols - 1; col++) {
                    max = 0;
                    idmax = -1;
                    for (int row = 0; row < classes.length; row++) {
                        temp = clustersContents.get(classes[row]).get(col);
                        matrix[row][col + 1] = temp == null ? "" : temp;
                        if (temp != null && temp > max) {
                            max = temp;
                            idmax = row;
                        }
                    }
                    for (int row = 0; row < classes.length; row++) {
                        if (row == idmax)
                            continue;
                        else if (clustersContents.get(classes[row]).get(col) != null)
                            bg[row][col + 1] = true;
                    }
                }
                /****************************************************/
                int misses = 0;
                // For each class:
                for (int i = 0; i < rows; i++) {
                    out.append("<tr align=\"center\">");
                    for (int j = 0; j < cols; j++) {
                        out.append("<td");
                        if (bg[i][j]) {
                            out.append(" bgcolor=\"CCCCCC\"");
                            misses += (Integer) matrix[i][j];
                        }
                        out.append("> " + matrix[i][j]);
                        out.append("</td>");
                    }
                    out.append("</tr>");
                }
                out.append("</table>");
                double percent = ((double) (misses * 100)) / ((double) samples.size());
                out.append("<br><br>Misses: " + misses + " / " + samples.size() + " " +
                        "(<b>" + DoubleRenderer.formatter.format(percent) + "%</b>)");
            }
            out.append("</center></body></html>");
            out.close();
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(PrototypesPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jb_saveResultsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jb_commit;
    private javax.swing.JButton jb_exit;
    private javax.swing.JButton jb_saveResults;
    private javax.swing.JComboBox jcb_view;
    private javax.swing.JScrollPane jsp;
    private javax.swing.JTable jt_prototypes;
    // End of variables declaration//GEN-END:variables

    public void tableChanged(TableModelEvent arg0) {
        if (arg0.getType() == TableModelEvent.UPDATE
                || arg0.getType() == TableModelEvent.INSERT) {
            jt_prototypes.clearSelection();
            jt_prototypes.setRowSelectionInterval(arg0.getFirstRow(), arg0.getFirstRow());
        }
    }

    public void itemStateChanged(ItemEvent arg0) {
        if (arg0.getStateChange() == ItemEvent.SELECTED) {
            network.setView((ViewType)jcb_view.getSelectedItem());
        }
    }

    static class DoubleRenderer extends DefaultTableCellRenderer {

        static NumberFormat formatter = NumberFormat.getNumberInstance();

        public DoubleRenderer() {
            super();
            formatter.setMaximumFractionDigits(4);
        }

        @Override
        public void setValue(Object value) {
            try {
                setText((value == null) ? "" : formatter.format(value));
            } catch (IllegalArgumentException ex) {
                setText("");
            }
        }
    }
}
