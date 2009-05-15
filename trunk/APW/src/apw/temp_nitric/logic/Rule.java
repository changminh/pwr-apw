/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.temp_nitric.logic;

import java.util.ArrayList;

/**
 *
 * @author Nitric
 */
public class Rule {
    // TODO: Maybe these names are not appropriate? ;)
    protected ArrayList<Complex> ifClause = new ArrayList<Complex>();
    protected ArrayList<Complex> thenClause = new ArrayList<Complex>();
    protected String name;

    public Rule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Complex> getIfClause() {
        return ifClause;
    }

    public void setIfClause(ArrayList<Complex> ifClause) {
        this.ifClause = ifClause;
    }

    public ArrayList<Complex> getThenClause() {
        return thenClause;
    }

    public void setThenClause(ArrayList<Complex> thenClause) {
        this.thenClause = thenClause;
    }

    public void addIfComplex(Complex c) {
        ifClause.add(c);
    }

    public void addThenComplex(Complex c) {
        thenClause.add(c);
    }
}
