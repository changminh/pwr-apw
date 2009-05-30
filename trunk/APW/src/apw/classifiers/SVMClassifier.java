/*
 *  Copyright (c) 2009, Wrocław University of Technology
 *  All rights reserved.
 *  Redistribution  and use in source  and binary  forms,  with or
 *  without modification,  are permitted provided that the follow-
 *  ing conditions are met:
 *
 *   • Redistributions of source code  must retain the above copy-
 *     right  notice, this list  of conditions and  the  following
 *     disclaimer.
 *   • Redistributions  in binary  form must  reproduce the  above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the  documentation and/or other mate provided
 *     with the distribution.
 *   • Neither  the name of the  Wrocław University of  Technology
 *     nor the names of its contributors may be used to endorse or
 *     promote products derived from this  software without speci-
 *     fic prior  written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRI-
 *  BUTORS "AS IS" AND ANY  EXPRESS OR IMPLIED WARRANTIES, INCLUD-
 *  ING, BUT NOT  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTA-
 *  BILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 *  NO EVENT SHALL THE COPYRIGHT HOLDER OR  CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCURE-
 *  MENT OF SUBSTITUTE  GOODS OR SERVICES;  LOSS OF USE,  DATA, OR
 *  PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER  CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING  NEGLIGENCE  OR  OTHERWISE) ARISING IN ANY WAY
 *  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSI-
 *  BILITY OF SUCH DAMAGE.
 */

package apw.classifiers;

import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import libsvm.*;


/**
 *
 * @author przemo
 */
public class SVMClassifier extends Classifier {

    protected Samples samples = null;
    protected svm_parameter param;  // LibSVM oprions
    protected int normalize=0;        // normalize input data
    protected svm_problem prob;     // LibSVM Problem
    protected svm_model model;      // LibSVM Model
    protected String error_msg;
    protected String[] options = null;
    protected boolean debug = false;

    private static class Utils {

        public static int getOptionPos(char flag, String[] options) {
            return getOptionPos("" + flag, options);
        }

        public static int getOptionPos(String flag, String[] options) {
            if (options == null) {
                return -1;
            }

            for (int i = 0; i < options.length; i++) {
                if ((options[i].length() > 0) && (options[i].charAt(0) == '-')) {
                    // Check if it is a negative number
                    try {
                        Double.valueOf(options[i]);
                    } catch (NumberFormatException e) {
                        // found?
                        if (options[i].equals("-" + flag)) {
                            return i;
                        }
                        // did we reach "--"
                        if (options[i].charAt(1) == '-') {
                            return -1;
                        }
                    }
                }
            }

            return -1;
        }

        public static String getOption(char flag, String[] options)
                throws Exception {

            return getOption("" + flag, options);
        }

        public static String getOption(String flag, String[] options)
                throws Exception {

            String newString;
            int i = getOptionPos(flag, options);

            if (i > -1) {
                if (options[i].equals("-" + flag)) {
                    if (i + 1 == options.length) {
                        throw new Exception("No value given for -" + flag + " option.");
                    }
                    options[i] = "";
                    newString = new String(options[i + 1]);
                    options[i + 1] = "";
                    return newString;
                }
                if (options[i].charAt(1) == '-') {
                    return "";
                }
            }

            return "";
        }
    };

    public SVMClassifier(Samples s) {
        super(s);
        this.samples = s;

        String[] dummy = {};
        try {
            setOptions(dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SVMClassifier(File input) throws IOException, ParseException {
        this(new ARFFLoader(input).getSamples());
    }

    public SVMClassifier(InputStream input) throws IOException, ParseException {
        this(new ARFFLoader(input).getSamples());
    }

    public SVMClassifier(String source) throws IOException, ParseException {
        this(new File(source));
    }

    @Override
    public void addSamples(Samples s)  {
        samples.addAll(s);
    }

    @Override
    public void addSample(Sample s) {
        samples.add(s);
    }

    @Override
    public void rebuild() {
        try {
            // throw new UnsupportedOperationException("Not supported yet.");
           // setOptions(options);
            buildClassifier();

        } catch (Exception ex) {
            Logger.getLogger(SVMClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Classifier copy() {
        try {

            SVMClassifier svm = new SVMClassifier(samples);
            svm.setOptions(options);
            svm.buildClassifier();
            return svm;

        } catch (Exception ex) {
            Logger.getLogger(SVMClassifier.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Samples normalize(Samples nSamples) {
        double length;
        Samples _samples = new Samples(nSamples.getAtts());

        for (int i = 0; i < nSamples.size(); i++) {
            length = 0.0;
            int size = nSamples.get(i).size() - 1;

            for (int j = 0; j < size; j++) {
                double data = atof(nSamples.get(i).get(j).toString());
                length += data * data;
            }

            length = Math.sqrt(length);

            List<Object> list = new ArrayList<Object>();

            for (int j = 0; j < size; j++) {
                double data = atof(nSamples.get(i).get(j).toString());
                data /= length;
                String str = Double.toString(data);
                Object obj = nSamples.getAtts().get(j).getRepresentation(str);
                list.add(obj);
            }

            Object obj = nSamples.getAtts().get(size).
                    getRepresentation(nSamples.get(i).get(size).toString());

            list.add(obj);

            Sample newSample = new Sample(new Samples(nSamples.getAtts()), list.toArray());
            // System.err.println(newSample);
            _samples.add(newSample);
        }

        return _samples;
    }

    public void setOptions(String[] options) throws Exception {
        param = new svm_parameter();

        String svmtypeString = Utils.getOption('S', options);
        if (svmtypeString.length() != 0) {
            param.svm_type = Integer.parseInt(svmtypeString);
        } else {
            param.svm_type = svm_parameter.C_SVC;
        }

        String kerneltypeString = Utils.getOption('K', options);
        if (kerneltypeString.length() != 0) {
            param.kernel_type = Integer.parseInt(kerneltypeString);
        } else {
            param.kernel_type = svm_parameter.RBF;
        }



        String degreeString = Utils.getOption('D', options);
        if (degreeString.length() != 0) {
            param.degree = (new Double(degreeString)).doubleValue();
        } else {
            param.degree = 3;
        }

        String gammaString = Utils.getOption('G', options);
        if (gammaString.length() != 0) {
            param.gamma = (new Double(gammaString)).doubleValue();
        } else {
            param.gamma = 0;
        }

        String coef0String = Utils.getOption('R', options);
        if (coef0String.length() != 0) {
            param.coef0 = (new Double(coef0String)).doubleValue();
        } else {
            param.coef0 = 0;
        }

        String nuString = Utils.getOption('N', options);
        if (nuString.length() != 0) {
            param.nu = (new Double(nuString)).doubleValue();
        } else {
            param.nu = 0.5;
        }

        String cacheString = Utils.getOption('M', options);
        if (cacheString.length() != 0) {
            param.cache_size = (new Double(cacheString)).doubleValue();
        } else {
            param.cache_size = 40;
        }

        String costString = Utils.getOption('C', options);
        if (costString.length() != 0) {
            param.C = (new Double(costString)).doubleValue();
        } else {
            param.C = 1;
        }

        String epsString = Utils.getOption('E', options);
        if (epsString.length() != 0) {
            param.eps = (new Double(epsString)).doubleValue();
        } else {
            param.eps = 1e-3;
        }

        String normString = Utils.getOption('Z', options);
        if (normString.length() != 0) {
            normalize = Integer.parseInt(normString);
        } else {
            normalize = 0;
        }

        String lossString = Utils.getOption('P', options);
        if (lossString.length() != 0) {
            param.p = (new Double(lossString)).doubleValue();
        } else {
            param.p = 0.1;
        }

        String shrinkingString = Utils.getOption('H', options);
        if (shrinkingString.length() != 0) {
            param.shrinking = Integer.parseInt(shrinkingString);
        } else {
            param.shrinking = 1;
        }

        String probString = Utils.getOption('B', options);
        if (probString.length() != 0) {
            param.probability = Integer.parseInt(probString);
        } else {
            param.probability = 0;
        }

        String weightsString = Utils.getOption('W', options);
        if (weightsString.length() != 0) {
            StringTokenizer st = new StringTokenizer(weightsString, " ");
            int n_classes = st.countTokens();
            param.weight_label = new int[n_classes];
            param.weight = new double[n_classes];

            // get array of doubles from this string
            int count = 0;
            while (st.hasMoreTokens()) {
                param.weight[count++] = Double.parseDouble(st.nextToken());
            //atof(st.nextToken());
            }
            param.nr_weight = count;
            param.weight_label[0] = -1; // label of first class
            for (int i = 1; i < count; i++) {
                param.weight_label[i] = i;
            }
        } else {
            param.nr_weight = 0;
            param.weight_label = new int[0];
            param.weight = new double[0];
        }

        this.options = options;
    }

    /**
     * Converts an ARFF Instance into a string in the sparse format accepted by
     * LIBSVM
     *
     * @param instance
     * @return
     */
    protected String samplesToSparse(Sample sample) {
        String line = new String();

        int c = (int) Double.parseDouble(sample.classAttributeRepr().toString());

        if (c == 0) {
            c = -1;
        }

        line = c + " ";
        for (int j = 1; j < sample.size(); j++) {
            int tmpI = (int) Double.parseDouble(sample.classAttributeRepr().toString());
            if (j - 1 == tmpI) {
                continue;
            }

            double value = Double.parseDouble(sample.get(j - 1).toString());

            if (value != 0.0) {
                line += " " + j + ":" + value;
            }
        }
        // System.out.println(line);
        return (line + "\n");
    }

    /**
     * converts an ARFF dataset into sparse format
     *
     * @param instances
     * @return
     */
    protected Vector dataToSparse(Samples data) {
        Vector sparse = new Vector(data.size() + 1);

        for (int i = 0; i < data.size(); i++) { // for each instance
            sparse.add(samplesToSparse(data.get(i)));
        }
        return sparse;
    }

    public boolean getDebug() {return debug;}
    
    public void setDebug(boolean _debug){debug = _debug;}

    private double atof(String str) {
        return Double.valueOf(str).doubleValue();
    }

    private int atoi(String str) {
        return Double.valueOf(str).intValue();
    }

    @Override
    // @SuppressWarnings("empty-statement")
    public double[] classifySample(Sample s) {

        if (s == null) {
            return null;
        }

        Vector sparseData = new Vector();
        sparseData.add(samplesToSparse(s));

        if (samples.get(0).size() == s.size()) {
            svm_node[] x = null;

            for (int d = 0; d < sparseData.size(); d++) {
                String line = (String) sparseData.get(d);

                StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

                int m = st.countTokens() / 2;
                x = new svm_node[m];
                for (int j = 0; j < m; j++) {
                    x[j] = new svm_node();
                    x[j].index = atoi(st.nextToken());
                    x[j].value = atof(st.nextToken());
                }
            }

            return new double[]{svm.svm_predict(model, x)};
        }
        return null;

    }

    public String interprate(double[] data) {
        String result = null;

        for (Sample s : samples) {
            String str = this.samplesToSparse(s);
            String _str;

            int index = str.indexOf(' ', 0);
            
            _str = str.substring(0, index);

            if (Double.valueOf(_str).doubleValue() == data[0]) {
                result = s.get(s.size()-1).toString();
                return result;
            }
        }

        return result;
    }

    public Sample getSample(int index) {
        return samples.get(index);
    }

    public void buildClassifier() {
       // Samples _samples = new Samples(samples);
        Samples _samples = this.samples;

        if (normalize == 1) {
            if (getDebug()) {
                System.err.println("Normalizing...");
            }
            _samples = normalize(samples);
        }else{
            if(this.normalize != 0){
                System.err.println("Not defined value for normailizing...");
            }
        }

        if (getDebug()) {
            System.err.println("Converting to libsvm format...");
        }

        Vector sparseData = dataToSparse(_samples);
        Vector vy = new Vector();
        Vector vx = new Vector();
        int max_index = 0;

        if (getDebug()) {
            System.err.println("Tokenizing libsvm data...");
        }

        for (int d = 0; d < sparseData.size(); d++) {
            String line = (String) sparseData.get(d);

            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

            vy.addElement(st.nextToken());
            int m = st.countTokens() / 2;
            svm_node[] x = new svm_node[m];
            for (int j = 0; j < m; j++) {
                x[j] = new svm_node();
                x[j].index = atoi(st.nextToken());
                x[j].value = atof(st.nextToken());
            }
            if (m > 0) {
                max_index = Math.max(max_index, x[m - 1].index);
            }
            vx.addElement(x);
        }

        prob = new svm_problem();
        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for (int i = 0; i < prob.l; i++) {
            prob.x[i] = (svm_node[]) vx.elementAt(i);
        }

        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++) {
            prob.y[i] = atof((String) vy.elementAt(i));
        }

        if (param.gamma == 0) {
            param.gamma = 1.0 / max_index;
        }

        error_msg = svm.svm_check_parameter(prob, param);

        if (error_msg != null) {
            System.err.print("Error: " + error_msg + "\n");
            System.exit(1);
        }

        if (getDebug()) {
            System.err.println("Training model");
        }
        
        try {
            model = svm.svm_train(prob, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            String dataFile = "c:/svm/data/iris.arff";  // input arff file

            String[] ops = {new String("-t"),
                dataFile,
                new String("-x"), // 5 folds CV
                new String("5"),
                new String("-i"), //
                new String("-S"), // WLSVM options
                new String("0"),  // Classification problem
                new String("-K"), // RBF kernel
                new String("2"),
                new String("-G"), // gamma
                new String("1"),
                new String("-C"), // C
                new String("7"),
                new String("-Z"), // normalize input data
                new String("1"),
                new String("-M"), // cache size in MB
                new String("100")
            };

            SVMClassifier svm = new SVMClassifier("c:/svm/data/iris.arff");
            svm.setOptions(ops);
            svm.setDebug(true);
            svm.buildClassifier();

            double[] data = svm.classifySample(svm.getSample(0));
            System.out.println(svm.interprate(data));

        } catch (IOException ex) {
            Logger.getLogger(SVMClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SVMClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SVMClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}