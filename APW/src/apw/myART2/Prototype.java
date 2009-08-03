package apw.myART2;

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
    private double alpha, beta, rho, theta;

    public Prototype(double a, double b, double r, double t, int id, double[] dd) {
        alpha = a;
        beta = b;
        rho = r;
        theta = t;
        index = id;
        weights = dd;
        dimension = dd.length;
    }

    public void addInstance(Instance inst) {
        if (inst.getVector().length != dimension)
            throw new RuntimeException("Dimension error expected (" + dimension +
                    ") != " + inst.getVector().length);
        if (weights == null) {
            weights = new double[inst.getVector().length];
            for (int i = 0; i < inst.getVector().length; i++)
                weights[i] = inst.at(i);
        }
        else {
            // Updating weights:
            double[] v = inst.getVector();
            double[] temp = new double[v.length];
            for (int i = 0; i < v.length; i++)
                temp[i] = (1.d - beta) * weights[i] + beta * v[i];
            double norm = 0.d;
            for (int i = 0; i < temp.length; i++)
                norm += temp[i] * temp[i];
            norm = Math.sqrt(norm);
            for (int i = 0; i < temp.length; i++)
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

    public void print() {
        System.out.print("Prototype " + index + ": ");
        for (double d : weights)
            System.out.print(d + " ");
        System.out.println("");
    }
}
