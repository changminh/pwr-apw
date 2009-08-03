package apw.myART2;

/**
 *
 * @author nitric
 */
public class Instance {
    private double[] vector;
    private int id;

    public Instance(double[] v, int id) {
        this.vector = v;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] d) {
        vector = d;
    }

    public double at(int index) {
        return vector[index];
    }

    public void print() {
        System.out.print("Instance " + id + ": ");
        for (double d : vector)
            System.out.print(d + " ");
        System.out.println("");
    }
}
