package apw.myART2;

import apw.core.Attribute;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author nitric
 */
public class ART_2A {

    private static double alpha, beta, rho, theta;
    private static int passes;
    private static Network network = null;
    private static HashSet<String> labels = null;
    private static boolean labeled;


    public static Network createNetwork(
            double a, double b, double r, double t, int p, ArrayList<Instance> instances)
            throws IllegalArgumentException {
        checkParameters(a, b, r, t, p, instances.get(0).getVector().length);
        alpha = a;
        beta = b;
        rho = r;
        theta = t;
        passes = p;
        normalizeVectors(instances);
        normalizeVectors(instances = denoiseVectors(instances));
        /* System.out.println("Input vectors: ");
        for (Instance inst : instances)
            inst.print(); */
        learn(instances);
        return network;
    }

    private static Network learn(ArrayList<Instance> instances) {
        network = new Network(alpha, beta, rho, theta);
        for (int i = 0; i < passes; i++) {
            for (Instance inst : instances) {
                // inst.print();
                network.query(inst);
                // network.print();
                // System.out.println("\n\n\n");
            }
        }
        return network;
    }

    private static void normalizeVectors(ArrayList<Instance> instances) {
        double sum, norm;
        int length = instances.get(0).getVector().length;
        double[] v;
        for (Instance i : instances) {
            sum = 0;
            v = i.getVector();
            for (int j = 0; j < length; j++)
                sum += v[j] * v[j];
            norm = Math.sqrt(sum);
            for (int j = 0; j < length; j++)
                v[j] = v[j] / norm;
            i.setVector(v);
        }
    }

    private static ArrayList<Instance> denoiseVectors(ArrayList<Instance> instances) {
        ArrayList<Instance> denoised = new ArrayList<Instance>();
        double[] v;
        boolean foundNonZero;
        for (Instance i : instances) {
            foundNonZero = false;
            v = i.getVector();
            for (int j = 0; j < v.length; j++) {
                if (v[j] < theta)
                    v[j] = 0.d;
                else
                    foundNonZero = true;
            }
            if (foundNonZero)
                denoised.add(i);
        }
        System.out.println("Removed " + (instances.size() - denoised.size()) + " samples.");
        return denoised;
    }

    public static void main(String[] args) {
        ArrayList<Instance> instances = new ArrayList<Instance>();
        Network n = null;
        Samples samples = null;
        try {
            samples = new ARFFLoader(new File("data/wine.arff")).getSamples();
            instances = shuffleInstances(convertSamples(samples));
            // n = createNetwork(0.3d, 0.005d, 0.99d, 0.01d, instances); // przy 9 przebiegach 2 błędy dla irysków :D
            n = createNetwork(0.1d, 0.1d, 0.995d, 0.01d, 15, instances);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        n.print();
        n.stopLearning();
        HashMap<String, Integer> stats = new HashMap<String, Integer>();
        String temp;
        Prototype p;
        for (Instance inst : instances) {
            // System.out.print("Instance " + inst.getId() + " -> ");
            p = n.query(inst);
            if (labeled) {
                temp = samples.get(inst.getId()).classAttributeInt().toString() + "_" + p.getIndex();
                if (stats.containsKey(temp))
                    stats.put(temp, stats.get(temp) + 1);
                else
                    stats.put(temp, 1);
            }
        }
        if (labeled) {
            for (String s : stats.keySet())
                System.out.println(s + " -> " + stats.get(s));
        }
    }

    private static void checkParameters(double a, double b, double r, double t, int p, int columnCount)
            throws IllegalArgumentException {
        double limit = 1.d / Math.sqrt(columnCount);
        StringBuilder sb = new StringBuilder();
        String s1 = "0 &lt;= alfa &lt;= " + limit;
        String s2 = "0 &lt; beta &lt;= 1";
        String s3 = "0 &lt; rho &lt; 1";
        String s4 = "0 &lt;= theta &lt; " + limit;
        String s5 = "0 &lt; passes";
        boolean error = false;
        sb.append("<html><body>The parameter's values should be: <br><br>");
        // Check alpha:
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

    public static ArrayList<Instance> convertSamples(Samples samples) {
        // checking whether all attribute's (excluding class) are real numbers:
        ArrayList<Attribute> atts = samples.getAtts();
        labels = new HashSet<String>();
        int attCount = atts.size();
        int classAtt = samples.getClassAttributeIndex();
        System.out.println("classAtt = " + classAtt);
        for (int i = 0; i < attCount; i++) {
            if (atts.get(i).isNominal() && i != classAtt)
                throw new IllegalArgumentException("All non-class attributes must be real numbers!");
        }
        ArrayList<Instance> instances = new ArrayList<Instance>();
        // creating internal representation of samples:
        int cc = 0;
        labeled = classAtt != -1;
        int validAtts = labeled ? attCount - 1 : attCount;
        System.out.println("validAtts = " + validAtts);
        double[] row;
        Sample s;
        for (int i = 0; i < samples.size(); i++) {
            cc = 0;
            s = samples.get(i);
            row = new double[validAtts];
            for (int j = 0; j < attCount; j++) {
                if (j == classAtt) {
                    if (labeled)
                        labels.add(s.get(j).toString());
                        continue;
                }
                row[cc++] = (Double) s.get(j);
            }
            instances.add(new Instance(row, i));
        }
        if (labeled && false) {
            System.out.println("Labels: ");
            for (String st : labels)
                System.out.print(st + " ");
            System.out.println("");
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
}