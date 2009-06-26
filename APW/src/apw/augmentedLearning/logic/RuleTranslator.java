package apw.augmentedLearning.logic;

import apw.core.Attribute;
import apw.core.Samples;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



/**
 *
 * @author Nitric
 */
public class RuleTranslator {
    public static final String ifClausePostfix = "___ifClause";
    public static final String thenClausePostfix = "___thenClause";
    private Rule rule;
    private Samples samples;
    private ArrayList<String> attsNames = new ArrayList<String>();
    private Set<Integer>importantAttributes = new HashSet<Integer>();
    private ClauseTranslator ifClause;
    private ClauseTranslator thenClause;
    private String prologRepresentation;

    public RuleTranslator(Rule rule, Samples samples) {
        this.rule = rule;
        this.samples = samples;
        for (Attribute att : samples.getAtts())
            attsNames.add(convertToAtom(att.getName()));
        ifClause = new ClauseTranslator(rule, attsNames, true);
        thenClause = new ClauseTranslator(rule, attsNames, false);
        prologRepresentation();
    }

    private String convertRule() {
        importantAttributes.addAll(ifClause.attributesForClause);
        importantAttributes.addAll(thenClause.attributesForClause);
        StringBuilder sb = new StringBuilder(rule.getName());
        sb.append("(");
        for (int i = 0; i < attsNames.size(); i++) {
            if (importantAttributes.contains(i))
                sb.append(attsNames.get(i) + ", ");
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
        for (int i = 0; i < attsNames.size(); i++) {
            if (ct.attributesForClause.contains(i))
                sb.append(attsNames.get(i) + ", ");
            else
                sb.append("_, ");
        }
        deleteLast2Letters(sb);
        return sb.append(")").toString();
    }

    public String prologRepresentation() {
        if (prologRepresentation == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% \n\n");
            sb.append("% Rule name: " + rule.getName() + " \n");
            sb.append("% If-complexes: \n");
            sb.append(ifClause.convertClause() + "\n");
            sb.append("% Then-complexes: \n");
            sb.append(thenClause.convertClause() + "\n");
            sb.append("% Whole rule: \n");
            sb.append(convertRule() + "\n");
            sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% \n\n");
            prologRepresentation = sb.toString();
        }
        return prologRepresentation;
    }

    public static void deleteLast2Letters(StringBuilder sb) {
        sb.delete(sb.length() - 2, sb.length());
    }

    public static String convertToAtom(String s) {
        return "'" + s + "'";
    }
}

class ClauseTranslator {
    boolean isAssumptionClause;
    Rule rule;
    String ruleName;
    ArrayList<String> attsNames;
    int attributeCount;
    Set<Integer> attributesForClause = new HashSet<Integer>();
    ArrayList<Set<Integer>> attributesForComplexes = new ArrayList<Set<Integer>>();
    String clauseName;
    String complexNamePrefix;
    ArrayList<Complex> complexes;

    public ClauseTranslator(Rule rule, ArrayList<String> attsNames, boolean isAssumptionClause) {
        this.isAssumptionClause = isAssumptionClause;
        this.rule = rule;
        ruleName = rule.getName();
        this.attsNames = attsNames;
        attributeCount = attsNames.size();

        if (isAssumptionClause) {
            clauseName = ruleName + RuleTranslator.ifClausePostfix;
            complexNamePrefix = ruleName + "___if_";
            complexes = rule.getIfClause();
        }
        else {
            clauseName = ruleName + RuleTranslator.thenClausePostfix;
            complexNamePrefix = ruleName + "___then_";
            complexes = rule.getThenClause();
        }
    }

    /**
     * Only for the purposes of complexes translation!
     */
    public ClauseTranslator(int attributeCount) {
        this.attributeCount = attributeCount;
    }

    public String convertComplex(Complex c, String complexName) {
        HashSet<Integer> set = new HashSet<Integer>();
        attributesForComplexes.add(set);
        // We must check whether complex has at least one non-universal selector.
        boolean correct = false;
        StringBuilder sb = new StringBuilder(complexName + "(");
        for (int i = 0; i < attributeCount; i++) {
            if (!c.getSelector(i).isUniversal()) {
                // First: store the information, that attribute is important for the whole clause
                attributesForClause.add(i);
                set.add(i);
                sb.append(attsNames.get(i) + ", ");
                correct = true;
            }
            else {
                sb.append("_, ");
            }
        }
        if (!correct) {
            System.out.println("!!!!!The complex isn't correct, all selectors are universal!!!!!");
            return null;
        }
        RuleTranslator.deleteLast2Letters(sb);
        sb.append(") :- \n");
        for (int i : set) {
            sb.append("\t" + convertSelector(c.getSelector(i), attsNames.get(i)) + ",\n");
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
        for (int i = 0; i < attributeCount; i++) {
            if (attributesForClause.contains(i))
                sb.append(attsNames.get(i) + ", ");
            else
                sb.append("_, ");
        }
        RuleTranslator.deleteLast2Letters(sb);
        sb.append(") :- \n");
        id = 1;
        Set<Integer> set;
        for (int j = 0; j < complexes.size(); j++) {
            sb.append("\t" + complexNamePrefix + (j + 1) + "(");
            set = attributesForComplexes.get(j);
            for (int i = 0; i < attributeCount; i++)
                if (set.contains(i))
                    sb.append(attsNames.get(i) + ", ");
                else
                    sb.append("_, ");
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
            case GREATER_THAN:
                sb.append(name + " > " + sel.getLowerLimit());
                break;
            case LOWER_OR_EQUAL:
                sb.append(name + " =< " + sel.getUpperLimit());
                break;
            case NONE_VALUE:
                return "fail";
        }
        return sb.toString();
    }
}
