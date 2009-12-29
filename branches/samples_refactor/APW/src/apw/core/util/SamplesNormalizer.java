package apw.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apw.core.Sample;
import apw.core.Samples;


public class SamplesNormalizer {
    private double[] minValue;
    private double[] maxValue;
    
    
    public SamplesNormalizer(Samples samples) {
    	final int DATA_VECTOR_LENGTH = samples.getAtts().size() - (samples.getClassAttribute().isNominal() ? 1 : 0);
    	maxValue = initValuesArray(DATA_VECTOR_LENGTH, Double.MIN_VALUE);
    	minValue = initValuesArray(DATA_VECTOR_LENGTH, Double.MAX_VALUE);
    	
		for (Sample s : samples) {
			double[] dd = s.toDoubleArray();
			for (int i = 0; i < dd.length; i++) {
				maxValue[i] = Math.max(maxValue[i], dd[i]);
				minValue[i] = Math.min(minValue[i], dd[i]);
			}
		}
	}
    
    public void scale(double[] value) {
		for (int i = 0; i < value.length; i++) {
			value[i] = (value[i] - minValue[i]) / (maxValue[i] - minValue[i] + 0.1);	// 0.1 to avoid problems with same numbers all along the range
		}
    }
    
    private static double[] initValuesArray(int valuesLength, double initValue) {
    	final double[] result = new double[valuesLength];
    	
    	Arrays.fill(result, initValue);
    	
    	return result;
    }
    
    public static Samples normalize(Samples nSamples) {
        double length;
        Samples _samples = nSamples.copyStructure();

        for (int i = 0; i < nSamples.size(); i++) {
            length = 0.0;
            int size = nSamples.get(i).size() - 1;
            double data;

            for (int j = 0; j < size; j++) {
                String str = nSamples.getAtts().get(j).getRepresentation(nSamples.get(i).get(j)).toString();
                data = atof(str);
                length += data * data;
            }

            length = Math.sqrt(length);

            List<Object> list = new ArrayList<Object>();

            for (int j = 0; j < size; j++)
                if (!nSamples.getAtts().get(j).isNominal()) {
                    String str = nSamples.getAtts().get(j).getRepresentation(nSamples.get(i).get(j)).toString();
                    data = atof(str);
                    data /= length;
                    str = Double.toString(data);
                    Object obj = nSamples.getAtts().get(j).getRepresentation(str);
                    list.add(obj);
                } else
                    list.add(nSamples.getAtts().get(j).getRepresentation(nSamples.get(i).get(j)));

            Object obj = nSamples.getAtts().get(size).
                    getRepresentation(nSamples.get(i).get(size).toString());


            list.add(obj);
            Sample newSample = new Sample(_samples, list.toArray());

            _samples.add(newSample);
        }

        return _samples;
    }
    
    private static double atof(String str) {
        return Double.valueOf(str).doubleValue();
    }
}
