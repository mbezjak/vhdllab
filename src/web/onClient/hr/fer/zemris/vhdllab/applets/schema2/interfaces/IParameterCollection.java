package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import java.util.Map.Entry;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateParameterException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.ParameterNotFoundException;





/**
 * Definira sucelje klasa koje
 * sadrze razne parametre vezane
 * uz zicu (signal).
 * U principu bi ovo mogla biti jedna name-value
 * kolekcija, gdje je name kljuc za dohvat nekog
 * parametra, a value je njegova vrijednost.
 * 
 * Detaljnije u opisu svake funkcije zasebno.
 * 
 * @author Axel
 *
 */
public interface IParameterCollection extends ISerializable, Iterable<Entry<String, IParameter>> {
	// TODO izbaciti dohvat parametra i dodati funkcije za dohvat i postavljanje vrijednosti
	// TODO dodati event listener support
	
	/**
	 * Vraca IParameter objekt za
	 * navedeni kljuc.
	 * Preko IParameter objekta je
	 * moguce dohvatiti i vrijednost
	 * i tip parametra.
	 * 
	 * @throws
	 * Navedenu iznimku ako parametar ne postoji.
	 * @param key
	 * Kljuc za dohvat.
	 * @return
	 * IParametar ako isti postoji.
	 */
	IParameter getParameter(String key) throws ParameterNotFoundException;
	
	
	/**
	 * Dodaje parametar u kolekciju.
	 * 
	 * @param key
	 * Kljuc pod kojim ce parametar biti dodan (njegovo ime).
	 * @param parameter
	 * Sam parametar.
	 * @throws DuplicateParameterException
	 * Ako parametar vec postoji pod tim kljucem.
	 */
	void addParameter(String key, IParameter parameter) throws DuplicateParameterException;
	
	
	/**
	 * Mice parametar iz kolekcije parametara.
	 * 
	 * @param key
	 * Kljuc pod kojim je spremljen parametar.
	 * @throws ParameterNotFoundException
	 * Ako parametar ne postoji, baca se ova iznimka.
	 * 
	 */
	void removeParameter(String key) throws ParameterNotFoundException;
	
	
	/**
	 * Vraca vrijednost parametra za navedeni kljuc.
	 * Pritom nije moguce odrediti tip parametra,
	 * osim ako je on unaprijed poznat.
	 * Ova se funkcija moze pozvati izravno nakon
	 * getParameter() od dobivenog parametra, pa
	 * je ona u biti pokrata.
	 * 
	 * @throws
	 * Navedenu iznimku ako parametar ne postoji.
	 * @param key
	 * Kljuc za dohvat.
	 * @return
	 * IParametar ako isti postoji.
	 * 
	 */
	Object getValue(String key) throws ParameterNotFoundException;
	
	/**
	 * Sluzi za postavljanje neke vrijednosti nekog
	 * parametra odredenog sa key.
	 * Ako parametar ne postoji, bit ce stvoren
	 * novi parametar.
	 * Ova se funkcija moze pozvati izravno nakon
	 * getParameter() od dobivenog parametra, pa
	 * je ona u biti pokrata.
	 * 
	 * @param key
	 * Kljuc.
	 * @param value
	 * Vrijednost.
	 * @throws ParameterNotFoundException
	 * Ukoliko parametar ne postoji.
	 * @throws InvalidParameterValueException
	 * Iznimka je propagirana od parametra kojem je postavljena
	 * vrijednost, a baca se u slucaju da vrijednost
	 * koju zelimo postaviti nije primjenjiva.
	 * 
	 */
	void setValue(String key, Object value) throws ParameterNotFoundException, InvalidParameterValueException;
	
	/**
	 * Dohvaca broj parametara u kolekciji
	 * parametara.
	 * Pritom je povratna vrijednost nenegativna,
	 * no vraca se int radi jednostavnosti pisanja
	 * for petlji :)
	 *
	 */
	int count();
}







