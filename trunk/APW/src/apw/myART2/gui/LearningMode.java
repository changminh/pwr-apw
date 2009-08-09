package apw.myART2.gui;

/**
 *
 * @author nitric
 */
public enum LearningMode {
    HYBRID ("Hybrid"), INTERACTIVE ("Interactive");

    private String humanReadable;

    private LearningMode(String s) {
        humanReadable = s;
    }

    @Override
    public String toString() {
        return humanReadable;
    }
}
