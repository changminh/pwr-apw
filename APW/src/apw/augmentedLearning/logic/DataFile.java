/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

/**
 *
 * @author nitric
 */
public class DataFile {
    private String attributesSeparator = "";
    private String startOfComment = "";
    private String missingValueTag = "";
    private Character fractionalSeparator = '.';
    private File dataFile;
    private int attributesCount = -1;
    private ArrayList<String> attributesNames;
    private ArrayList<String[]> records = null;
    private Vector<Integer> nominals = new Vector<Integer>();
    private ArrayList<HashSet<String>> nominalValues = new ArrayList<HashSet<String>>();

    public ArrayList<HashSet<String>> getNominalValues() {
        return nominalValues;
    }

    public HashSet<String> getNominalValuesOfAttribute(int id) {
        if (nominals.contains(id))
            return nominalValues.get(nominals.indexOf(id));
        else
            return null;
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
}
