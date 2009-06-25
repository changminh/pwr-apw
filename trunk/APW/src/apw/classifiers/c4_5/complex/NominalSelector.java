package apw.classifiers.c4_5.complex;

public class NominalSelector extends Selector {

	private Object classObject;

	public NominalSelector(Object classObject)
	{
		this.classObject = classObject;
	}
	
	@Override
	public boolean check(Object o) 
	{
//		System.out.println(o+" ??? "+classObject);
		if(o==null)return true;
		return classObject.equals(o);
	}

	@Override
	public String toString() {
		return "'"+classObject.toString()+"'";
	}
}
