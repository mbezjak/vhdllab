package hr.fer.zemris.vhdllab.applets.main.component.about;

public enum Description {
	CUPIC("Sef projekta, menadzer i programerski guru.  Najcesce koristena recenica: " +
			"\"Problem s vhdllabom' (tak se zvala radna verzija projekta)\" "),
	ALIC("Student 3. godine, smjer industrijska elektronika.  MIA (missing in action)"),
	BEZJAK("Student 3. godine racunarstva.  Ceka se lista poslova koje je radio" +
			" na projektu.  Kolosalnost te liste mogla bi srusiti krhke ZEMRIS-ove servere"),
	CAKMAK("Student 2. godine racunarstva.  Jedini bolonjez u timu."),
	DELAC("Student 3. godine racunarstva.  Automat je njegovih ruku djelo.  Ovih se dana" +
			" baca u kostac s izradom testbench appleta.  Ima 5.0.  Kupuje novi komp."),
	GJURKOVIC("Student 3. telekomunikacija i informatike.  MIA :)"),
	OZEGOVIC("Student 4. godine telekomunikacija i informatike.  Na projektu radio simulaciju rezultata" +
			" ,project explorer i compile errorse"),
	PROKOPEC("Student 3. godine racunarstva.  Schematic je njegovih ruku djelo.  Jos jedan" +
			" 5 nulas.  A mi ne volimo 5 nulase.  :)"),
	RAJAKOVIC("Student 3. godine racunarstva.  Bio moralna podrska, ali ocekuje se njegov" +
			" brutalno mocni editor."),
	VIGNJEVIC("Studentica 3. godine racunarstva.  Ceka se njen sinopsis razgovora s Majom Botincan" +
			" poradi OOP-a.  Na projektu bila zlocesta i podbadala ekipu.  :P");
	
	private String string;
	
	Description(String string) {
		this.string = string;
	}
	
	public String getString() {
		return string;
	}
}
