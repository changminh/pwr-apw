package apw.klasyfikator;
import java.util.GregorianCalendar;



/**
 * Przyk?adowa implementacja interfejsu obiekt
 * @author Krzysztof Praszmo
 */
public class Osoba implements Obiekt{
	class Miasto
	{
		public int liczba_mieszk;
	}

	GregorianCalendar data_ur;
	int wzrost;
	double waga;
	Miasto miasto;


	@Override
	public GregorianCalendar dajData(int indeks) throws ClassCastException {
		if(indeks==1)
			return data_ur;
		throw new ClassCastException();
	}

	@Override
	public Class dajKlase(int indeks)
	{
		switch (indeks) {
		case 1:
			return GregorianCalendar.class;
		case 2:
			return int.class;
		case 3:
			return double.class;
		case 4:
			return Miasto.class;
		}
		return null;
	}

	@Override
	public double dajMaksimum(int indeks)
	{
		switch (indeks) {
		case 1:
			return 150.0;
		case 2:
			return 300.0;
		case 3:
			return 500.0;
		case 4:
			return 30000000.0;
		}
		return 0.0;
	}

	@Override
	public double dajMinimum(int indeks) {
		switch (indeks) {
		case 1:
			return 0.0;
		case 2:
			return 0.0;
		case 3:
			return 0.0;
		case 4:
			return 0.0;
		}
		return 0.0;
	}

	@Override
	public String dajTekst(int indeks) throws ClassCastException
	{
		throw new ClassCastException();
	}

	@Override
	public double dajWartosc(int indeks)
	{
		switch (indeks) {
		case 1:
			return data_ur.compareTo(new GregorianCalendar());// wiek w latach
		case 2:
			return (double) wzrost;
		case 3:
			return waga;
		case 4:
			return (double)miasto.liczba_mieszk;
		}
		return 0.0;
	}

	@Override
	public String nazwaPola(int indeks)
	{
		switch (indeks) {
		case 1:
			return "Data urodzenia";
		case 2:
			return "Wzrost";
		case 3:
			return "Waga";
		case 4:
			return "Miasto zamieszkania";
		}
		return null;
	}

}
