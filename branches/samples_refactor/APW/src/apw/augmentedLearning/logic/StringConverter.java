package apw.augmentedLearning.logic;

/**
 *
 * @author Nitric
 */
public class StringConverter {
    public static String convertToAtom(String s) {
        /* 
        String[] tokens = s.split(" ");
        if (tokens.length == 0)
            throw new IllegalArgumentException("Empty string or sth: '" + s +"'");
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(tokens[0].charAt(0)) + tokens[0].substring(1));
        for (int i = 1; i < tokens.length; i++) {
            sb.append(Character.toUpperCase(tokens[i].charAt(0)) + tokens[i].substring(1));
        }
        return sb.toString(); */
        
        // AAAAaaaaaaaaa tam :P
        return "'" + s + "'";
    }

    public static String convertToVariable(String s) {
        String[] tokens = s.split(" ");
        if (tokens.length == 0)
            throw new IllegalArgumentException("Empty string or sth: '" + s +"'");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.length; i++)
            sb.append(Character.toUpperCase(tokens[i].charAt(0)) + tokens[i].substring(1));
        return sb.toString();
    }
}
