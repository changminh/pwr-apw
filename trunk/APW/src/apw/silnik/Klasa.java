package apw.silnik;

/**
 *
 * @author Krzysztof Praszmo
 */
public class Klasa {

	private int id;
	private String nazwa;

	public Klasa(int id,String nazwa)
	{
		this.nazwa = nazwa;
		this.id = id;
	}

	@Override
	public String toString() {
		return nazwa;
	}

}