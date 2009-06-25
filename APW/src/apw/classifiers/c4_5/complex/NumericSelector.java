package apw.classifiers.c4_5.complex;

public class NumericSelector extends Selector {

	private double down = -Double.MAX_VALUE;
	private double up = Double.MAX_VALUE;
	
	@Override
	public boolean check(Object o) 
	{
//		System.out.print(down+" < " +o+" < "+up);
		if(o==null)
				return true;
		if (o instanceof Double) {
			double val = (Double) o;
			return val>=down&&val<up;			
		}
		return false;
	}
	
	public void cutUp(double d)
	{
		up = Math.min(d, up);
	}
	
	public void cutDown(double d)
	{
		down = Math.max(d, down);
	}
	
	@Override
	public String toString() {
		return "<"+down+"-"+up+")";
	}
}
