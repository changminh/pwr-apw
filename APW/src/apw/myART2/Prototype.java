package apw.myART2;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author nitric
 */
public class Prototype {
    
    private int dimension = -1;
    private int index = -1;
    private double[] weights = null;
    private HashSet<Integer> instances = new HashSet<Integer>();
    private double beta;
    private Instance closest = null;
    private Instance interpretation = null;

    public Prototype(double beta, int id, double[] dd) {
        this.beta = beta;
        index = id;
        weights = dd;
        dimension = dd.length;
    }

    public void addInstance(Instance inst) {
        if (inst.getLength() != dimension)
            throw new RuntimeException("Dimension error, expected (" + dimension +
                    ") != " + inst.getLength());
        if (weights == null) {
            weights = new double[inst.getLength()];
            for (int i = 0; i < inst.getLength(); i++)
                weights[i] = inst.at(i);
        }
        else {
            // Reseting closest instance:
            closest = null;
            // Updating weights:
            double[] v = inst.getProcessingVector();
            double[] temp = new double[v.length];
            int length = v.length;
            for (int i = 0; i < length; i++)
                temp[i] = (1.d - beta) * weights[i] + beta * v[i];
            double norm = 0.d;
            for (int i = 0; i < length; i++)
                norm += temp[i] * temp[i];
            norm = Math.sqrt(norm);
            for (int i = 0; i < length; i++)
                weights[i] = temp[i] / norm;
        }
    }

    public void removeInstance(Instance inst) {

    }

    public double countScore(Instance inst) {
        double sum = 0.d;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inst.at(i);
        }
        return sum;
    }

    public boolean contains(Instance inst) {
        return instances.contains(inst.getId());
    }

    public int getIndex() {
        return index;
    }

    public Instance findClosestInstance(ArrayList<Instance> instances) {
        double max = -1.d;
        double temp = 0.d;
        closest = null;
        for (Instance i : instances) {
            if ((temp = countScore(i)) > max) {
                max = temp;
                closest = i;
            }
        }
        interpretation = new Instance(weights, closest.getNorm());
        return closest;
    }

    public Instance turnToInstance(ArrayList<Instance> instances) {
        if (closest == null) {
            closest = findClosestInstance(instances);
            interpretation = new Instance(weights, closest.getNorm());
        }
        return interpretation;
    }
}
