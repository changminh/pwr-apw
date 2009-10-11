package apw.kohonenSom.util;

import apw.core.Attribute;
import apw.core.Sample;
import apw.core.Samples;
import java.util.ArrayList;

/**
 *
 * @author Cristopher Wadowski
 */
public class SOMSamplesLoader {

    public static final int NUM_EXCL_MISSING_VAL = 0;
    public static final int NUM_MFREQ_MISSING_VAL = 1;
    public static final int NUM_AVR_MISSING_VAL_ARITHM = 2;
    public static final int NUM_AVR_MISSING_VAL_GEOM = 3;
    public static final int NUM_AVR_MISSING_VAL_SQR = 4;
    public static final int NUM_AVR_MISSING_VAL_HARM = 5;


    public static final int NOM_EXCL_MISSING_VAL = 0;
    public static final int NOM_MFREQ_MISSING_VAL = 1;

    private Samples samples;

    private ArrayList<double[]> numericalData;
    private ArrayList<Attribute[]> nominalData;
    private ArrayList<String> classTypes;

    private ArrayList<Integer> nomAttribsInd;
    private ArrayList<Integer> numAttribsInd;
    private ArrayList<Integer> missValSamples;

    private double[] numFillers;
    private Attribute[] nomFillers;

    private int numMode;
    private int nomMode;

    private int numAttrNo;
    private int nomAttrNo;

    //TODO

    public SOMSamplesLoader(Samples samples, int numMode, int nomMode) {

        this.samples = samples;

        this.numMode = numMode;
        this.nomMode = nomMode;

        checkNominalAttribs();
        checkMissingValues();
        evalMissingDataFillers();
        processSamples();
    }

    private void processSamples(){
        //TODO
    }

    private void processSample(Sample sample){
        //TODO
        
    }

    private void checkNominalAttribs(){
        nomAttribsInd = new ArrayList<Integer>();
        numAttribsInd = new ArrayList<Integer>();
        Attribute attr;
        Sample samp = samples.get(0);

        for(int a=0; a<samp.size(); a++){
            attr = (Attribute)samp.get(a);

            if(attr.isNominal()){
                nomAttribsInd.add(a);
            }
            else
                numAttribsInd.add(a);
        }

        nomAttrNo = nomAttribsInd.size();
        numAttrNo = numAttribsInd.size();
    }

    private void checkMissingValues(){
        missValSamples = new ArrayList<Integer>();

        Sample samp;
        boolean miss;
        Attribute attr;

        for(int s=0; s<samples.size(); s++){
            samp = samples.get(s);
            miss = false;

            for(int a=0; a<samp.size() && !miss; a++){
                attr = (Attribute)samp.get(a);

                if(attr.getName() == null){
                    miss = true;
                    missValSamples.add(s);
                }
            }
        }
    }
    
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

    private void evalAvrNumFillersArithm(){
        double[] avr = new double[this.numAttrNo];

        for(int s=0; s<samples.size(); s++){
            Sample samp = samples.get(s);
            for(int i=0; i<numAttribsInd.size(); i++){
                Attribute attr =  (Attribute)samp.get(numAttribsInd.get(i));
                avr[i] += Double.parseDouble(attr.getName());
            }
        }

        for(int i=0; i<numAttribsInd.size(); i++)
            avr[i] = avr[i]/(double)samples.size();

        numFillers = avr;
    }

    private void evalAvrNumFillersGeom(){
        double[] avr = new double[this.numAttrNo];

        for(int s=0; s<samples.size(); s++){
            Sample samp = samples.get(s);
            for(int i=0; i<numAttribsInd.size(); i++){
                Attribute attr =  (Attribute)samp.get(numAttribsInd.get(i));
                avr[i] = avr[i]*Double.parseDouble(attr.getName());
            }
        }

        for(int i=0; i<numAttribsInd.size(); i++)
            avr[i] = java.lang.Math.pow(avr[i], 1/(double)samples.size());

        numFillers = avr;
    }

    private void evalAvrNumFillersSqr(){
        double[] avr = new double[this.numAttrNo];

        for(int s=0; s<samples.size(); s++){
            Sample samp = samples.get(s);
            for(int i=0; i<numAttribsInd.size(); i++){
                Attribute attr =  (Attribute)samp.get(numAttribsInd.get(i));
                avr[i] += java.lang.Math.pow(
                        Double.parseDouble(attr.getName()), 2);
            }
        }

        for(int i=0; i<numAttribsInd.size(); i++)
            avr[i] = java.lang.Math.sqrt(avr[i]/(double)samples.size());

        numFillers = avr;
    }

    private void evalAvrNumFillersHarm(){
        double[] avr = new double[this.numAttrNo];

        for(int s=0; s<samples.size(); s++){
            Sample samp = samples.get(s);
            for(int i=0; i<numAttribsInd.size(); i++){
                Attribute attr =  (Attribute)samp.get(numAttribsInd.get(i));
                avr[i] +=1/(Double.parseDouble(attr.getName()));
            }
        }

        for(int i=0; i<numAttribsInd.size(); i++)
            avr[i] = (double)samples.size()/avr[i];

        numFillers = avr;
    }

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

    public ArrayList<double[]> getNumericalData(){
        return this.numericalData;
    }

    public ArrayList<Attribute[]> getNominalData(){
        return this.nominalData;
    }

    public ArrayList<String> getClassNames(){
        return this.classTypes;
    }

    public ArrayList<double[]> getNormalizedNumericalData(){
       ArrayList <double[]> data = new ArrayList<double[]>();

       for(int d=0; d<numericalData.size(); d++)
           data.add(SOMNormalizer.normalizeVector(numericalData.get(d)));

       return data;
    }
}
