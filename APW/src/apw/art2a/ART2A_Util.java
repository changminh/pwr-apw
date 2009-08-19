package apw.art2a;

import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JOptionPane;

/**
 *
 * @author nitric
 */
public class ART2A_Util {

    private double alpha, beta, rho, theta;
    private int passes;
    private Network network = null;
    private boolean labeled;
    private static NumberFormat formatter;

    static {
        formatter = NumberFormat.getNumberInstance(Locale.ENGLISH);
        formatter.setMinimumFractionDigits(4);
    }

    private void commitParameters(double a, double b, double r, double t, int p) {
        alpha = a;
        beta = b;
        rho = r;
        theta = t;
        passes = p;
    }

    /**
     * Use this method to create network initialized and learned form given set
     * of instances.
     * @param a alpha
     * @param b beta
     * @param r vigilance
     * @param t theta
     * @param passes passes that network has to go through. Each pass consist of
     * presenting to the network each instance exactly one time.
     * @param instances
     * @return learned network
     * @throws IllegalArgumentException
     */
    public Network createAndLearnNetwork(
            double a, double b, double r, double t, int p, ArrayList<Instance> instances)
            throws IllegalArgumentException {
        setParameters(a, b, r, t, p, instances.get(0).getProcessingVector().length);        
        learn(instances);
        return network;
    }

    /**
     * Use this method to create network, that won't be learned using instances
     * from file, but instances given interactively by user.
     * @param a alpha
     * @param b beta
     * @param r vigilance
     * @param t theta
     * @param col count of count of instance attributes.
     * @return
     */
    public Network createAndLearnNetwork(double a, double b, double r, double t, int col) {
        setParameters(a, b, r, t, passes, col);
        Network result = new Network(a, b, r, t, col);
        result.learningMode(true);
        return result;
    }

    private Network learn(ArrayList<Instance> instances) {
        int columns = instances.get(0).getLength();
        network = new Network(alpha, beta, rho, theta, columns);
        for (int i = 0; i < passes; i++) {
            for (Instance inst : instances)
                network.query(inst);
        }
        return network;
    }

    public static void main(String[] args) {
        ART2A_Util util = new ART2A_Util();
        ArrayList<Instance> instances = new ArrayList<Instance>();
        Network n = null;
        Samples samples = null;
        double t = 0.1d;
        try {
            samples = new ARFFLoader(new File("data/test.arff")).getSamples();
            // samples.setClassAttributeIndex(0);
            instances = util.shuffleInstances(util.convertSamples(samples, t));
            // n = createNetwork(0.3d, 0.005d, 0.99d, 0.01d, instances); // przy 9 przebiegach 2 błędy dla irysków :D
            n = util.createAndLearnNetwork(0.1d, 0.5d, 0.97d, t, 9, instances);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setParameters(double a, double b, double r, double t, int p, int columnCount)
            throws IllegalArgumentException {
        double limit = 1.d / Math.sqrt(columnCount);
        try {
            StringBuilder sb = new StringBuilder();
            String s1 = "0 &le; alpha &le; " + formatter.format(limit);
            String s2 = "0 &lt; beta &le; 1";
            String s3 = "0 &lt; vigilance &lt; 1";
            String s4 = "0 &le; theta &lt; " + formatter.format(limit);
            String s5 = "0 &le; passes";
            boolean error = false;
            sb.append("<html><body>The parameter's values should be: <br><br>");
            if (a > limit || a <= 0.f) {
                error = true;
                sb.append("<u>" + s1 + "</u><br>");
            }
            else
                sb.append(s1+ "<br>");
            if (b > 1.f || b <= 0.f) {
                error = true;
                sb.append("<u>" + s2 + "</u><br>");
            }
            else
                sb.append(s2 + "<br>");
            if (r >= 1.f || r <= 0.f) {
                error = true;
                sb.append("<u>" + s3 + "</u><br>");
            }
            else
                sb.append(s3 + "<br>");
            if (t >= limit || t < 0.f) {
                error = true;
                sb.append("<u>" + s4 + "</u><br>");
            }
            else
                sb.append(s4 + "<br>");
            if (p < 0) {
                error = true;
                sb.append("<u>" + s5 + "</u><br>");
            }
            else
                sb.append(s5 + "<br>");
            sb.append("</body></html>");
            if (error)
                throw new IllegalArgumentException(sb.toString());
            commitParameters(a, b, r, t, p);
        } catch (ArrayIndexOutOfBoundsException ex) { }
    }

    public ArrayList<Instance> convertSamples(Samples samples, double t) {
        // checking whether all attribute's (excluding class) are real numbers:
        ArrayList<Attribute> atts = samples.getAtts();
        int attCount = atts.size();
        int classAtt = samples.getClassAttributeIndex();
        for (int i = 0; i < attCount; i++) {
            if (atts.get(i).isNominal() && i != classAtt)
                throw new IllegalArgumentException("All non-class attributes must be real numbers!");
        }
        ArrayList<Instance> instances = new ArrayList<Instance>();

        // determining amount of valid attributes:
        int cc = 0;
        labeled = classAtt != -1;
        int validAtts = labeled ? attCount - 1 : attCount;

        // checking parameter 'theta':
        double limit = 1.d / Math.sqrt(validAtts);
        if (t < 0 || t >= limit)
            throw new IllegalArgumentException("<html><body>Value of paramether <i>theta</i> should" +
                    "be: &nbsp;&nbsp;&nbsp; 0 &lt; <i>theta</i> &lt; " + limit + "</body></html>");

        // creating internal representation of samples:
        double[] row;
        Sample s;
        int instancesCount = 0;
        for (int i = 0; i < samples.size(); i++) {
            cc = 0;
            s = samples.get(i);
            row = new double[validAtts];
            for (int j = 0; j < attCount; j++) {
                if (j == classAtt)
                    continue;
                row[cc++] = (Double) s.get(j);
            }
            try {
                instances.add(new Instance(row, instancesCount, t));
                instancesCount++;
            } catch (InstantiationError ex) { }
        }
        return instances;
    }

    public ArrayList<Instance> shuffleInstances(ArrayList<Instance> instances) {
        ArrayList<Instance> result = new ArrayList<Instance>();
        HashSet<Integer> shuffled = new HashSet<Integer>();
        int size = instances.size();
        Random r = new Random();
        shuffled.add(size);
        while(result.size() != instances.size()) {
            int i = r.nextInt(size);
            if (shuffled.add(i))
                result.add(instances.get(i));
        }
        return result;
    }

    public static void showStats(Network n, ArrayList<Instance> instances, Samples samples) {
        System.out.println("---------------- Network statistics -----------------");
        n.print();
        n.learningMode(false);
        for (Prototype p : n.getPrototypes()) {
            System.out.print("Closest instance for " + p.getIndex() + ": ");
            p.findClosestInstance(instances).print();
            p.turnToInstance(instances).print();
            System.out.println("");
        }
        if (samples.getClassAttributeIndex() == -1)
            return;
        HashMap<String, Integer> stats = new HashMap<String, Integer>();
        String temp;
        Prototype p;
        for (Instance inst : instances) {
            p = n.query(inst);
            temp = samples.get(inst.getId()).classAttributeInt().toString() + "_" + p.getIndex();
            if (stats.containsKey(temp))
                stats.put(temp, stats.get(temp) + 1);
            else
                stats.put(temp, 1);
        }
        for (String s : stats.keySet())
            System.out.println(s + " -> " + stats.get(s));
        n.learningMode(true);
        System.out.println("-----------------------------------------------------");
        System.out.println("Concat:");
        concatenateClusters(n);
    }

    public Set<String> retrieveClustersNames(Samples samples) {
        HashSet<String> result = null;
        if (samples.getClassAttributeIndex() != -1) {
            return ((Nominal) samples.getClassAttribute()).getKeys();
        }
        return result;
    }

    public static void concatenateClusters(Network network) {
        ArrayList<Cluster> clusters = new ArrayList<Cluster>();
        TreeSet<Instance> ordered = new TreeSet<Instance>(new Comparator<Instance> () {
            public int compare(Instance arg0, Instance arg1) {
                if (arg0.getId() < arg1.getId())
                    return -1;
                else if (arg0.getId() == arg1.getId())
                    return 0;
                else return 1;
            }
        });
        ordered.addAll(network.getInstances());
        for (Instance i : ordered)
            clusters.add(Cluster.getNewCluster(network.getActuatedNeurons(i)));
        boolean change = true;
        Cluster c, temp;
        outer:
        while (change) {
            inner:
            for (int i = 0; i < clusters.size() - 1; i++) {
                c = clusters.get(i);
                for (int j = i + 1; j < clusters.size(); j++) {
                    temp = clusters.get(j);
                    if (!c.containsCommonPrototypes(temp))
                        continue;
                    if (c.theSame(temp)) {
                        clusters.remove(j);
                        continue outer;
                    }
                    else if (c.isSubsetOf(temp)) {
                        clusters.remove(c);
                        continue outer;
                    }
                    else if (c.isSupersetOf(temp)) {
                        clusters.remove(temp);
                        continue outer;
                    }
                    else {
                        clusters.add(c.merge(temp));
                        clusters.remove(c);
                        clusters.remove(temp);
                        continue outer;
                    }
                }
            }
            change = false;
        }
        for (Cluster cc : clusters) {
            System.out.print(cc.getIndex() + ": ");
            for (Prototype p : cc.getPrototypes())
                System.out.print(p.getIndex() + " ");
            System.out.println("");
        }
    }
}