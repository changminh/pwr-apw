/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

import alice.tuprolog.Term;
import alice.tuprolog.Var;
import apw.core.Attribute;
import apw.core.Samples;
import apw.core.Sample;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

/**
 *
 * @author nitric
 */
public class DataFile {
    private String attributesSeparator = "";                                    // OK
    private String startOfComment = "";                                         // OK
    private String missingValueTag = "";                                        // OK
    private Character fractionalSeparator = '.';                                // OK
    private File dataFile;                                                      // OK
    private int attributesCount = -1;                                           // OK
    private ArrayList<String> attributesNames;                                  //
    private ArrayList<String[]> records = null;                                 // OK
    private Vector<Integer> nominals = new Vector<Integer>();                   //
    private ArrayList<HashSet<String>> nominalValues = new ArrayList<HashSet<String>>();    //
    private int classAttributeIndex = -1;                                       //
    private Object[][] rawObjects;                                              //
    private HashSet<Integer> samplesWithNull;                                   //

    public DataFile() { }

    public DataFile(Samples samples) {
        attributesCount = samples.getAtts().size();
        obtainClassAttributeIndex(samples);
        obtainAttributes(samples);
        obtainRawObjectsAndSamplesWithNull(samples);
        collectNominalValues();
    }

    /**
     * Allows to check whether the attribute with number 'id' is nominal (returns true) or numerical
     * (returns false).
     * @param id identity number of attribute
     * @return true, if the attribute with specified id is nominal, false otherwise.
     */
    public boolean isNominalAttribute(int id) {
        return nominals.contains(id);
    }

    public ArrayList<HashSet<String>> getNominalValues() {
        return nominalValues;
    }

    /**
     * Returns set of nominal values of the attribute of particular id. The id is id of the attribute
     * (in global meaning, e.g. if first nominal attribute is attribute number 3, then in fact
     * 'getNominalValuesOfAttribute(0)' returns 'null', while 'getNominalValuesOfAttribute(3)' returns
     * the values of the attribute number 3.
     * @param id
     * @return set of attribute's possible values
     */
    public HashSet<String> getNominalValuesOfAttribute(int id) {
        if (nominals.contains(id))
            return nominalValues.get(nominals.indexOf(id));
        else
            return null;
    }

    public HashSet<Integer> getSamplesWithNull() {
        return samplesWithNull;
    }

    public void setSamplesWithNull(HashSet<Integer> samplesWithNull) {
        this.samplesWithNull = samplesWithNull;
    }

    public void setNominalValues(ArrayList<HashSet<String>> nominalValues) {
        this.nominalValues = nominalValues;
    }

    public Vector<Integer> getNominals() {
        return nominals;
    }

    public void setNominals(Vector<Integer> nominals) {
        this.nominals = nominals;
    }

    public ArrayList<String[]> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<String[]> records) {
        this.records = records;
    }

    public ArrayList<String> getAttributesNames() {
        return attributesNames;
    }

    public void setAttributesNames(ArrayList<String> attributesNames) {
        this.attributesNames = attributesNames;
    }

    public File getDataFile() {
        return dataFile;
    }

    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    public int getAttributesCount() {
        return attributesCount;
    }

    public void setAttributesCount(int iloscAtrybutow) {
        this.attributesCount = iloscAtrybutow;
    }

    public String getStartOfComment() {
        return startOfComment;
    }

    public void setStartOfComment(String poczatekKomentarza) {
        this.startOfComment = poczatekKomentarza;
    }
    
    public String getAttributesSeparator() {
        return attributesSeparator;
    }

    public void setAttributesSeparator(String separatorAtrybutow) {
        this.attributesSeparator = separatorAtrybutow;
    }

    public Character getFractionalSeparator() {
        return fractionalSeparator;
    }

    public void setFractionalSeparator(Character separatorUlamkaICzesciCalkowitej) {
        this.fractionalSeparator = separatorUlamkaICzesciCalkowitej;
    }

    public String getMissingValueTag() {
        return missingValueTag;
    }

    public void setMissingValueTag(String znacznikBrakuWartosci) {
        this.missingValueTag = znacznikBrakuWartosci;
    }

    public int getClassAttributeIndex() {
        return classAttributeIndex;
    }

    public void setClassAttributeIndex(int classAttributeIndex) {
        this.classAttributeIndex = classAttributeIndex;
    }

    public Object[][] getRawObjects() {
        return rawObjects;
    }

    public void setRawObjects(Object[][] rawObjects) {
        this.rawObjects = rawObjects;
    }

    private void obtainAttributes(Samples samples) {
        attributesNames = new ArrayList<String>();
        ArrayList<Attribute> atts = samples.getAtts();
        Attribute att;
        for (int i = 0; i < attributesCount; i++) {
            attributesNames.add((att = atts.get(i)).getName());
            if (att.isNominal())
                nominals.add(i);
        }
    }

    private void obtainClassAttributeIndex(Samples samples) {
        int i = samples.getClassAttributeIndex();
        if (i < 0 || i >= attributesCount)
            throw new IllegalStateException("Class not set or not set properly.");
        classAttributeIndex = i;
    }

    private void obtainRawObjectsAndSamplesWithNull(Samples samples) {
        rawObjects = new Object[samples.size()][attributesCount];
        samplesWithNull = new HashSet<Integer>();
        boolean addNull;
        int counter = 0;
        Object[] row;
        Object object = null;
        for (Sample s : samples) {
            addNull = true;
            row = new Object[attributesCount];
            for (int i = 0; i < attributesCount; i++) {
                try {
                    row[i] = object = s.get(i);
                }
                catch (ClassCastException ex) {
                    ex.printStackTrace();
                    return;
                }
                if (object == null && addNull) {
                    samplesWithNull.add(counter);
                    addNull = false;
                }
            }
            rawObjects[counter++] = row;

        }
    }

    private void collectNominalValues() {
        HashSet<String> set;
        String s = null;
        for (int id : nominals) {
            set = new HashSet<String>();
            for (int sample = 0; sample < rawObjects.length; sample++) {
                s = (String) rawObjects[sample][id];
                set.add(s);
            }
            nominalValues.add(set);
        }
    }

     public ArrayList<Term[]> createTerms() {
        ArrayList<Term[]> terms = new ArrayList<Term[]>();
        Term[] term;
        // TODO: Optimization...?
        for (Object [] record : rawObjects) {
            term = new Term[attributesCount];
            for (int i = 0; i < attributesCount; i++) {
                if (record[i] == null)
                    term[i] = new Var("_");
                else
                    if (nominals.contains(i))
                        term[i] = new alice.tuprolog.Struct((String)record[i]);
                    else
                        term[i] = new alice.tuprolog.Double((Double)record[i]);
            }
            terms.add(term);
        }
        return terms;
    }
}
