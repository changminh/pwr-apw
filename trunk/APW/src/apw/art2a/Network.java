package apw.art2a;

import apw.art2a.gui.ViewType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author nitric
 */
public class Network extends AbstractTableModel {
    protected HashMap<Integer, Prototype> prototypes = new HashMap<Integer, Prototype>();
    protected boolean learningMode = true;
    protected double alpha, beta, rho, theta;
    protected int columns = -1;
    protected double[] buffer;
    protected ViewType viewType = ViewType.CLOSEST_INSTANCE;
    protected ArrayList<Instance> instances = new ArrayList<Instance>();
    protected String[] columnNames;

    public Network(double alpha, double beta, double rho, double theta, int columns) {
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.theta = theta;
        this.columns = columns;
        resetBuffer();
    }

    public Prototype query(Instance inst) {
        if (!instances.contains(inst))
            instances.add(inst);
        // Querying:
        if (prototypes.size() == 0 && learningMode)
            addNeuron(inst);
        int bestIndex = -1;
        double bestValue = 0.d;
        double currentValue = 0.d;
        for (Prototype p : prototypes.values()) {
            if ((currentValue = p.countScore(inst)) > bestValue) {
                bestIndex = p.getIndex();
                bestValue = currentValue;
            }
        }
        Prototype p = prototypes.get(bestIndex);
        if (!learningMode)
            return p;
        else {
            if (!similarityTest(inst, bestValue))
                return addNeuron(inst);
            else if (vigilanceTest(bestValue)) {
                p.addInstance(inst);
                fireTableRowsUpdated(bestIndex + 1, bestIndex + 1);
                return p;
            }
            else {
                p = addNeuron(inst);
                return p;
            }
        }
    }

    public ArrayList<Prototype> getActuatedNeurons(Instance instance) {
        ArrayList<Prototype> result = new ArrayList<Prototype>();
        double score = 0.d;
        for (Prototype p : prototypes.values()) {
            score = p.countScore(instance);
            if (similarityTest(instance, score) && vigilanceTest(score))
                result.add(p);
        }
        /////////
        System.out.print("Instance " + instance.getId() + " -> ");
        for (Prototype p : result)
            System.out.print(p.getIndex() + " ");
        System.out.println("");
        /////////
        return result;
    }



    private Prototype addNeuron(Instance inst) {
        int number = prototypes.size();
        Prototype p;
        prototypes.put(number, p = new Prototype(beta, number, inst.getProcessingVector()));
        fireTableRowsInserted(prototypes.size(), prototypes.size());
        return p;
    }

    private boolean similarityTest(Instance inst, double score) {
        double alphaSum = 0;
        for (double d : inst.getProcessingVector())
            alphaSum += d;
        alphaSum *= alpha;
        return score >= alphaSum;
    }

    private boolean vigilanceTest(double score) {
        return score >= rho;
    }
    
    public void print() {
        System.out.println("Network has " + prototypes.size() + " neurons.");
        System.out.println("Parameters: ");
        System.out.print("alpha = " + alpha);
        System.out.print(", beta = " + beta);
        System.out.print(", vigilance = " + rho);
        System.out.println(", theta = " + theta);
    }

    public void learningMode(boolean b) {
        learningMode = b;
    }

    public Collection<Prototype> getPrototypes() {
        return prototypes.values();
    }

    /************** Methods responsible for rendering table **************/
    private void resetBuffer() {
        buffer = new double[columns];
        for (int i = 0; i < columns; i++)
            buffer[i] = Double.NEGATIVE_INFINITY;
    }

    @Override
    public Class getColumnClass(int col) {
        return Double.class;
    }
    
    public int getRowCount() {
        return prototypes.size() + 1;
    }

    public int getColumnCount() {
        return columns;
    }

    public Object getValueAt(int row, int col) {
        if (row == 0)
            return buffer[col] == Double.NEGATIVE_INFINITY ? "" : buffer[col];
        else {
            Prototype p = prototypes.get(row - 1);
            switch (viewType) {
                case CLOSEST_INSTANCE:
                    return p.findClosestInstance(instances).inputAt(col);
                case SIMULATED_INSTANCE:
                    return p.turnToInstance(instances).inputAt(col);
                case WEIGHTS:
                    return p.at(col);
            }
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        buffer[columnIndex] = (Double) aValue;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return row == 0;
    }

    public void commitBuffer() {
        for (int i = 0; i < columns && true; i++) {
            if (buffer[i] < 0) {
                JOptionPane.showMessageDialog(null, "All input values should be greater or equal 0!");
                return;
            }
        }
        try {
            query(new Instance(buffer, instances.size(), theta));
            resetBuffer();
        } catch (InstantiationError ex) {
            JOptionPane.showMessageDialog(null, "All input values should be greater or equal 0!");
        }
    }

    public void setView(ViewType viewType) {
        this.viewType = viewType;
        fireTableDataChanged();
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public ArrayList<Instance> getInstances() {
        return instances;
    }
}
