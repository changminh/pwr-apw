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

import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import apw.core.meta.ClassifierCapabilities;
import apw.core.util.SamplesNormalizer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import libsvm.*;

/**
 *
 * @author Przemek Woś
 */
@ClassifierCapabilities(multiClass = true, regression = true, handlesNumericAtts = true)
public class SVMClassifier extends Classifier {

    protected Samples samples = null;
    protected svm_parameter param;  // LibSVM options
    protected int normalize = 0;    // normalize input data
    protected svm_problem prob;     // LibSVM Problem
    protected svm_model model;      // LibSVM Model
    protected String errorMsg;
    protected String[] options = null;
    protected boolean debug = false;
    protected boolean _builded = false;

    private static class Utils {

        public static int getOptionPos(char flag, String[] options) {
            return getOptionPos("" + flag, options);
        }

        public static int getOptionPos(String flag, String[] options) {
            if (options == null)
                return -1;

            for (int i = 0; i < options.length; i++)
                if ((options[i].length() > 0) && (options[i].charAt(0) == '-'))
                    try {
                        Double.valueOf(options[i]);
                    } catch (NumberFormatException e) {
                        // found?
                        if (options[i].equals("-" + flag))
                            return i;
                        // did we reach "--"
                        if (options[i].charAt(1) == '-')
                            return -1;
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
                    if (i + 1 == options.length)
                        throw new Exception("No value given for -" + flag + " option.");
                    options[i] = "";
                    newString = new String(options[i + 1]);
                    options[i + 1] = "";
                    return newString;
                }
                if (options[i].charAt(1) == '-')
                    return "";
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

    private boolean checkSampleIdentity(Sample _sample){
        Samples _ss = _sample.getSamples();

        if(samples.getAtts().size() < _ss.getAtts().size()){
            return false;
        }

        if(_ss.getAtts().size() < samples.getAtts().size() - 1){
            return false;
        }

        for(int i=0; i < _ss.getAtts().size(); i++){
            if(_ss.getAtts().get(i) instanceof Nominal){
                if(!(samples.getAtts().get(i) instanceof Nominal)){return false;}
            }else{
                if(_ss.getAtts().get(i) instanceof Numeric){
                    if(!(samples.getAtts().get(i) instanceof Numeric)){return false;}
                }
            }
        }

        return true;
    }

    @Override
    public void addSamples(Samples s) {
        if(checkSampleIdentity(s.get(0))){
            samples.addAll(s);
        }
    }

    @Override
    public void addSample(Sample s) {
        if(this.checkSampleIdentity(s)){
            samples.add(s);
        }
    }

    @Override
    public void rebuild() {
        try {
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

    public void setOptions(String[] options) throws Exception {
        param = new svm_parameter();

        String svmtypeString = Utils.getOption('S', options);
        if (svmtypeString.length() != 0)
            param.svm_type = Integer.parseInt(svmtypeString);
        else
            param.svm_type = svm_parameter.C_SVC;

        String kerneltypeString = Utils.getOption('K', options);
        if (kerneltypeString.length() != 0)
            param.kernel_type = Integer.parseInt(kerneltypeString);
        else
            param.kernel_type = svm_parameter.RBF;



        String degreeString = Utils.getOption('D', options);
        if (degreeString.length() != 0)
            param.degree = (new Double(degreeString)).doubleValue();
        else
            param.degree = 3;

        String gammaString = Utils.getOption('G', options);
        if (gammaString.length() != 0)
            param.gamma = (new Double(gammaString)).doubleValue();
        else
            param.gamma = 0;

        String coef0String = Utils.getOption('R', options);
        if (coef0String.length() != 0)
            param.coef0 = (new Double(coef0String)).doubleValue();
        else
            param.coef0 = 0;

        String nuString = Utils.getOption('N', options);
        if (nuString.length() != 0)
            param.nu = (new Double(nuString)).doubleValue();
        else
            param.nu = 0.5;

        String cacheString = Utils.getOption('M', options);
        if (cacheString.length() != 0)
            param.cache_size = (new Double(cacheString)).doubleValue();
        else
            param.cache_size = 40;

        String costString = Utils.getOption('C', options);
        if (costString.length() != 0)
            param.C = (new Double(costString)).doubleValue();
        else
            param.C = 1;

        String epsString = Utils.getOption('E', options);
        if (epsString.length() != 0)
            param.eps = (new Double(epsString)).doubleValue();
        else
            param.eps = 1e-3;

        String normString = Utils.getOption('Z', options);
        if (normString.length() != 0)
            normalize = Integer.parseInt(normString);
        else
            normalize = 0;

        String lossString = Utils.getOption('P', options);
        if (lossString.length() != 0)
            param.p = (new Double(lossString)).doubleValue();
        else
            param.p = 0.1;

        String shrinkingString = Utils.getOption('H', options);
        if (shrinkingString.length() != 0)
            param.shrinking = Integer.parseInt(shrinkingString);
        else
            param.shrinking = 1;

        String probString = Utils.getOption('B', options);
        if (probString.length() != 0)
            param.probability = Integer.parseInt(probString);
        else
            param.probability = 0;

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
            for (int i = 1; i < count; i++)
                param.weight_label[i] = i;
        } else {
            param.nr_weight = 0;
            param.weight_label = new int[0];
            param.weight = new double[0];
        }

        this.options = options;
    }

    protected String samplesToSparse(Sample sample) {
        String line = new String();

        Object _c = samples.getAtts().get(sample.size() - 1).
                getRepresentation(sample.get(sample.size() - 1).toString());

        double c = Double.parseDouble(_c.toString());

        c -= 1.0;

        line = c + " ";
        for (int j = 1; j < sample.size(); j++) {
            String str = this.samples.getAtts().get(j - 1).getRepresentation(sample.get(j - 1)).toString();
            double value = Double.parseDouble(str);
            line += " " + j + ":" + value;
        }

        return (line + "\n");
    }

    protected Vector dataToSparse(Samples data) {
        Vector sparse = new Vector(data.size() + 1);

        for (int i = 0; i < data.size(); i++)
            sparse.add(samplesToSparse(data.get(i)));
        return sparse;
    }

    public boolean getDebug() {
        return debug;
    }

    public void setDebug(boolean _debug) {
        debug = _debug;
    }

    private double atof(String str) {
        return Double.valueOf(str).doubleValue();
    }

    private int atoi(String str) {
        return Double.valueOf(str).intValue();
    }

    private Object[] ClassesInProblem() {
        HashMap<String, Integer> data = new HashMap<String, Integer>();

        for (int i = 0; i < this.samples.size(); i++) {
            int size = samples.get(i).size();
            String str = samples.get(i).get(size - 1).toString();

            if (!data.containsKey(str))
                data.put(str, 0);
            else
                data.put(str, data.get(str).intValue() + 1);
        }

        return data.keySet().toArray();
    }

    @Override
    public double[] classifySample(Sample _sample) {

        Object[] _class = this.ClassesInProblem();
        double[] result = new double[_class.length];
        ArrayList<String> _classes = new ArrayList(_class.length);

        for (int i = 0; i < _class.length; i++) {
            _classes.add(_class[i].toString());
            result[i] = 0.0;
        }

        if ((_sample == null) || !_builded){
            return result;
        }
        
        if (this.checkSampleIdentity(_sample)) {
            svm_node[] x = null;

            Vector sparseData = new Vector();
            Vector vy = new Vector();
            sparseData.add(samplesToSparse(_sample));

            for (int d = 0; d < sparseData.size(); d++) {
                String line = (String) sparseData.get(d);

                StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
                vy.addElement(st.nextToken());

                int m = st.countTokens() / 2;
                x = new svm_node[m];
                for (int j = 0; j < m; j++) {
                    x[j] = new svm_node();
                    x[j].index = atoi(st.nextToken());
                    x[j].value = atof(st.nextToken());
                }
            }

            Collections.sort(_classes);
            String clasyfication = convert(new double[]{svm.svm_predict(model, x)});

            for (int i = 0; i < _classes.size(); i++)
                if (_classes.get(i).compareTo(clasyfication) == 0)
                    result[i] = 1.0;
                else
                    result[i] = 0.0;
        }

        return result;
    }

    private String convert(double[] data) {
        String result = null;

        for (Sample s : samples) {
            String str = this.samplesToSparse(s);
            String _str;

            int index = str.indexOf(' ', 0);

            _str = str.substring(0, index);

            if (Double.valueOf(_str).doubleValue() == data[0]) {
                result = s.get(s.size() - 1).toString();
                return result;
            }
        }

        return result;
    }

    public Sample getSample(int index) {
        return samples.get(index);
    }

    public void buildClassifier() {
        Samples _samples = this.samples;

        if (normalize == 1) {
            if (getDebug())
                System.err.println("Normalizing...");
            _samples = samples = SamplesNormalizer.normalize(samples);
        } else if (this.normalize != 0)
            System.err.println("Not defined value for normailizing...");

        if (getDebug())
            System.err.println("Converting to libsvm format...");

        Vector sparseData = dataToSparse(_samples);
        Vector vy = new Vector();
        Vector vx = new Vector();
        int max_index = 0;

        if (getDebug())
            System.err.println("Tokenizing libsvm data...");

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

            if (m > 0)
                max_index = Math.max(max_index, x[m - 1].index);
            vx.addElement(x);
        }

        prob = new svm_problem();
        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for (int i = 0; i < prob.l; i++)
            prob.x[i] = (svm_node[]) vx.elementAt(i);

        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++)
            prob.y[i] = atof((String) vy.elementAt(i));

        if (param.gamma == 0)
            param.gamma = 1.0 / max_index;

        errorMsg = svm.svm_check_parameter(prob, param);

        if (errorMsg != null) {
            System.err.print("Error: " + errorMsg + "\n");
            System.exit(1);
        }

        if (getDebug())
            System.err.println("Training model");

        try {
            model = svm.svm_train(prob, param);
            this._builded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {

            /*
            String[] ops = {
            new String("-S"), // WLSVM options
            new String("0"),  // Classification problem
            new String("-K"), // RBF kernel
            new String("0"),
            new String("-G"), // gamma
            new String("1"),
            new String("-C"), // C
            new String("7"),
            new String("-Z"), // normalize input data
            new String("1"),
            new String("-M"), // cache size in MB
            new String("100")
            };
             */
            Classifier svm = new SVMClassifier("data/weather.nominal.arff");
            svm.rebuild();


            double[] data = svm.classifySample(new ARFFLoader(new File("data/weather.nominal.arff")).getSamples().get(0));
            
            System.out.println(data[0] + " " + data[1]);
           

        } catch (IOException ex) {
            Logger.getLogger(SVMClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SVMClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SVMClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }





    }
}
