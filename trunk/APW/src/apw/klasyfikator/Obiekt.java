package apw.klasyfikator;

import java.util.GregorianCalendar;

/**
 * Przyklad implementacji w klasie uczen
 *
 * @author Krzystof Praszmo
 */
public interface Obiekt {

    /**
     * zwraca warto?? rzeczywist? pola o numerze indeks
     * to b?dzie podstawowa metoda i moim zdaniem wszystkie klasy implementuj?ca
     * powinny rzutowa? swoje pola do typu double, jednak powinna pozosta? mo?liwo??
     * pobierania warto?ci p?l w ich naturalnym typie np Data
     */
    public double dajWartosc(int indeks);

    /** zwraca maksymaln? warto?? pola o numerze indeks */
    public double dajMaksimum(int indeks);

    /**zwraca minimaln? warto?? pola o numerze indeks */
    public double dajMinimum(int indeks);

    /** zwraca klas? pola o numerze indeks */
    public Class dajKlase(int indeks);

    /** zwraca nazw? pola o numerze imdeks */
    public String nazwaPola(int indeks);

    /** zwraca Dat? z pola o numerze indeks (je?li jest to data, w przeciwnym wypadku leci wyj?tek) */
    public GregorianCalendar dajData(int indeks) throws ClassCastException;

    /** @see Obiekt#dajData(int) */
    public String dajTekst(int indeks) throws ClassCastException;
    
    //itd
}
