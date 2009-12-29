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

    private int numumericAttrNumber;
    private int nominalAttrNumber;

    private int classInd;
    //------------------------------------------------------

    public SOMSamplesLoader(Samples samples,
            boolean normalizeVectors) {

        this.samples = samples;

        this.numMode = -1;
        this.nomMode = -1;

        classInd = samples.getClassAttributeIndex();

        checkNominalAttribs();
        checkMissingValues();
        evalMissingDataFillers();
        processSamples();

        if(normalizeVectors){
            numericalData = normalizeNumericalData();
        }
    }

    public SOMSamplesLoader(
            Samples samples, int numMode,
            int nomMode, boolean normalizeVectors) {

        this.samples = samples;

        this.numMode = numMode;
        this.nomMode = nomMode;

        classInd = samples.getClassAttributeIndex();

        checkNominalAttribs();
        checkMissingValues();
        evalMissingDataFillers();
        processSamples();

        if(normalizeVectors){
            numericalData = normalizeNumericalData();
        }
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
        double[] numVector = new double[this.numumericAttrNumber];
        Object[] nomVector = new Attribute[this.nominalAttrNumber];
        int ind;

        for(int a=0; a<numumericAttrNumber; a++){
            ind = numAttribsInd.get(a);

            if(sample.get(ind) == null){
                numVector[a] = numFillers[a];
            }else{
                numVector[a] = (Double)sample.get(ind);
            }
        }

        for(int a=0; a<nominalAttrNumber; a++){
            ind = nomAttribsInd.get(a);

            if(sample.get(ind) == null){
                nomVector[a] = nomFillers[a];
            }else{
                nomVector[a] = sample.get(ind);
            }
        }

        String classType = sample.get(this.classInd).toString();

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

        nominalAttrNumber = nomAttribsInd.size();
        numumericAttrNumber = numAttribsInd.size();
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
        numFillers = new double[numumericAttrNumber];
        nomFillers = new Attribute[nominalAttrNumber];

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
        double[] avr = new double[this.numumericAttrNumber];

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
        double[] avr = new double[this.numumericAttrNumber];

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
        double[] avr = new double[this.numumericAttrNumber];

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
        double[] avr = new double[this.numumericAttrNumber];

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
        double[] freq = new double[this.numumericAttrNumber];

        ArrayList<Double> values = new ArrayList<Double>();
        ArrayList<Integer> valCounters = new ArrayList<Integer>();

        for(int a=0; a<numumericAttrNumber; a++){
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
        Attribute[] freq = new Attribute[this.nominalAttrNumber];

        ArrayList<Attribute> values = new ArrayList<Attribute>();
        ArrayList<Integer> valCounters = new ArrayList<Integer>();

        for(int a=0; a<nominalAttrNumber; a++){
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

    private ArrayList<double[]> normalizeNumericalData(){
       ArrayList <double[]> data = new ArrayList<double[]>();

       for(int d=0; d<numericalData.size(); d++)
           data.add(SOMNormalizer.normalizeVector(numericalData.get(d)));

       return data;
    }

    //------------------------------------------------------
    public int getNumericAttrNumber(){
        return this.numumericAttrNumber;
    }

    public int getNominalAttrNumber(){
        return this.nominalAttrNumber;
    }

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

    public int getMissingValuesSamplesNumber(){
        return this.missValSamples.size();
    }

    public Samples getSamples(){
        return this.samples;
    }

    public double getMaxNumValue(){
        double[] values = getMaxNumValues();
        double value = values[0];

        for(int ia=0; ia<values.length; ia++){
            if(values[ia] > value){
                value = values[ia];
            }
        }

        return value;
    }

    public double getMinNumValue(){
        double[] values = getMinNumValues();
        double value = values[0];

        for(int ia=0; ia<values.length; ia++){
            if(values[ia] < value){
                value = values[ia];
            }
        }

        return value;
    }

    public double[] getMaxNumValues(){
        double[] values = new double[numumericAttrNumber];

        for(int ia=0; ia<this.numumericAttrNumber; ia++){
            values[ia] = getMaxNumValue(ia);
        }

        return values;
    }

    public double[] getMinNumValues(){
        double[] values = new double[numumericAttrNumber];

        for(int ia=0; ia<this.numumericAttrNumber; ia++){
            values[ia] = getMinNumValue(ia);
        }

        return values;
    }

    public double getMaxNumValue(int a){
        double value = this.numericalData.get(0)[a];

        for(int ip=1; ip<numericalData.size(); ip++){
            if(numericalData.get(ip)[a] > value){
                value = numericalData.get(ip)[a];
            }
        }

        return value;
    }

    public double getMinNumValue(int a){
        double value = this.numericalData.get(0)[a];

        for(int ip=1; ip<numericalData.size(); ip++){
            if(numericalData.get(ip)[a] < value){
                value = numericalData.get(ip)[a];
            }
        }

        return value;
    }

}
