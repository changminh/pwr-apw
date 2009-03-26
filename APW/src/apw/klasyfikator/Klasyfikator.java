package apw.klasyfikator;

import apw.silnik.Klasa;
import java.util.List;

/**
 *
 * @author Krzysztof Praszmo
 */
public interface Klasyfikator {

    /** dodaje listê obiektów */
    public void dodajDane(List<Obiekt> lista);

    /** dodaje jeden obiekt */
    public void dodajDane(Obiekt obiekt);

    /** dokonuje klasyfikacji podanego obiektu */
    public Klasa zapytajO(Obiekt obiekt);

    /** (nie wiem, czy siê przyda) weryfikacja poprawnoœci poprzedniej klasyfikacji */
    public void zweryfikuj(boolean czyPoprawna);
}
