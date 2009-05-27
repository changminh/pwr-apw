/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import apw.core.Samples;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nitric
 */
public class Rule {
    protected ArrayList<Complex> ifClause = new ArrayList<Complex>();
    protected ArrayList<Complex> thenClause = new ArrayList<Complex>();
    protected String name;
    protected RuleTranslator translator;
    protected Prolog prolog = new Prolog();
    protected Samples samples;
    protected String ifClauseQuery;
    protected String thenClauseQuery;

    public Rule(String name, Samples samples) {
        this.name = name;
        this.samples = samples;
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

    public void translate() {
        translator = new RuleTranslator(this, samples);
        prolog = new Prolog();
        String s = translator.prologRepresentation();
        try {
            prolog.setTheory(new Theory(translator.prologRepresentation()));
        } catch (InvalidTheoryException ex) {
            Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
        }
        ifClauseQuery = name + RuleTranslator.ifClausePostfix;
        thenClauseQuery = name + RuleTranslator.thenClausePostfix;
    }

    public boolean covers(Term[] arguments) {
        Struct query = new Struct(ifClauseQuery, arguments);
        SolveInfo info = prolog.solve(query);
        return info.isSuccess();
    }

    public boolean covers(Object[] arguments) {
        for (Complex c : ifClause)
            if (c.covers(arguments))
                return true;
        return false;
    }

    public boolean isSatisfiedBy(Term[] arguments) {
        Struct query = new Struct(thenClauseQuery, arguments);
        SolveInfo info = prolog.solve(query);
        return info.isSuccess();
    }
}
