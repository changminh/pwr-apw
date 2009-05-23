package apw.augmentedLearning.logic;

import apw.core.Attribute;
import apw.core.Samples;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 *
 * @author Nitric
 */
public class RuleTranslator {
    private Rule rule;
    private Samples samples;
    private ArrayList<Attribute> attributes;

    private ClauseTranslator ifClause;
    private ClauseTranslator thenClause;

    public RuleTranslator(Rule rule, Samples samples) {
        this.rule = rule;
        this.samples = samples;
        ifClause = new ClauseTranslator(rule, samples, true);
        thenClause = new ClauseTranslator(rule, samples, false);
        attributes = samples.getAtts();
    }

    public String convertRule() {
        Set<Integer> importantAttributes = new HashSet<Integer>();
        importantAttributes.addAll(ifClause.attributesForClause);
        importantAttributes.addAll(thenClause.attributesForClause);
        StringBuilder sb = new StringBuilder(rule.getName());
        sb.append("(");
        for (int i = 0; i < attributes.size(); i++) {
            if (importantAttributes.contains(i))
                sb.append(attributes.get(i).getName() + ", ");
            else
                sb.append("_, ");
        }
        deleteLast2Letters(sb);
        sb.append(") :- \n");
        // 'Call' the 'thenComplex':
        sb.append("\t" + callClause(thenClause));
        sb.append(" ; \\+ (");
        // 'Call' the 'ifComplex':
        sb.append(callClause(ifClause) + ")\n.");
        return sb.toString();
    }

    /**
     * Build string that is equivalent to the clause's call in Prolog, assuming the attribute's names
     * are still the same as in the 'Samples.attributes' names
     * @param ct
     * @return
     */
    private String callClause(ClauseTranslator ct) {
        StringBuilder sb = new StringBuilder(ct.getClauseName() + "(");
        for (int i : ct.attributesForClause)
            sb.append(attributes.get(i).getName() + ", ");
        deleteLast2Letters(sb);
        return sb.append(")").toString();
    }

    public String prologRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% \n\n");
        sb.append("% Rule name: " + rule.getName() + " \n");
        sb.append("% If-complexes: \n");
        sb.append(ifClause.convertClause() + "\n");
        sb.append("% Then-complexes: \n");
        sb.append(thenClause.convertClause() + "\n");
        sb.append("% Whole rule: \n");
        sb.append(convertRule() + "\n");
        sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% \n\n");
        return sb.toString();
    }

    public static void deleteLast2Letters(StringBuilder sb) {
        sb.delete(sb.length() - 2, sb.length());
    }
}

class ClauseTranslator {
    boolean isAssumptionClause;
    Rule rule;
    String ruleName;
    Samples samples;
    int attributeCount;
    Set<Integer> attributesForClause = new HashSet<Integer>();
    ArrayList<Set<Integer>> attributesForComplexes = new ArrayList<Set<Integer>>();
    String clauseName;
    String complexNamePrefix;
    ArrayList<Complex> complexes;
    ArrayList<Attribute> attributes;

    public ClauseTranslator(Rule rule, Samples samples, boolean isAssumptionClause) {
        this.isAssumptionClause = isAssumptionClause;
        this.rule = rule;
        this.samples = samples;
        ruleName = rule.getName();
        attributes = samples.getAtts();
        attributeCount = attributes.size();

        if (isAssumptionClause) {
            clauseName = ruleName + "___ifClause";
            complexNamePrefix = ruleName + "___if_";
            complexes = rule.getIfClause();
        }
        else {
            clauseName = ruleName + "___thenClause";
            complexNamePrefix = ruleName + "___then_";
            complexes = rule.getThenClause();
        }
    }

    private String convertComplex(Complex c, String complexName) {
        HashSet<Integer> set = new HashSet<Integer>();
        attributesForComplexes.add(set);
        boolean correct = false;
        StringBuilder sb = new StringBuilder(complexName + "(");
        for (int i = 0; i < attributeCount; i++)
            if (!c.getSelector(i).isUniversal()) {
                // First: store the information, that attribute is important for the whole clause
                attributesForClause.add(i);
                set.add(i);
                sb.append(attributes.get(i).getName() + ", ");
                correct = true;
            }
        if (!correct) {
            System.err.println("The complex isn't correct!");
            return null;
        }
        RuleTranslator.deleteLast2Letters(sb);
        sb.append(") :- \n");
        for (int i : set) {
            sb.append("\t" + convertSelector(c.getSelector(i), attributes.get(i).getName()) + ",\n");
        }
        RuleTranslator.deleteLast2Letters(sb);
        sb.append("\n.");
        return sb.toString();
    }

    public String convertClause() {
        StringBuilder sb = new StringBuilder();
        int id = 1;
        for (Complex c: complexes) {
            sb.append(convertComplex(c, complexNamePrefix + id++));
            sb.append("\n");
        }
        sb.append("\n\n");
        sb.append(isAssumptionClause ? "% if-clause: \n" : "% then-clause: \n");
        sb.append(clauseName + "(");
        for (int i : attributesForClause) {
            sb.append(samples.getAtts().get(i).getName() + ", ");
        }
        RuleTranslator.deleteLast2Letters(sb);
        sb.append(") :- \n");
        id = 1;
        Set<Integer> set;
        Iterator iter;
        for (int j = 0; j < complexes.size(); j++) {
            sb.append("\t" + complexNamePrefix + (j + 1) + "(");
            set = attributesForComplexes.get(j);
            iter = set.iterator();
            while (iter.hasNext())
                sb.append(samples.getAtts().get((Integer) iter.next()).getName() + ", ");
            RuleTranslator.deleteLast2Letters(sb);
            sb.append(");\n");
        }
        RuleTranslator.deleteLast2Letters(sb);
        sb.append("\n.\n\n");
        return sb.toString();
    }

    public String getClauseName() {
        return clauseName;
    }

    private String convertSelector(Selector sel, String name) {
        if (sel.isForNominalAttribute())
            return convertSelectorForNominal((SelectorForNominal)sel, name);
        else
            return convertSelectorForNumber((SelectorForNumber)sel, name);
    }

    private String convertSelectorForNominal(SelectorForNominal sel, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("member(" + name + ", [");
        for (String s : sel.values)
            sb.append(StringConverter.convertToAtom(s) + ", ");
        RuleTranslator.deleteLast2Letters(sb);
        return sb.append("])").toString();
    }

    private String convertSelectorForNumber(SelectorForNumber sel, String name) {
        StringBuilder sb = new StringBuilder();
        switch (sel.type) {
            case ALL_VALUES:
                return "true";
            case BELONGS_RIGHT_INCLUDING:
                sb.append("" + sel.getLowerLimit() + " < " + name + ", ");
                sb.append(name + " =< " + sel.getUpperLimit());
                break;
            case EQUAL:
                sb.append(name + " =:= " + sel.getUpperLimit());
                break;
            /* case GREATER_OR_EQUAL:
                sb.append(name + " >= " + sel.getLowerLimit());
                break; */
            case GREATER_THAN:
                sb.append(name + " > " + sel.getLowerLimit());
                break;
            case LOWER_OR_EQUAL:
                sb.append(name + " =< " + sel.getUpperLimit());
                break;
            /* case LOWER_THAN:
                sb.append(name + " < " + sel.getUpperLimit());
                break; */
            case NONE_VALUE:
                return "fail";
        }
        return sb.toString();
    }
}
