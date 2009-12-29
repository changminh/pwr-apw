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
 *     disclaimer in the  documentation and/or other mate provided
 *     with the distribution.
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
 * ParametersInput.java
 *
 * Created on 2009-09-11, 00:09:48
 */

package apw.art2.working;

import apw.art2a.ART2A_Util;
import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author nitric
 */
public class GUI extends javax.swing.JFrame {
    private double a, b, c, d, e, alpha, theta, rho;
    private Network network = null;
    private Samples samples = null;
    private JFileChooser fileChooser = new JFileChooser();
    private File file;
    private int dimension;
    private int iterations;
    private static boolean shuffle = true;
    private NumberFormat formatter = NumberFormat.getNumberInstance();

    /** Creates new form ParametersInput */
    public GUI() {
        initComponents();
        setLocationRelativeTo(null);
        formatter.setMaximumFractionDigits(4);
    }

    private void retrieveParameters() throws Exception {
        try {
            a = Double.parseDouble(jt_a.getText());
            b = Double.parseDouble(jt_b.getText());
            c = Double.parseDouble(jt_c.getText());
            d = Double.parseDouble(jt_d.getText());
            e = Double.parseDouble(jt_e.getText());
            alpha = Double.parseDouble(jt_alpha.getText());
            theta = Double.parseDouble(jt_theta.getText());
            rho = Double.parseDouble(jt_rho.getText());
            iterations = Integer.parseInt(jt_learningIterations.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Illegal number format!");
            throw ex;
        }
        if (a < 0 || b < 0 || c < 0 || d < 0 || e < 0 
                  || alpha < 0 || theta < 0 || rho < 0 || iterations < 0) {
            JOptionPane.showMessageDialog(this,
                    "All parameters should be non-negative."
            );
            throw new IllegalArgumentException();
        }
        if (theta >= 1 || rho >= 1 || alpha >= 1) {
            JOptionPane.showMessageDialog(this,
                    "'theta', 'alpha', 'vigilance' must be less than 1!"
            );
            throw new IllegalArgumentException();
        }
        if (c * d > (1 - d)) {
            JOptionPane.showMessageDialog(this,
                    "Parameters 'c' and 'd' must satisfy constraint: cd > 1-d"
            );
            throw new IllegalArgumentException();
        }
        try {
            samples = new ARFFLoader(new File(jt_samples.getText())).getSamples();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Couldn't read / parse data file!");
            throw ex;
        }
        if (!ART2A_Util.checkAttributes(samples)) {
            throw new IllegalArgumentException("All non-class attributes must be numeric!");
        }
    }

    private void showResults(ArrayList<Integer> shuffled, HashMap<Integer, Integer> results) {
        File outputFile = new File("clustering_results___" + System.currentTimeMillis() + ".htm");
        FileWriter out;
        try {
            out = new FileWriter(outputFile);
            out.append("<html><title>Clustering results: ART-2</title>\n");
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
            out.append("<tr align=\"center\"><td>alpha</td><td>" + alpha + "</td></tr>\n");
            out.append("<tr align=\"center\"><td>theta</td><td>" + theta + "</td></tr>\n");
            out.append("<tr align=\"center\"><td>vigilance</td><td>" + rho + "</td></tr>\n");
            out.append("<tr align=\"center\"><td>learning iterations</td><td>" + iterations + "</td></tr>\n");
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
            out.append("<td><center>Id</center></td>");
            out.append("<td><center>Sample</center></td>");
            boolean labelsAvailable = false;
            int classId = -1;
            if ((samples = network.getSamples()) != null && samples.getClassAttributeIndex() != -1) {
                samples = network.getSamples();
                classId = samples.getClassAttributeIndex();
                labelsAvailable = true;
            }
            out.append("<td><center>Cluster</td></center></tr>\n");
            Integer id;
            int cluster;
            for (int i = 0; i < shuffled.size(); i++) {
                id = shuffled.get(i);
                out.append("<tr align=\"center\"><td>" + id + "</td>");
                out.append("<td>" + samples.get(id) + "</td><td>");
                out.append((cluster = results.get(id)) + "</td></tr>\n");
                // Write results for 'future' summary:
                datasetOrder.put(id, cluster);
                if (clusters.containsKey(cluster))
                    clusters.get(cluster).add(id);
                else {
                    TreeSet<Integer> temp = new TreeSet<Integer>();
                    temp.add(id);
                    clusters.put(cluster, temp);
                }
                if (labelsAvailable) {
                    String key = samples.get(id).get(classId).toString();
                    if (!clustersContents.containsKey(key))
                        clustersContents.put(key, new HashMap<Integer, Integer>());
                    HashMap<Integer, Integer> map = clustersContents.get(key);
                    if (map.containsKey(cluster))
                        map.put(cluster, map.get(cluster) + 1);
                    else
                        map.put(cluster, 1);
                }
            }
            out.append("</table><br><br>");

            // Dataset ordering:
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
                        out.append("<td>" + samples.get(j) + "</td>");
                    out.append("</tr>\n");
                }
            }
            out.append("</table><br><br>\n");

            // Clusters summary:
            out.append("<a name=\"clusters_summary\"></a>");
            out.append("<b>Clusters summary: </b><br><br>");
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
                for (int i = 0; i < network.getPrototypesCount(); i++)
                    out.append("<td> " + i + " </td>");
                out.append("</tr>");
                /****************** Preparing table *****************/
                Set<String> tempSet = ((Nominal)samples.getClassAttribute()).getKeys();
                String[] classes = new String[tempSet.size()];
                int counter = 0;
                for (String s : tempSet)
                    classes[counter++] = s;
                int rows = classes.length;
                int cols = network.getPrototypesCount() + 1; // ?!
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
                        "(<b>" + formatter.format(percent) + "%</b>)");
            }
            out.append("</center></body></html>");
            out.close();
            Desktop.getDesktop().open(outputFile);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        jLabel2 = new javax.swing.JLabel();
        jt_a = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jt_b = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jt_c = new javax.swing.JTextField();
        jt_d = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jt_e = new javax.swing.JTextField();
        jt_theta = new javax.swing.JTextField();
        jt_rho = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jt_samples = new javax.swing.JTextField();
        jb_browse = new javax.swing.JButton();
        jb_cancel = new javax.swing.JButton();
        jb_train = new javax.swing.JButton();
        jcb_shuffle = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jt_alpha = new javax.swing.JTextField();
        jb_help = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jt_learningIterations = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Input parameters");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("a:");

        jt_a.setText("10");
        jt_a.setToolTipText("Fixed weight in F1 layer; usualy 10");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("b:");

        jt_b.setText("10");
        jt_b.setToolTipText("Fixed weight in F1 layer; usualy 10");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("c:");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("d:");

        jt_c.setText("0.1");
        jt_c.setToolTipText("Fixed weight used in testing for reset; usually 0.1");

        jt_d.setText("0.9");
        jt_d.setToolTipText("Activation of winner F2 neuron; usually 0.9");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("e:");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("theta:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("rho:");

        jt_e.setText("0");
        jt_e.setToolTipText("Prevents division by zero when vector norm is zero; usually 0");

        jt_theta.setText("0.01");
        jt_theta.setToolTipText("Noise suppresion;  theta < (1/sqrt(dimension))");

        jt_rho.setText("0.9");
        jt_rho.setToolTipText("Vigilance parameter; 0.7 < rho < 1");

        jLabel10.setText("samples:");

        jt_samples.setText("/home/nitric/workspace/NetBeansProjects/trunk/APW/data/iris.arff");

        jb_browse.setText("...");
        jb_browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_browseActionPerformed(evt);
            }
        });

        jb_cancel.setText("Cancel");
        jb_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cancelActionPerformed(evt);
            }
        });

        jb_train.setText("Train");
        jb_train.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_trainActionPerformed(evt);
            }
        });

        jcb_shuffle.setSelected(true);
        jcb_shuffle.setText("Shuffle instances");
        jcb_shuffle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcb_shuffleActionPerformed(evt);
            }
        });

        jLabel7.setText("alpha:");

        jt_alpha.setText("0.6");
        jt_alpha.setToolTipText("Constant in formua for initial bottom-up weights; should be between 0.5 and 1.");

        jb_help.setText("Help");
        jb_help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_helpActionPerformed(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("learning iterations:");

        jt_learningIterations.setText("100");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcb_shuffle)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jt_samples, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jb_browse, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jb_help)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jb_train)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jb_cancel)
                                .addGap(20, 20, 20))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jt_a)
                                            .addComponent(jt_b)
                                            .addComponent(jt_d, 0, 0, Short.MAX_VALUE)
                                            .addComponent(jt_c, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jt_learningIterations)
                                    .addComponent(jt_e)
                                    .addComponent(jt_alpha)
                                    .addComponent(jt_theta)
                                    .addComponent(jt_rho, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))))
                        .addGap(15, 15, 15)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jt_samples, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jb_browse))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jt_a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jt_e, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jt_b, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jt_alpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jt_c, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jt_theta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jt_d, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jt_rho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jt_learningIterations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jcb_shuffle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_help)
                    .addComponent(jb_train)
                    .addComponent(jb_cancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_jb_cancelActionPerformed

    private void jb_trainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_trainActionPerformed
        try {
            retrieveParameters();
        } catch (Exception ex) {
            return;
        }
        ArrayList<Attribute> atts = samples.getAtts();
        int attCount = atts.size();
        boolean labeled = samples.getClassAttributeIndex() != -1;
        dimension = labeled ? attCount - 1 : attCount;
        network = new Network(dimension, a, b, c, d, e, alpha, rho, theta, iterations);
        network.setSamples(samples);
        trainNetwork(network, samples);
    }//GEN-LAST:event_jb_trainActionPerformed

    private void jb_browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_browseActionPerformed
        int wynik = fileChooser.showOpenDialog(this);
        if (wynik == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            jt_samples.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jb_browseActionPerformed

    private void jcb_shuffleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcb_shuffleActionPerformed
        shuffle = jcb_shuffle.isSelected();
    }//GEN-LAST:event_jcb_shuffleActionPerformed

    private void jb_helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_helpActionPerformed
        new JD_ART2_Help(this, false).setVisible(true);
    }//GEN-LAST:event_jb_helpActionPerformed


    private void trainNetwork(Network network, Samples samples) {
        HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();
        ArrayList<Integer> shuffled = shuffle(samples.size());
        int attsCount = samples.getAtts().size();
        int classIndex = samples.getClassAttributeIndex();
        int attCounter = 0;
        Sample sample;
        int cluster;
        int index;
        for (int i = 0; i < samples.size(); i++) {
            sample = samples.get(index = shuffled.get(i));
            attCounter = 0;
            // Create table:
            double[] input = new double[dimension];
            for (int j = 0; j < attsCount; j++)
                if (j != classIndex)
                    input[attCounter++] = (Double) sample.get(j);
            cluster = network.process(input);
            System.out.println("Sample [" + shuffled.get(i) + "] --> " + cluster);
            results.put(index, cluster);
        }
        System.out.println("Training completed.");
        showResults(shuffled, results);
    }

    private void showResults(HashMap<Integer, Integer> results, Samples samples) {
        File f = null;
        HashMap<String, Integer> clusters = new HashMap<String, Integer>();
        String key;
        try {
            FileWriter out = new FileWriter(f = new File("results" + System.currentTimeMillis() + ".htm"));
            out.append("<html><title>Clustering resutls </title><body>\n");
            out.append("<center><b>Parameters</b><br><br>\n");
            // Parameters:
            out.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"thin\">\n");
            out.append("<tr align=\"center\"><td>Name</td><td>Value</td>");
            out.append("<tr align=\"center\"><td>a</td><td>" + a + "</td></tr>");
            out.append("<tr align=\"center\"><td>b</td><td>" + b + "</td></tr>");
            out.append("<tr align=\"center\"><td>c</td><td>" + c + "</td></tr>");
            out.append("<tr align=\"center\"><td>d</td><td>" + d + "</td></tr>");
            out.append("<tr align=\"center\"><td>e</td><td>" + e + "</td></tr>");
            out.append("<tr align=\"center\"><td>alpha</td><td>" + alpha + "</td></tr>");
            out.append("<tr align=\"center\"><td>rho</td><td>" + rho + "</td></tr>");
            out.append("<tr align=\"center\"><td>theta</td><td>" + theta + "</td></tr>");
            out.append("</table><br><br>\n");
            out.append("<b>Clustering results</b><br><br>\n");
            out.append("<table cellpadding=\"5\" cellspacing=\"5\" border=\"thin\"><tr>\n");
            out.append("<td><center>Sample Id</center></td>");
            out.append("<td><center>Whole sample</center></td>");
            out.append("<td><center>Cluster</td></center></tr>");
            // Cluster for each sample:
            for (int i = 0; i < samples.size(); i++) {
                out.append(
                        "<tr align=\"center\"><td>" + (i + 1) + "</td>"
                        + "<td>" + samples.get(i) + "</td>"
                        + "<td>" + results.get(i) + "</td></tr>\n");
                key = samples.get(i).get(samples.getClassAttributeIndex()) + "_" + results.get(i);
                if (clusters.containsKey(key))  {
                    clusters.put(key, clusters.get(key) + 1);
                }
                else {
                    clusters.put(key, 1);
                }
            }
            out.append("</table><br><br>");
            // Summary:
            if (samples.getClassAttributeIndex() != -1) {
                for (String str : clusters.keySet())
                    out.append(str + " --> " + clusters.get(str) + "<br>");
            }
            out.append("</center></body></html>");
            out.close();
            Desktop.getDesktop().open(f);
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static ArrayList<Integer> shuffle(int size) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (shuffle) {
            HashSet<Integer> shuffled = new HashSet<Integer>();
            Random r = new Random();
            while(result.size() != size) {
                int i = r.nextInt(size);
                if (shuffled.add(i)) {
                    result.add(i);
                }
            }
        }
        else {
            for (int i = 0; i < size; i++)
                result.add(i);
        }
        return result;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton jb_browse;
    private javax.swing.JButton jb_cancel;
    private javax.swing.JButton jb_help;
    private javax.swing.JButton jb_train;
    private javax.swing.JCheckBox jcb_shuffle;
    private javax.swing.JTextField jt_a;
    private javax.swing.JTextField jt_alpha;
    private javax.swing.JTextField jt_b;
    private javax.swing.JTextField jt_c;
    private javax.swing.JTextField jt_d;
    private javax.swing.JTextField jt_e;
    private javax.swing.JTextField jt_learningIterations;
    private javax.swing.JTextField jt_rho;
    private javax.swing.JTextField jt_samples;
    private javax.swing.JTextField jt_theta;
    // End of variables declaration//GEN-END:variables

}
