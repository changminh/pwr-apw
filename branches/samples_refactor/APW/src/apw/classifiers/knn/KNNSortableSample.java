package apw.classifiers.knn;

import apw.core.Sample;

class KNNSortableSample implements Comparable<KNNSortableSample>{
	private final double range;
	private final Sample sample;

	KNNSortableSample(double range, Sample sample) {
		this.sample = sample;
		this.range = range;
	}

	@Override
	public int compareTo(KNNSortableSample o) 
	{
		double dif = o.range-range;
		dif*=10000000.0;
		return (int)dif;
	}

	public Sample getSample() {
		return sample;
	}
	
	public double getRange() {
		return range;
	}
	
}
