import java.io.Serializable;
import java.util.List;


public interface Klasyfikator
{
	
	public void dodajDane(List<Obiekt> lista);
	// dodaje listê obiektów
	public void dodajDane(Obiekt obiekt);
	// dodaje jeden obiekt
	public Klasa zapytajO(Obiekt obiekt);
	// dokonuje klasyfikacji podanego obiektu
	public void zweryfikuj(boolean czyPoprawna);
	// (nie wiem, czy siê przyda) weryfikacja poprawnoœci poprzedniej klasyfikacji
	
	
	
}
