package apw.kohonenSom;

public class KohonenParameters {
	//methods for distance calculation between neuron weights and data vector
	public static final int DISTANCE_EUCLIDEAN = 0;
	public static final int DISTANCE_SCALAR = 1;
	public static final int DISTANCE_MANHATTAN = 2;
	public static final int DISTANCE_L = 3;
	
	//training methods
	public static final int ALGORITHM_WTA = 0;
	public static final int ALGORITHM_WTM = 1;
	
	//neighborhood types for wtm method
	public static final int NEIGHBORHOOD_RECTANGULAR = 0;
	public static final int NEIGHBORHOOD_GAUSSIAN = 1;	
	
	//time factor functions 
	public static final int TIME_LINEAR = 0;
	public static final int TIME_HYPERBOLIC = 1; 
	
	//winning neuron selection methods
	public static final int WINNER_SIMPLE = 0;
	public static final int WINNER_CONSCIENCE = 1;
	public static final int WINNER_COUNT = 2;
	
	//weights initialization methods
	public static final int WEIGHTS_RANDOM = 0;
	
}
