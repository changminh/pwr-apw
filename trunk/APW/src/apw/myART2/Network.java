package apw.myART2;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author nitric
 */
public class Network {
    private HashMap<Integer, Prototype> prototypes = new HashMap<Integer, Prototype>();
    private boolean learningMode = true;
    private double alpha, beta, rho, theta;

    public Network(double alpha, double beta, double rho, double theta) {
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.theta = theta;
    }

    public Prototype query(Instance inst) {
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
                return p;
            }
            else
                return addNeuron(inst);
        }
    }

    private Prototype addNeuron(Instance inst) {
        int number = prototypes.size();
        Prototype p;
        prototypes.put(number, p = new Prototype(beta, number, inst.getProcessingVector()));
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
    }

    public void stopLearning() {
        learningMode = false;
    }

    public Collection<Prototype> getPrototypes() {
        return prototypes.values();
    }
}
