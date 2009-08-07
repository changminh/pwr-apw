package apw.myART2.gui;

/**
 *
 * @author nitric
 */
public enum ViewType {
    CLOSEST_INSTANCE ("Closest instance"),
    WEIGHTS ("Prototype weights"),
    SIMULATED_INSTANCE ("Simulated instance");

    private String humanReadable;

    ViewType(String s) {
        humanReadable = s;
    }

    @Override
    public String toString() {
        return humanReadable;
    }

}
