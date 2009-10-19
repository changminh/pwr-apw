package apw.kohonenSom.patterns;

import apw.kohonenSom.util.*;
import apw.core.Attribute;
import apw.core.Sample;
import apw.core.Samples;
import java.util.ArrayList;

/**
 *
 * @author Christopher Wadowski
 */
public class SOMSamplesLoader {
    public static final int EXCLUDE_MISSSING_VAL_SAMPLES = -1;

    public static final int NUM_MFREQ_MISSING_VAL = 1;
    public static final int NUM_AVR_MISSING_VAL_ARITHM = 2;
    public static final int NUM_AVR_MISSING_VAL_GEOM = 3;
    public static final int NUM_AVR_MISSING_VAL_SQR = 4;
    public static final int NUM_AVR_MISSING_VAL_HARM = 5;

    public static final int NOM_MFREQ_MISSING_VAL = 1;

    private boolean normalizeVectors;

    //------------------------------------------------------
    private Samples samples;

    private int numMode;
    private int nomMode;
    
    //------------------------------------------------------
    private ArrayList<double[]> numericalData;
    private ArrayList<Object[]> nominalData;
    private ArrayList<Object> nominalDataTypes;
    private ArrayList<String> classTypes;

    //------------------------------------------------------
    private ArrayList<Integer> nomAttribsInd;
    private ArrayList<Integer> numAttribsInd;
    private ArrayList<Integer> missValSamples;

    private double[] numFillers;
    private Attribute[] nomFillers;

    private int numAttrNo;
    private int nomAttrNo;

    private int classInd;
    //------------------------------------------------------

    public SOMSamplesLoader(Samples samples, boolean normalizeVectors) {

        this.samples = samples;

        this.numMode = -1;
        this.nomMode = -1;

        this.normalizeVectors = normalizeVectors;

        classInd = samples.getClassAttributeIndex();

        checkNominalAttribs();
        checkMissingValues();
        evalMissingDataFillers();
        processSamples();
    }

    public SOMSamplesLoader(Samples samples, int numMode, int nomMode,
            boolean normalizeVectors) {

        this.samples = samples;

        this.numMode = numMode;
        this.nomMode = nomMode;

        this.normalizeVectors = normalizeVectors;

        classInd = samples.getClassAttributeIndex();

        checkNominalAttribs();
        checkMissingValues();
        evalMissingDataFillers();
        processSamples();
    }

   //------------------------------------------------------
    private void processSamples(){
        numericalData = new ArrayList<double[]>();
        nominalData = new ArrayList<Object[]>();
        classTypes = new ArrayList<String>();

        for(int s=0; s<samples.size(); s++){
            if(!(numMode == -1 && missValSamples.contains(s)))
                processSample(samples.get(s));
        }
    }

    private void processSample(Sample sample){
        double[] numVector = new double[this.numAttrNo];
        Object[] nomVector = new Attribute[this.nomAttrNo];
        int ind;

        for(int a=0; a<numAttrNo; a++){
            ind = numAttribsInd.get(a);

            if(sample.get(ind) == null){
                numVector[a] = numFillers[a];
            }else{
                numVector[a] = (Double)sample.get(ind);
            }
        }

        for(int a=0; a<nomAttrNo; a++){
            ind = nomAttribsInd.get(a);

            if(sample.get(ind) == null){
                nomVector[a] = nomFillers[a];
            }else{
                nomVector[a] = sample.get(ind);
            }
        }

        String classType = (String)sample.get(this.classInd);

        numericalData.add(numVector);
        nominalData.add(nomVector);
        classTypes.add(classType);
    }

    //------------------------------------------------------
    private void checkNominalAttribs(){
        nomAttribsInd = new ArrayList<Integer>();
        numAttribsInd = new ArrayList<Integer>();
        nominalDataTypes = new ArrayList<Object>();

        ArrayList<Attribute> attributes = samples.getAtts();

        for(int a=0; a<attributes.size(); a++){
            if(a != this.classInd){
                if(attributes.get(a).isNominal()){
                    nomAttribsInd.add(a);
                    nominalDataTypes.add(
                            attributes.get(a).getInterpretation(a));
                }
                else
                    numAttribsInd.add(a);
            }
        }

        nomAttrNo = nomAttribsInd.size();
        numAttrNo = numAttribsInd.size();
    }

    //------------------------------------------------------
    private void checkMissingValues(){
        missValSamples = new ArrayList<Integer>();

        Sample samp;
        boolean miss;
        Object attr;

        for(int s=0; s<samples.size(); s++){
            samp = samples.get(s);
            miss = false;

            for(int a=0; a<samp.size() && !miss; a++){
                attr = samp.get(a);

                if(attr == null){
                    miss = true;
                    missValSamples.add(s);
                }
            }
        }
    }

    //------------------------------------------------------
    private void evalMissingDataFillers(){
        numFillers = new double[numAttrNo];
        nomFillers = new Attribute[nomAttrNo];

        if(numMode == NUM_MFREQ_MISSING_VAL){
            evalFreqNumFillers();
        }else if(numMode == NUM_AVR_MISSING_VAL_ARITHM){
            evalAvrNumFillersArithm();
        }else if(numMode == NUM_AVR_MISSING_VAL_GEOM){
            evalAvrNumFillersGeom();
        }else if(numMode == NUM_AVR_MISSING_VAL_SQR){
            evalAvrNumFillersSqr();
        }else if(numMode == NUM_AVR_MISSING_VAL_HARM){
            evalAvrNumFillersHarm();
        }

        if(nomMode == NOM_MFREQ_MISSING_VAL){
            evalFreqNomFillers();
        }
    }

    //------------------------------------------------------
    private void evalAvrNumFillersArithm(){
        double[] avr = new double[this.numAttrNo];

        for(int s=0; s<samples.size(); s++){
            Sample samp = samples.get(s);
            for(int i=0; i<numAttribsInd.size(); i++){
                avr[i] += (Double)samp.get(numAttribsInd.get(i));
            }
        }

        for(int i=0; i<numAttribsInd.size(); i++)
            avr[i] = avr[i]/(double)samples.size();

        numFillers = avr;
    }

    //------------------------------------------------------
    private void evalAvrNumFillersGeom(){
        double[] avr = new double[this.numAttrNo];

        for(int s=0; s<samples.size(); s++){
            Sample samp = samples.get(s);
            for(int i=0; i<numAttribsInd.size(); i++){
                avr[i] = avr[i]*(Double)samp.get(numAttribsInd.get(i));
            }
        }

        for(int i=0; i<numAttribsInd.size(); i++)
            avr[i] = java.lang.Math.pow(avr[i], 1/(double)samples.size());

        numFillers = avr;
    }

    //------------------------------------------------------
    private void evalAvrNumFillersSqr(){
        double[] avr = new double[this.numAttrNo];

        for(int s=0; s<samples.size(); s++){
            Sample samp = samples.get(s);
            for(int i=0; i<numAttribsInd.size(); i++){
                avr[i] += java.lang.Math.pow(
                        (Double)samp.get(numAttribsInd.get(i)), 2);
            }
        }

        for(int i=0; i<numAttribsInd.size(); i++)
            avr[i] = java.lang.Math.sqrt(avr[i]/(double)samples.size());

        numFillers = avr;
    }

    //------------------------------------------------------
    private void evalAvrNumFillersHarm(){
        double[] avr = new double[this.numAttrNo];

        for(int s=0; s<samples.size(); s++){
            Sample samp = samples.get(s);
            for(int i=0; i<numAttribsInd.size(); i++){
                avr[i] +=1/(Double)samp.get(numAttribsInd.get(i));
            }
        }

        for(int i=0; i<numAttribsInd.size(); i++)
            avr[i] = (double)samples.size()/avr[i];

        numFillers = avr;
    }

    //------------------------------------------------------
    private void evalFreqNumFillers(){
        double[] freq = new double[this.numAttrNo];

        ArrayList<Double> values = new ArrayList<Double>();
        ArrayList<Integer> valCounters = new ArrayList<Integer>();

        for(int a=0; a<numAttrNo; a++){
            int attrNo = this.numAttribsInd.get(a);
            values.clear();
            valCounters.clear();

            for(int s=0; s<samples.size(); s++){
                double val = Double.parseDouble(
                        ((Attribute)samples.get(s).get(attrNo)).getName());

                if(!values.contains(val)){
                    values.add(val);
                    valCounters.add(1);
                }else{
                    valCounters.set(
                            values.indexOf(val),
                            valCounters.get(values.indexOf(val))+1
                            );
                }
            }

            int ind = 0;
            for(int i=1; i<valCounters.size(); i++){
               if(valCounters.get(i) > valCounters.get(ind))
                   ind = i;
            }

            freq[a] = values.get(ind);
        }

        numFillers = freq;
    }

    //------------------------------------------------------
    private void evalFreqNomFillers(){
        Attribute[] freq = new Attribute[this.nomAttrNo];

        ArrayList<Attribute> values = new ArrayList<Attribute>();
        ArrayList<Integer> valCounters = new ArrayList<Integer>();

        for(int a=0; a<nomAttrNo; a++){
            int attrNo = this.nomAttribsInd.get(a);
            values.clear();
            valCounters.clear();

            for(int s=0; s<samples.size(); s++){
                Attribute val = (Attribute)samples.get(s).get(attrNo);

                int ind =-1;

                for(int i=0; i<values.size() && ind==-1; i++){
                    if(values.get(i).getName().equals(val.getName())){
                        ind = i;
                    }
                }

                if(ind == -1){
                    values.add(val);
                    valCounters.add(1);
                }else{
                    valCounters.set(ind, valCounters.get(ind)+1);
                }
            }

            int ind = 0;
            for(int i=1; i<valCounters.size(); i++){
               if(valCounters.get(i) > valCounters.get(ind))
                   ind = i;
            }

            freq[a] = values.get(ind);
        }
        nomFillers = freq;
    }

    private ArrayList<double[]> getNormalizedNumericalData(){
       ArrayList <double[]> data = new ArrayList<double[]>();

       for(int d=0; d<numericalData.size(); d++)
           data.add(SOMNormalizer.normalizeVector(numericalData.get(d)));

       return data;
    }

    private double getMaxNumNormalizedValue(int a){
        ArrayList<double[]> norm = getNormalizedNumericalData();
        double result = norm.get(0)[a];

        for(int i=0; i<norm.size(); i++){
            if(norm.get(i)[a] > result)
                result = norm.get(i)[a];
        }

        return result;
    }

    private double getMinNumNormalizedValue(int a){
        ArrayList<double[]> norm = getNormalizedNumericalData();
        double result = norm.get(0)[a];

        for(int i=0; i<norm.size(); i++){
            if(norm.get(i)[a] < result)
                result = norm.get(i)[a];
        }

        return result;
    }

    public double getNotNormalizedMaxNumValue(int a){
        double result = numericalData.get(0)[a];

        for(int i=0; i<numericalData.size(); i++){
            if(numericalData.get(i)[a] > result)
                result = numericalData.get(i)[a];
        }

        return result;
    }

    public double getNotNormalizedMinNumValue(int a){
        double result = numericalData.get(0)[a];

        for(int i=0; i<numericalData.size(); i++){
            if(numericalData.get(i)[a] < result)
                result = numericalData.get(i)[a];
        }

        return result;
    }

    private double getMaxNumNormalizedValue(){
        double result = this.getMaxNumNormalizedValue(0);
        double val;

        for(int ia=1; ia<this.numAttrNo; ia++){
            val = this.getMaxNumNormalizedValue(ia);
            if(val>result)
                result = val;
        }

        return result;
    }

    private double getMinNumNormalizedValue(){
        double result = this.getMinNumNormalizedValue(0);
        double val;

        for(int ia=1; ia<this.numAttrNo; ia++){
            val = this.getMinNumNormalizedValue(ia);
            if(val<result)
                result = val;
        }

        return result;
    }

    public double getNotNormalizedMaxNumValue(){
        double result = this.getNotNormalizedMaxNumValue(0);
        double val;

        for(int ia=1; ia<this.numAttrNo; ia++){
            val = this.getNotNormalizedMaxNumValue(ia);
            if(val>result)
                result = val;
        }

        return result;
    }

    public double getNotNormalizedMinNumValue(){
        double result = this.getNotNormalizedMinNumValue(0);
        double val;

        for(int ia=1; ia<this.numAttrNo; ia++){
            val = this.getNotNormalizedMinNumValue(ia);
            if(val<result)
                result = val;
        }

        return result;
    }

    //------------------------------------------------------
    public int getNumAttrNumber(){
        return this.numAttrNo;
    }

    public int getNomAttrNumber(){
        return this.nomAttrNo;
    }

    public ArrayList<double[]> getNumericalData(){
        if(normalizeVectors)
            return this.getNormalizedNumericalData();
        else
            return this.numericalData;
    }

    public ArrayList<Object[]> getNominalData(){
        return this.nominalData;
    }

    public ArrayList<Object> getNominalAttribsInterpretation(){
        return this.nominalDataTypes;
    }

    public ArrayList<String> getClassNames(){
        return this.classTypes;
    }

    public int getSampsWithMisValNumber(){
        return this.missValSamples.size();
    }

    public Samples getSamples(){
        return this.samples;
    }

    public double getMaxNumValue(){
        if(normalizeVectors)
            return this.getMaxNumNormalizedValue();
        else
            return this.getNotNormalizedMaxNumValue();
    }

    public double getMinNumValue(){
        if(normalizeVectors)
            return this.getMinNumNormalizedValue();
        else
            return this.getNotNormalizedMinNumValue();
    }

    public double getMaxNumValue(int a){
        if(normalizeVectors)
            return this.getMinNumNormalizedValue(a);
        else
            return this.getNotNormalizedMaxNumValue(a);
    }

    public double getMinNumValue(int a){
        if(normalizeVectors)
            return this.getMaxNumNormalizedValue(a);
        else
            return this.getNotNormalizedMinNumValue(a);
    }

}
