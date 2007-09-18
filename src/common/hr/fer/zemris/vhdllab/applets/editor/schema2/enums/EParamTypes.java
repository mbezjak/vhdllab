package hr.fer.zemris.vhdllab.applets.editor.schema2.enums;




/**
 * Popis razlicitih tipova
 * vrijednosti koje parametri
 * mogu biti.
 * Parametri se koriste kao opis
 * svojstava sklopova unutar schematica,
 * te se nalaze unutar svakog sklopa
 * (ili zice).
 * 
 * Tipovi koji traze objasnjenje:
 * CASELESS - String za koji nije bitan case.
 * TEXT - radi se o vrijednosti String.
 * OBJECT - bilo koji drugi objekt koji implementira sucelje ISerializable
 * 
 * Ostali objasnjavaju sami sebe.
 * 
 * @author Axel
 *
 */
public enum EParamTypes {
	CASELESS, BOOLEAN, INTEGER, DECIMAL, TEXT, TIME, OBJECT
}



















