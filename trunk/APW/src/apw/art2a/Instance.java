package apw.art2a;

/**
 *
 * @author nitric
 */
public class Instance {
    private final double[] processingVector;
    private double[] inputVector;
    private int id;
    private double norm = 0.d;

    public Instance(double[] v, int id, double theta) {
        this.inputVector = v;
        this.processingVector = new double[inputVector.length];
        this.id = id;
        // Preparing vector for processing:
        normalize(true);
        denoise(theta);
        normalize(false);
    }

    public Instance(double[] weights, double norm) {
        this.processingVector = weights;
        this.inputVector = new double[weights.length];
        this.id = -1;
        this.norm = norm;
        for (int i = 0; i < inputVector.length; i++)
            inputVector[i] = processingVector[i] * norm;
    }

    public int getLength() {
        return processingVector.length;
    }

    public int getId() {
        return id;
    }

    public double[] getProcessingVector() {
        // TODO: Co jest grane?!
        double[] result = new double[processingVector.length];
        for (int i = 0; i < processingVector.length; i++)
            result[i] = processingVector[i];
        return result;
    }

    public double[] getInputVector() {
        return inputVector;
    }

    public double at(int index) {
        return processingVector[index];
    }

    public double inputAt(int index) {
        return inputVector[index];
    }

    public void print() {
        System.out.print("Instance " + id + ": ");
        for (int i = 0; i < getLength(); i++)
            System.out.print(inputVector[i] + "; ");
        System.out.println("");
    }

    private void normalize(boolean firstProcessing) {
        double sum = 0;
        double[] v = firstProcessing ? inputVector : processingVector;
        for (double d : v)
            sum += d * d;
        double temp = Math.sqrt(sum);
        if (firstProcessing)
            norm = temp;
        else
            norm = norm * temp;
        for (int i = 0; i < processingVector.length; i++)
            processingVector[i] = v[i] / temp;
    }

    private void denoise(double theta) {
        boolean allZero = true;
        for (int i = 0; i < processingVector.length; i++) {
            if (processingVector[i] < theta)
                processingVector[i] = 0.d;
            else
                allZero = false;
        }
        if (allZero)
            throw new InstantiationError(
                    "Normalized and denoised vector doesn't containt non-zero value."
                    );
    }

    public static void printTable(double [] t) {
        for (double d : t)
            System.out.print(d + " ");
    }

    public double getNorm() {
        return norm;
    }
}
