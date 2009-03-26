import java.util.GregorianCalendar;


public interface Obiekt 
{
	
	
	
	// Przyklad implementacji w klasie uczen
	
	
	
	public double dajWartosc(int indeks);
	// zwraca wartoœæ rzeczywist¹ pola o numerze indeks
	
	/* to bêdzie podstawowa metoda i moim zdaniem wszystkie klasy implementuj¹ca
	 powinny rzutowaæ swoje pola do typu double, jednak powinna pozostaæ mo¿liwoœæ 
	 pobierania wartoœci pól w ich naturalnym typie np Data 
	*/
	
	public double dajMaksimum(int indeks);
	// zwraca maksymaln¹ wartoœæ pola o numerze indeks

	public double dajMinimum(int indeks);
	// zwraca minimaln¹ wartoœæ pola o numerze indeks

	
	public Class dajKlase(int indeks);
	// zwraca klasê pola o numerze indeks
	
	public String nazwaPola(int indeks);
	// zwraca nazwê pola o numerze imdeks
	
	public GregorianCalendar dajData(int indeks) throws ClassCastException;
	// zwraca Datê z pola o numerze indeks (jeœli jest to data, w przeciwnym wypadku leci wyj¹tek)
	
	public String dajTekst(int indeks) throws ClassCastException;
	// jw.
	
	//itd
	

}
