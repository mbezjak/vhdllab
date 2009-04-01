package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;






public interface IQueryResult {

	/**
	 * Vraca informaciju o tome da li je upit
	 * bio uspjesan. Neuspjesan upit nece biti
	 * cacheiran.
	 * 
	 */
	public boolean isSuccessful();
	
	/**
	 * Vrijednosti iz ove kolekcije smiju se
	 * citati, ali nikako i mijenjati.
	 * Ona sluzi kako bi upit mogao vratiti
	 * razno-razne informacije.
	 * 
	 * @param key
	 */
	public Object get(String key);
	
}








