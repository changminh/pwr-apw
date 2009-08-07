package apw.myART2;

import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author nitric
 */
public class ART_2A {

    private static double alpha, beta, rho, theta;
    private static int passes;
    private static Network network = null;
    private static boolean labeled;


    public static Network createAndLearnNetwork(
            double a, double b, double r, double t, int p, ArrayList<Instance> instances)
            throws IllegalArgumentException {
        checkParameters(a, b, r, t, p, instances.get(0).getProcessingVector().length);
        alpha = a;
        beta = b;
        rho = r;
        theta = t;
        passes = p;
        learn(instances);
        return network;
    }

    private static Network learn(ArrayList<Instance> instances) {
        int columns = instances.get(0).getLength();
        network = new Network(alpha, beta, rho, theta, columns);
        for (int i = 0; i < passes; i++) {
            for (Instance inst : instances)
                network.query(inst);
        }
        return network;
    }

    public static void main(String[] args) {
        ArrayList<Instance> instances = new ArrayList<Instance>();
        Network n = null;
        Samples samples = null;
        double t = 0.1d;
        try {
            samples = new ARFFLoader(new File("data/test.arff")).getSamples();
            // samples.setClassAttributeIndex(0);
            instances = shuffleInstances(convertSamples(samples, t));
            // n = createNetwork(0.3d, 0.005d, 0.99d, 0.01d, instances); // przy 9 przebiegach 2 błędy dla irysków :D
            n = createAndLearnNetwork(0.1d, 0.5d, 0.97d, t, 9, instances);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkParameters(double a, double b, double r, double t, int p, int columnCount)
            throws IllegalArgumentException {
        double limit = 1.d / Math.sqrt(columnCount);
        StringBuilder sb = new StringBuilder();
        String s1 = "0 &lt;= alpha &lt;= " + limit;
        String s2 = "0 &lt; beta &lt;= 1";
        String s3 = "0 &lt; vigilance &lt; 1";
        String s4 = "0 &lt;= theta &lt; " + limit;
        String s5 = "0 &lt; passes";
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
        if (p < 1) {
            error = true;
            sb.append("<u>" + s5 + "</u><br>");
        }
        else
            sb.append(s5 + "<br>");
        sb.append("</body></html>");
        if (error)
            throw new IllegalArgumentException(sb.toString());
    }

    public static ArrayList<Instance> convertSamples(Samples samples, double t) {
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

    public static ArrayList<Instance> shuffleInstances(ArrayList<Instance> instances) {
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
        network.learningMode(true);
        System.out.println("-----------------------------------------------------");
    }

    public Set<String> retrieveClustersNames(Samples samples) {
        HashSet<String> result = null;
        if (samples.getClassAttributeIndex() != -1) {
            return ((Nominal) samples.getClassAttribute()).getKeys();
        }
        return result;
    }
}