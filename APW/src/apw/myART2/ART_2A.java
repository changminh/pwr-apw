package apw.myART2;

import apw.core.Attribute;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author nitric
 */
public class ART_2A {

    private static double alpha, beta, rho, theta;
    private static Network network = null;


    public static Network createNetwork(
            double a, double b, double r, double t, ArrayList<Instance> instances)
            throws IllegalArgumentException {
        checkParameters(a, b, r, t, instances.get(0).getVector().length);
        alpha = a;
        beta = b;
        rho = r;
        theta = t;
        normalizeVectors(instances);
        normalizeVectors(instances = denoiseVectors(instances));
        System.out.println("Input vectors: ");
        for (Instance inst : instances)
            inst.print();
        learn(instances);
        return network;
    }

    private static Network learn(ArrayList<Instance> instances) {
        network = new Network(alpha, beta, rho, theta);
        for (int i = 0; i < 3; i++) {
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
        int counter = 0;

        Network n = null;
        try {
            instances = convertSamples(new ARFFLoader(new File("data/iris.arff")).getSamples());
            n = createNetwork(0.5d, 0.2d, 0.992d, 0.01d, instances);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        n.print();
        n.stopLearning();
        for (Instance inst : instances) {
            System.out.print("Instance " + inst.getId() + " -> ");
            n.query(inst).print();
        }
    }

    private static void checkParameters(double a, double b, double r, double t, int columnCount) {
        double limit = 1.d / Math.sqrt(columnCount);
        StringBuilder sb = new StringBuilder();
        String s1 = "0 &lt;= alfa &lt;= " + limit;
        String s2 = "0 &lt; beta &lt;= 1";
        String s3 = "0 &lt; rho &lt; 1";
        String s4 = "0 &lt;= theta &lt; " + limit;
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
        sb.append("</body></html>");
        if (error)
            throw new IllegalArgumentException(sb.toString());
    }

    public static ArrayList<Instance> convertSamples(Samples samples) {
        // checking whether all attribute's (excluding class) are real numbers:
        ArrayList<Attribute> atts = samples.getAtts();
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
        int validAtts = classAtt == -1 ? attCount : attCount - 1;
        System.out.println("validAtts = " + validAtts);
        double [] row;
        for (int i = 0; i < samples.size(); i++) {
            cc = 0;
            row = new double[validAtts];
            for (int j = 0; j < attCount; j++) {
                if (j == classAtt)
                    continue;
                row[cc++] = (Double) samples.get(i).get(j);
            }
            instances.add(new Instance(row, i));
        }
        return instances;
    }
}