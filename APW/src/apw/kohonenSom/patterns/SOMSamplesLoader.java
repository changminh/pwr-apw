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

    public SOMSamplesLoader(Samples samples) {

        this.samples = samples;

        this.numMode = -1;
        this.nomMode = -1;

        classInd = samples.getClassAttributeIndex();

        checkNominalAttribs();
        checkMissingValues();
        evalMissingDataFillers();
        processSamples();
    }

    public SOMSamplesLoader(Samples samples, int numMode, int nomMode) {

        this.samples = samples;

        this.numMode = numMode;
        this.nomMode = nomMode;

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

    //------------------------------------------------------
    public ArrayList<double[]> getNumericalData(){
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

    public ArrayList<double[]> getNormalizedNumericalData(){
       ArrayList <double[]> data = new ArrayList<double[]>();

       for(int d=0; d<numericalData.size(); d++)
           data.add(SOMNormalizer.normalizeVector(numericalData.get(d)));

       return data;
    }

    public Samples getSamples(){
        return this.samples;
    }

    public double getMaxNumValue(){
        double result = 0;

        for(int i=0; i<numericalData.size(); i++){
            for(int j=0; j<numericalData.get(i).length; j++){
                if(numericalData.get(i)[j] > result)
                    result = numericalData.get(i)[j];
            }
        }

        return result;
    }

    public double getMinNumValue(){
        double result = 0;

        for(int i=0; i<numericalData.size(); i++){
            for(int j=0; j<numericalData.get(i).length; j++){
                if(numericalData.get(i)[j] < result)
                    result = numericalData.get(i)[j];
            }
        }

        return result;
    }

    public double getMaxNumNormalizedValue(){
        double result = 0;

        ArrayList<double[]> norm = getNormalizedNumericalData();

        for(int i=0; i<norm.size(); i++){
            for(int j=0; j<norm.get(i).length; j++){
                if(norm.get(i)[j] > result)
                    result = norm.get(i)[j];
            }
        }

        return result;
    }

    public double getMinNumNormalizedValue(){
        double result = 0;

        ArrayList<double[]> norm = getNormalizedNumericalData();

        for(int i=0; i<norm.size(); i++){
            for(int j=0; j<norm.get(i).length; j++){
                if(norm.get(i)[j] < result)
                    result = norm.get(i)[j];
            }
        }

        return result;
    }

}
