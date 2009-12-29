package apw.classifiers.id3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import apw.classifiers.Classifier;
import apw.classifiers.RuleClassifier;
import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.meta.ClassifierCapabilities;

/**
 * @author Krzysztof Praszmo
 */
@ClassifierCapabilities(multiClass = true, handlesNominalAtts = true)
public class ID3 extends RuleClassifier {

    private ID3DecisionNode<Object> tree;

    public ID3(Samples s) {
        super(s);
        ArrayList<Nominal> attributes = new ArrayList<Nominal>(s.getAtts().size());
        for (Attribute attribute : s.getAtts())
            if (attribute instanceof Nominal) {
                Nominal n = (Nominal) attribute;
                if (n != s.getClassAttribute())
                    attributes.add(n);
            } else
                throw new IllegalArgumentException("Each sample attribute must be nominal");

        tree = new ID3DecisionNode<Object>(s, attributes);

        for (String str : getRules()) {
            //System.out.println(str);
        }
    }

    @Override
    public String[] getRules() {
        LinkedList<String> list = tree.getRules();
        String[] result = new String[list.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = "if " + list.get(i).substring(2);
        return result;
    }

    @Override
    public void addSample(Sample s) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addSamples(Samples s) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double[] classifySample(Sample s) {

        Object sampleClass = tree.classify(s);
        Attribute att = s.getSamples().getClassAttribute();
        if (att instanceof Nominal) {
            Nominal nominal = (Nominal) att;
            double[] result = new double[nominal.getSortedIKeys().length];
            result[Arrays.binarySearch(nominal.getSortedIKeys(), sampleClass)] = 1.0;
            return result;
        }
        throw new IllegalArgumentException("Sample class must be Nominal");
    }

    @Override
    public Classifier copy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void rebuild() {
        // TODO Auto-generated method stub
    }
}
