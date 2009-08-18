package apw.art2a;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author nitric
 */
public class Cluster {

    private HashSet<Prototype> prototypes = new HashSet<Prototype>();
    private int index = -1;
    
    private static int count = 0;

    private Cluster() { }

    public static Cluster getNewCluster(ArrayList<Prototype> list) {
        Cluster result = new Cluster();
        result.addPrototypes(list);
        result.index = count++;
        return result;
    }

    public int getIndex() {
        return index;
    }

    private boolean containsPrototype(Prototype p) {
        return prototypes.contains(p);
    }

    public boolean containsCommonPrototypes(Cluster c) {
        for (Prototype p : prototypes)
            if (c.containsPrototype(p))
                return true;
        return false;
    }

    public boolean containsCommonPrototypes(ArrayList<Prototype> list) {
        for (Prototype p : prototypes)
            if (list.contains(p))
                return true;
        return false;
    }

    public boolean isSubsetOf(Cluster other) {
        for (Prototype p : prototypes)
            if (!other.containsPrototype(p))
                return false;
        return true;
    }

    public boolean isSupersetOf(Cluster other) {
        return other.isSubsetOf(this);
    }

    public boolean theSame(Cluster other) {
        return isSupersetOf(other) && isSubsetOf(other);
    }
    
    public void addPrototypes(ArrayList<Prototype> list) {
        prototypes.addAll(list);
    }

    public Cluster merge(Cluster c) {
        Cluster result = new Cluster();
        result.index = this.index < c.index ? this.index : c.index;
        result.prototypes.addAll(this.prototypes);
        result.prototypes.addAll(c.prototypes);
        return result;
    }

    public HashSet<Prototype> getPrototypes() {
        return prototypes;
    }
}
