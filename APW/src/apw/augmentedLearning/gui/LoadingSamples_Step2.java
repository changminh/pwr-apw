/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Created on 2009-04-26, 11:13:31
 */

package apw.augmentedLearning.gui;


import alice.tuprolog.Term;
import alice.tuprolog.Var;
import apw.core.Attribute;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.util.FastVector;
import apw.augmentedLearning.logic.AttributeTypes;
import apw.augmentedLearning.logic.LoadingSamplesMain;
import apw.augmentedLearning.logic.DataFile;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Nitric
 */
public class LoadingSamples_Step2 extends javax.swing.JFrame {

    /* TODO: Maybe it would be not so bad idea if we declared public final static fields (in 'Attribute'
     * class), that contain this values? */
    private static final String real = "real";
    private static final String integer = "integer";

    private DataFile dataFile;
    private ArrayList<String[]> records = null;
    private LoadingSamplesMain advisor;
    private Vector<Integer> nominals = new Vector<Integer>();
    private ArrayList<AttributeTypes> attributesTypes = new ArrayList<AttributeTypes>();
    private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
    private ArrayList<String> attributesNames = new ArrayList<String>();
    private ArrayList<HashSet<String>> nominalValues = new ArrayList<HashSet<String>>();
    private Object[][] rawObjects;
    private Samples samples;
    private int classAttributeIndex = -1;
    private int classAttributeColumIndex = 6;
    private int attributesCount;
    private HashMap<Integer, ArrayList<Integer>> conflictingSamples = new HashMap<Integer, ArrayList<Integer>>();
    private boolean hasToWait = false;
    private String interceptor;

    private String[] columnNames = new String[] {
        "Numer atrybutu", 
        "Nazwa atrybutu",
        "Krotka 1",
        "Krotka 2",
        "Krotka 3",
        "Typ atrybutu",
        "Atrybut klasy"
    };

    public LoadingSamples_Step2(DataFile format, LoadingSamplesMain advisor) {
        this.dataFile = format;
        this.advisor = advisor;
        initComponents();
        setLocationRelativeTo(advisor.getStep1());
        advisor.getStep1().dispose();
        postInitComponents();
        attributesCount = dataFile.getAttributesCount();
    }

    private void createAttributes() {
        try {
            int nominalIndex = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < attributesTypes.size(); i++) {
                switch(attributesTypes.get(i)) {
                    case INTEGER:
                        attributes.add(Attribute.createAttribute(attributesNames.get(i), integer));
                        break;
                    case REAL_NUMBER:
                        attributes.add(Attribute.createAttribute(attributesNames.get(i), real));
                        break;
                    case NOMINAL:
                        sb = new StringBuilder("{");
                        for (String s : nominalValues.get(nominalIndex))
                            sb.append(s + ",");
                        sb.delete(sb.length() - 2, sb.length() - 1);
                        sb.append("}");
                        attributes.add(
                            Attribute.createAttribute(attributesNames.get(i), sb.toString())
                        );
                        nominalIndex++;
                        break;
                }
            }
            if (attributes.size() != dataFile.getAttributesCount())
                System.err.println("Amounts of attributes declared and attributes found are different!");
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
    }

    private void createSamples() {
        samples = new Samples(attributes);
        samples.setData(new FastVector());
        Sample sample = null;
        String nullTag = dataFile.getMissingValueTag();
        Object[] values;
        rawObjects = new Object[records.size()][];
        HashSet<Integer> samplesWithNull = new HashSet<Integer>();
        Term[] terms;
        int counter = 0;
        // TODO: Optimization...?
        for (String [] record : records) {
            values = new Object[attributesCount];
            terms = new Term[attributesCount];
            for (int i = 0; i < attributesCount; i++) {
                if (record[i].equals(nullTag)) {
                    values[i] = null;
                    terms[i] = new Var("_");
                    samplesWithNull.add(counter);
                }
                else
                    switch(attributesTypes.get(i)) {
                        case INTEGER: 
                            values[i] = Integer.valueOf(record[i]);
                            terms[i] = new alice.tuprolog.Int(Integer.parseInt(record[i]));
                            break;
                        case REAL_NUMBER: 
                            values[i] = Double.valueOf(record[i]);
                            terms[i] = new alice.tuprolog.Float(Float.parseFloat(record[i]));
                            break;
                        case NOMINAL:
                            values[i] = record[i];
                            terms[i] = new alice.tuprolog.Struct(record[i]);
                            break;
                    }
            }
            sample = new Sample(samples, values);
            samples.add(sample);
            advisor.addTerms(terms);
            rawObjects[counter++] = values;
        }
        advisor.setSamples(samples);
        dataFile.setRawObjects(rawObjects);
        dataFile.setSamplesWithNull(samplesWithNull);
        new Thread() {
            @Override
            public void run() {
                checkForConflictingSamples();
            }
        }.start();
    }

    /**
     * This code assumes that columns are:
     * [0]: attribute id,
     * [1]: attribute name (editable)
     * [2], [3], [4]: attributes values of the three beginning records of data
     * [5]: attribute type (comboBox that enables to choose/correct the data type)
     * [6]: class attribute (boolean, checkbox = editable)
     */
    private void postInitComponents() {
        jt_dataTable.getColumnModel().getColumn(5).setCellEditor(
                new DefaultCellEditor(new JComboBox(AttributeTypes.values())));

    }

    public DataFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    /**
     * Assumption is the same as in the 'postInitComponents()' method.
     * @return Data that is used to render the table
     */
    private Object[][] prepareTableData() {
        int countOfExamples = columnNames.length - 3;
        Object[][] result = new Object[dataFile.getAttributesCount()][columnNames.length];
        String examples[] = new String[countOfExamples];
        String temp;
        int counter = 0;

        // Tries to read ${countOfExamples} lines with data, so must skip comments and empty lines:
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile.getDataFile()));
            while ((counter < countOfExamples) && (temp = reader.readLine()) != null) {
                if (temp.startsWith(dataFile.getStartOfComment()) || temp.length() < 1)
                    continue;
                examples[counter++] = temp;
            }
        }
        catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Wystąpił błąd przy próbie otwarcia pliku!");
            ex.printStackTrace();
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Wystąpił błąd w trakcie czytania pliku!");
            ex.printStackTrace();
        }

        // Parses the lines with data to obtain their attribute's values:
        String[] tokens = null;
        for (int i = 0; i < countOfExamples; i++) {
            tokens = examples[i].split(dataFile.getAttributesSeparator());
            for (int j = 0; j < tokens.length; j++) {
                result[j][i + 2] = tokens[j];
            }
        }

        // Prepares rest of table's data:
        for (int i = 0; i < dataFile.getAttributesCount(); i++) {
            result[i][6] = new Boolean(false);
            result[i][0] = "" + (i + 1);                        // Id of attribute
            result[i][1] = "Atr_" + (i + 1);                    // Initial name of attribute
            try {
                Integer.parseInt(tokens[i]);
                // result[i][5] = AttributeTypes.INTEGER;
                result[i][5] = AttributeTypes.REAL_NUMBER;
            }
            catch (NumberFormatException ex) {
                try {
                    Double.parseDouble(tokens[i]);
                    result[i][5] = AttributeTypes.REAL_NUMBER;
                }
                catch (NumberFormatException ex2) {
                    result[i][5] = AttributeTypes.NOMINAL;
                }
            }
        }
        return result;
    }

    private TableModel prepareTableModel() {
        AbstractTableModel result = new AbstractTableModel() {
            Object[][] data = prepareTableData();

            public int getRowCount() {
                return dataFile.getAttributesCount();
            }

            public int getColumnCount() {
                return columnNames.length;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                return data[rowIndex][columnIndex];
            }

            @Override
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }

            @Override
            public String getColumnName(int col) {
                return columnNames[col];
            }
            
            @Override
            public boolean isCellEditable(int row, int col) { 
                return (col == 1 || col == 5 || col == classAttributeColumIndex);
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col != 6)                           // Not the checkbox
                    data[row][col] = value;
                else {
                    if ((Boolean) value) {
                        if (classAttributeIndex != -1) {
                            setValueAt(false, classAttributeIndex, classAttributeColumIndex);
                        }
                        fireTableCellUpdated(classAttributeIndex, classAttributeColumIndex);
                        classAttributeIndex = row;
                        data[row][classAttributeColumIndex] = true;
                    }
                    else {
                        data[row][classAttributeColumIndex] = false;
                        classAttributeIndex = -1;
                    }
                }
            }
        };
        result.setValueAt(true, dataFile.getAttributesCount() - 1, classAttributeColumIndex);
        return result;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jl_wprowadzNazwyAtrybutow = new javax.swing.JLabel();
        jb_next = new javax.swing.JButton();
        jsp_tablePanel = new javax.swing.JScrollPane();
        jt_dataTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jl_wprowadzNazwyAtrybutow.setFont(new java.awt.Font("Tahoma", 0, 14));
        jl_wprowadzNazwyAtrybutow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_wprowadzNazwyAtrybutow.setText("Krok 2: Wprowadź nazwy atrybutów");

        jb_next.setText("Dalej");
        jb_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_nextActionPerformed(evt);
            }
        });

        jt_dataTable.setModel(prepareTableModel());
        jsp_tablePanel.setViewportView(jt_dataTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsp_tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 785, Short.MAX_VALUE)
                    .addComponent(jl_wprowadzNazwyAtrybutow, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                    .addComponent(jb_next, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jl_wprowadzNazwyAtrybutow)
                .addGap(7, 7, 7)
                .addComponent(jsp_tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jb_next)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_nextActionPerformed
        TableModel model = jt_dataTable.getModel();
        AttributeTypes type;

        // collect attribute's names and types:
        for (int i = 0; i < dataFile.getAttributesCount(); i++) {
            attributesNames.add((String)model.getValueAt(i, 1));
            attributesTypes.add(type = (AttributeTypes)model.getValueAt(i, 5));
            if (type == AttributeTypes.NOMINAL)
                nominals.add(i);
        }
        collectRecords();
        collectNominalValues();
        dataFile.setAttributesNames(attributesNames);
        dataFile.setClassAttributeIndex(classAttributeIndex);
        samples.setClassAttributeIndex(classAttributeIndex);
}//GEN-LAST:event_jb_nextActionPerformed

    private void collectRecords() {
        int counter = 0;
        try {
            records = new ArrayList<String[]>();
            BufferedReader reader = new BufferedReader(new FileReader(dataFile.getDataFile()));
            String temp;
            String separator = dataFile.getAttributesSeparator();
            String commentStart = dataFile.getStartOfComment();
            while ((temp = reader.readLine()) != null) {
                if (temp.startsWith(commentStart) || temp.length() < 1)
                    continue;
                counter++;
                records.add(temp.split(separator));
            }
        }

        catch (FileNotFoundException ex) {
            Logger.getLogger(LoadingSamples_Step2.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Wczytano " + counter + " krotek.");
    }

    /**
     * Collects all the values for the attributes which value's type is Nominal
     */
    private void collectNominalValues() {
        String nullTag = dataFile.getMissingValueTag();
        int attributeId;
        for (int i = 0; i < nominals.size(); i++)
            nominalValues.add(new HashSet<String>());
        for (String[] s: records) {
            for (int i = 0; i < nominals.size(); i++) {
                // Line belows determines index of the attribute which is the i'th nominal attribute
                attributeId = nominals.get(i);
                if (!s[attributeId].equals(nullTag))
                    nominalValues.get(i).add(s[attributeId]);
            }
        }

        for (int i = 0; i < nominals.size(); i++) {
            System.out.println("Wartości nominalne dla atrybutu nr " + (nominals.get(i) + 1) + ":");
            System.out.println(nominalValues.get(i));
            System.out.println("-----------------------------------------------------------");
        }

        dataFile.setNominals(nominals);
        dataFile.setNominalValues(nominalValues);
        dataFile.setRecords(records);
        // Convert to project's internal representation:
        createAttributes();
        createSamples();
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoadingSamples_Step2(null, null).setVisible(true);
            }
        });
    }

    private synchronized void checkForConflictingSamples() {
        final HashSet<Integer> accessorsToRemove = new HashSet<Integer>();
        ArrayList<Integer> temp = null;
        boolean foundOtherValue = false;
        int attributeId = -1;
        // For all samples not checked yet:
        outer:
        for (Integer current = 0; current < rawObjects.length - 1; current++) {
            temp = null;
            // If sample is considered to be conflicting with some other, we can skip it:
            if (accessorsToRemove.contains(current))
                continue outer;
            // Else, check all following samples, that aren't yet considered to be conflicting.
            Object[] sample = rawObjects[current];
            inner:
            for (Integer otherSampleId = current + 1; otherSampleId < rawObjects.length; otherSampleId++) {
                // Again: if sample is considered to be conflicting with some other, skip it.
                if (accessorsToRemove.contains(otherSampleId))
                    continue inner;
                Object[] otherSample = rawObjects[otherSampleId];
                foundOtherValue = false;
                for (int attId = 0; attId < attributesCount; attId++) {
                    if (!(sample[attId] == null || otherSample[attId] == null || sample[attId].equals(otherSample[attId])))
                        if (!foundOtherValue) {
                            foundOtherValue = true;
                            attributeId = attId;
                        }
                        else continue inner;
                }
                if (foundOtherValue && attributeId == classAttributeIndex) {
                    accessorsToRemove.add(otherSampleId);
                    if (temp == null) {
                        temp = new ArrayList<Integer>();
                        temp.add(current);
                    }
                    temp.add(otherSampleId);
                    conflictingSamples.put(current, temp);
                }
            }
        }
        Iterator<Integer> iter = conflictingSamples.keySet().iterator();
        Integer i;
        ArrayList<String> categories;
        String s;
        // Foreach 'cluster' of conflicting samples:
        while (iter.hasNext()) {
            categories = new ArrayList<String>();
            i = iter.next();
            System.err.print("Konflikt:");
            temp = conflictingSamples.get(i);
            // Collect possible categories that the samples from clusters are claimed to belong:
            for (Integer j : temp) {
                System.err.print(" " + (j + 1));
                if (!categories.contains(s = (String) rawObjects[j][classAttributeIndex]))
                    categories.add(s);
            }
            System.err.println("");
            // Display window...
            new ConflictResolvingThread(rawObjects[i], categories.toArray(new String[] { }), classAttributeIndex, LoadingSamples_Step2.this).start();
            // ... and wait for the expert's answer!
            try {
                waitForResponse();
                System.out.println("Zakończono oczekiwanie");
            } catch (InterruptedException e) { System.out.println("Przerwano oczekiwanie"); }
            rawObjects[i][classAttributeIndex] = interceptor;
        }
        new Thread() {
            @Override
            public void run() {
                advisor.step3();
                advisor.getTermsAccessors().removeAll(accessorsToRemove);
                advisor.setBannedSamples(accessorsToRemove);
            }
        }.start();
    }

    private synchronized void waitForResponse() throws InterruptedException {
        hasToWait = true;
        while (hasToWait == true) {
            wait();
        }
    }

    public synchronized void setValue(String s) {
        interceptor = s;
        hasToWait = false;
        notifyAll();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jb_next;
    private javax.swing.JLabel jl_wprowadzNazwyAtrybutow;
    private javax.swing.JScrollPane jsp_tablePanel;
    private javax.swing.JTable jt_dataTable;
    // End of variables declaration//GEN-END:variables

    class ConflictResolvingThread extends Thread {
        private Object[] sample;
        private String[] categories;
        private int classAttId;
        private LoadingSamples_Step2 parent;

        public ConflictResolvingThread(Object[] sample, String[] categories, int classAttId, LoadingSamples_Step2 parent) {
            this.sample = sample;
            this.categories = categories;
            this.classAttId = classAttId;
            this.parent = parent;
        }

        @Override
        public void run() {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new ConflictsResolvingFrame(sample, categories, samples, classAttId, parent).setVisible(true);
                }
            });
        }
    }
}
