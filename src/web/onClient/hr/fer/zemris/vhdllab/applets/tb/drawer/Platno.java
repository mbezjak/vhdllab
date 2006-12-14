package hr.fer.zemris.vhdllab.applets.tb.drawer;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;


public class Platno extends JComponent {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String podatci;
	Signal[] podatci_t;
	CircuitInterface io;
	Color boja;
	Color boja_brojeva;
	Color boja_signala;
	Color boja_crtica;
	Color pomocne_crte;
	int zoom=1;
	int max_x=0;
	int max_y=0;
	private String vector_data = new String("(0,0)(0,1)(100,0)(100,1)(200,0)(200,1)(300,0)(300,1)(400,0)(400,1)(500,0)(500,1)(600,0)(600,1)(700,0)(700,1)(800,0)(800,1)");
	int polozaj=0;
	int it=0;
	boolean prvoCrtanje=true;
	
	public Platno(){
		super();
		this.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
				unesi_podatak(arg0.getPoint().x,arg0.getPoint().y);
				
			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseEntered(MouseEvent arg0) {
				nacrtaj_sve();
			}

			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		boja=Color.BLACK;
		boja_brojeva=Color.GRAY;
		boja_signala=Color.GREEN;
		boja_crtica=Color.ORANGE;
		pomocne_crte=Color.DARK_GRAY;
	
	}
	
	void setData(String data){
		this.podatci=data;
		podatci_t =transformiraj_signale();
		
	}
	
	public String getData(){
		return this.podatci;
	}
	
	/** vraca podatke kao polje tipa Signal*/
	private Signal[] transformiraj_signale(){
		
		String data=this.podatci;
		// remove tags measureUnit, duration and file
		int start, end;
		String substr;
		start = data.indexOf("<measureUnit>");
		if(start != -1) {
			end = data.indexOf("</measureUnit>");
			end += "</measureUnit>".length();
			substr = data.substring(start, end);
			data = data.replace(substr, "");
		}
		
		start = data.indexOf("<duration>");
		if(start != -1) {
			end = data.indexOf("</duration>");
			end += "</duration>".length();
			substr = data.substring(start, end);
			data = data.replace(substr, "");
		}

		start = data.indexOf("<file>");
		if(start != -1) {
			end = data.indexOf("</file>");
			end += "</file>".length();
			substr = data.substring(start, end);
			data = data.replace(substr, "");
		}

		data = data.replaceAll("\n", "");
		data = data.replaceAll("[ ]+", " ");
		
		//<signal name="a" type="scalar">(0,0)(100,1)(200,0)(250,1)(430,0)</signal>
		//<signal name="b">(0,0)(15,1)(200,0)(250,1)(430,0)</signal>
		//<signal name="c" type="vector" rangeFrom="0" rangeTo="1">(0,00)(100,01)(200,10)(250,11)</signal>
		
		data=data.replace("\"","").replace("</signal>","#").replace("<signal name=","");
		data=data.replace("type=","").replace("rangeFrom=","").replace("rangeTo=","");
		data=data.replace(">"," ");
		
		//a scalar (0,0)(100,1)(200,0)(250,1)(430,0)#
		//b (0,0)(15,1)(200,0)(250,1)(430,0)#
		//c vector 0 1 (0,00)(100,01)(100,10)(100,11)#
		String[] polje = data.split("#");
		Signal[] bla=new Signal[polje.length]; 
		String[] p;
		
		for(int i=0;i<polje.length;i++){
			if(polje[0].equals(" ") || polje[0].equals("")) return null;
			p=polje[i].split(" ");
			if(p.length==2){
				bla[i]=new Signal(p[0],"scalar",p[1]);
			}else if(p.length==3){
				bla[i]=new Signal(p[0],"scalar",p[2]);
			}else if(p.length==5){
				
				bla[i]=new Signal(p[0],"vector",p[4]);
				bla[i].setRange(Integer.parseInt(p[2]),Integer.parseInt(p[3]));
				//TODO promjeniti kada implementiram expandiranje vektora
				bla[i].exp=true;
			}
		}
		
		
		
		return bla;
		
		
	}
	
	
	
	public String[] unosVektora(){
		return null;
		
	}
	
	
	
	/** argument -ime string koji ce biti ispisan kao ime  --> ime ili ime(bit)
		 -polozaj signala: 1 je na vrhu 2 ispod itd.. 
		 -data u obliku (vrijeme, vrijednost)(vrijeme,vrijednost) npr (100,1)(150,0)*/ 

	private void crtaj_signal(String ime,int polozaj_signala,String data){
	
	
	int x1;
	int x2;
	int y=0;
	data=data.replace(")(","/");
	int p=data.length();
	data =data.substring(1,p-1);
	int offset=0;

	String[] polje_podataka0 = data.split("/"); 



	 int n =polje_podataka0.length;

	 String[] polje_podataka =new String[n+1];

	 for(int i=0;i<=n-1;i++){
	
		 polje_podataka[i]=polje_podataka0[i];

	 }

	 polje_podataka[n]=Integer.parseInt(polje_podataka[n-1].split(",")[0])+500+","+polje_podataka[n-1].split(",")[1];            



	 Graphics gp =this.getGraphics();
	 Graphics2D g= (Graphics2D) gp;


	 //pomocne crte-----------------------------
	 g.setColor(pomocne_crte);
	 g.drawLine(80,polozaj_signala*30+22,max_x,polozaj_signala*30+22);

	 for(int i=80;i<max_x;i=i+10){
		 g.drawLine(i,polozaj_signala*30+10,i,polozaj_signala*30+24);
	
	 }


	 for(int i=80;i<max_x;i=i+50){
	
		 g.setColor(boja_brojeva);
	
		 g.drawString(String.valueOf((i-80)*zoom),i,polozaj_signala*30+35);
		 g.setColor(boja_crtica);
		 g.drawLine(i,polozaj_signala*30+15,i,polozaj_signala*30+24);
	 }

	 g.setColor(Color.GRAY);
	 g.drawLine(80,max_y,80,5);

	 //---------------------------------------------------

	g.setColor(boja_signala);

	 for(int i=1; i<n+1;i++){
	
		 String tmp1 = polje_podataka[i-1];
		 String tmp2  = polje_podataka[i];
	
		 String[] tmp11 = tmp1.split(",");
		 String[] tmp22 = tmp2.split(",");
	
	
	
		 //podesava podatke o velicini podrucja na kojem se prokazuje signal
		 if(Integer.valueOf(tmp22[0]).intValue()/zoom+100>max_x) max_x=(Integer.valueOf(tmp22[0]).intValue()/zoom+400);
		 if(polozaj_signala*30+40> max_y) max_y= polozaj_signala*30+40;
	
	
	
	
	
		 //prostor za ime, sa kompenzacijom uvecanja
		 offset=80*zoom;
	
		 x1 = offset+Integer.parseInt(tmp11[0]);
		 x2 = offset+Integer.parseInt(tmp22[0]);
	
		 // proracun y mora biti uskladen sa y u metodi za dodavanje podataka
		 y =polozaj_signala*30-Integer.parseInt(tmp11[1])*10+20;
	
		 //crtanje
		 g.drawLine(x1/zoom,y,x2/zoom,y);
	 }

	
		 g.drawString(ime,10,polozaj_signala*30+20);
		 


	 // prosiruje velicinu panela da odgovara podrucju na kojem se crta 
	 this.setPreferredSize(new Dimension(max_x,max_y));
	 this.revalidate();	


	}
		

	
	
	
	/**crta sve signale pomocu metode za crtanje prvo ul pa iz*/

	public void nacrtaj_sve(){
	
	

		Signal[] ul=this.podatci_t;
		
		
		//	cisti stare crte
		Graphics g;
		g=this.getGraphics().create();
		Graphics2D g1= (Graphics2D) g;
		g.setColor(boja);
		g1.fillRect(0,0,max_x,max_y+20);
		
		
		
		
		int k=ul.length;
		
		//int polozaj=0;
		polozaj=0;
		for(it=0; it<k;it++){
			
				if(ul[it].tip.contentEquals("vector")){
					/*g.setColor(this.boja_signala);
					g.drawString("x",80*zoom-10,polozaj*30+20);
					if(this.prvoCrtanje){
						
						this.addMouseListener(new MouseListener(){
							int p=it;
							int polozaj1=polozaj;
							public void mouseClicked(MouseEvent arg0) {
								int x =arg0.getPoint().x;
								int y =arg0.getPoint().y;
							
							
								if(((80*zoom-10)<x & x<(80*zoom))&((polozaj1*30+20)>y)&((polozaj1*30+15)<y)){             
									if(podatci_t[p].exp){
										podatci_t[p].exp=false;
										System.out.println(podatci_t[p].exp);
									}else{
										podatci_t[p].exp=true;
										System.out.println(podatci_t[p].exp);
									}
								
								}
							}

							public void mousePressed(MouseEvent arg0) {
								// TODO Auto-generated method stub
							
							}

							public void mouseReleased(MouseEvent arg0) {
								// TODO Auto-generated method stub
								
							}

							public void mouseEntered(MouseEvent arg0) {
								// TODO Auto-generated method stub
							
							}

							public void mouseExited(MouseEvent arg0) {
								// TODO Auto-generated method stub
							
							}
						
					
						});
					this.prvoCrtanje=false;
					}*/
					if(ul[it].exp){
						//System.out.println(ul[i].ime);
						String[] bitovi=ul[it].razdvoji_bitove();
						//System.out.println(ul[i].data);
						
						//System.out.println("su"+ul[i].razdvoji_bitove()[0]);
						for(int n=0;n<bitovi.length;n++){
						
							if(ul[it].getRange()[0]<=ul[it].getRange()[1]){
								crtaj_signal(ul[it].ime+"("+(ul[it].getRange()[0]+n)+")",polozaj,bitovi[n]);
								polozaj++;
							}else if(ul[it].getRange()[0]>ul[it].getRange()[1]){	
								crtaj_signal(ul[it].ime+"("+(ul[it].getRange()[0]-n)+")",polozaj,bitovi[n]);	
								polozaj++;
							}
					
						}
						
						//k+=bitovi.length;
						//it+=bitovi.length;
					
				
					}else{
						crtaj_signal(ul[it].ime,polozaj,this.vector_data);
						polozaj++;
					}
				
				}else if(ul[it].tip.contentEquals("scalar")){
					crtaj_signal(ul[it].ime,polozaj,ul[it].data);
					polozaj++;
				}
		
			
		}
		
		
	}


	
	
	
	private void unesi_podatak(int x, int y) {
		
		Signal[] ul=this.podatci_t;
		
		
			
		//slaze podatke o tome gdje je koji nacrtan i u kakvom je stanju
		String[] polje=new String[100];
		int n=0;
		for(int i=0;i<ul.length;i++){
			
			if(ul[i].tip.contentEquals("scalar")){
				polje[n]=ul[i].ime+" "+String.valueOf(-1);
				n++;
				
			}else if(ul[i].tip.contentEquals("vector")){
				if(ul[i].exp){
					for(int k=0;k<ul[i].brojBitova;k++){
						polje[n]=ul[i].ime+" "+String.valueOf(k);
						n++;
					}
					
				}else{
					polje[n]=ul[i].ime+" "+String.valueOf(-2);
					n++;
					
				}
			}
		}
		
		
		
		
		
		
		int nova_vrijednost=0;
		int signal=-1;//redni broj mjesta gdje ja nacrtan signal
		
		
		//oznacava da treba promjeniti signal
		boolean f=false;
		
		
		//pronalazi na kojem mjestu se nalazi signal kojeg treba mjenjati
		for(int i=0;i<n;i++){
			if(y<(this.getY()+20+i*30+2)&y>(this.getY()+20+i*30-10-2)){
				signal=i;
				
				if(y<(this.getY()+20+i*30+2)&y>(this.getY()+20+i*30-5)){
					nova_vrijednost=0;
					f=true;
					break;
				}else{
					f=true;
					nova_vrijednost=1;
					break;
				}
			}
			
		}
		
		
		
		if(signal==-1)return;//ako je kliknuto na mjestu gdje nuje nista nacrtano
		
		String odabrani_signal=polje[signal];
		String ime=odabrani_signal.split(" ")[0];
		String bit=odabrani_signal.split(" ")[1];
		int redni_broj_u_polju=0;
		for(int i=0;i<ul.length;i++){
			if(ul[i].ime.contentEquals(ime)){
				redni_broj_u_polju=i;
				break;
			}
		}
		
		
		
		
		//--------------------------
		String signal_za_promjeniti=null;
		String signal_sa_unesenom_promjenom=new String();
		
		if(-1==Integer.parseInt(bit)){
			signal_za_promjeniti=ul[redni_broj_u_polju].data;
		}else if(Integer.parseInt(bit)>=0){
			String[] pom=ul[redni_broj_u_polju].razdvoji_bitove();
			signal_za_promjeniti=pom[Integer.parseInt(bit)];
		}else if(-2==Integer.parseInt(bit)){
			String[] signal_za_promjeniti_p=unosVektora();
			ul[redni_broj_u_polju].postavi_sve_bitove(signal_za_promjeniti_p);
			f=false;
		}
		
		
		
		
		if(f){
			
			
			
			//zaokruzivanje x polozaja
			
			
			int vrijeme=Math.round(x/5)*5;
			vrijeme=(vrijeme-80)*zoom;
			if(vrijeme<0) vrijeme=0;
			
			//ako signal sadrzi promjenu u to vrijeme
			if(signal_za_promjeniti.contains("("+vrijeme+",")){
				int i = signal_za_promjeniti.indexOf("("+vrijeme+",");
				int n1 = String.valueOf("("+Integer.toString(vrijeme)+",").length();
				String pom = signal_za_promjeniti.substring(i,i+n1+2);
				signal_sa_unesenom_promjenom=signal_za_promjeniti.replace(pom,"("+vrijeme+","+nova_vrijednost+")");
		
				
			
			//ako ne sadrzi promjenu u to vrijeme
			}else{
				
				
				 int index=0;
				 String pom2 = signal_za_promjeniti.replace("(","");
				 pom2=pom2.replace(")"," ");
				 String[] pom3=pom2.split(" ");
				 
				 //pom3.. [100,1][150,0]....
				 String[] pom5=null;
				 for(int t=0;t<(pom3.length+1);t++){
					 
					 if(t<pom3.length){
				 		 pom5=pom3[t].split(",");
				 	 }
					 if((Integer.parseInt(pom5[0])>vrijeme)&(t<pom3.length)){
					 	 index=t;
						 break;
					 }else if(t==pom3.length){
						 index=t;
						 break;
					 }
				 }
				 
				 String[] novi_signal = new String[pom3.length+1];
				 
				 boolean promjena_unesena=false;
				 boolean zamjeniti_element=false;// izbjegava se npr (10,0)(20,0)(30,1) i stedi memorija ---> (10,0)(30,1)
				 
				 
				 for(int t=0;t<pom3.length;t++){
					
					
					 if((t==index)&(!promjena_unesena)){
						 promjena_unesena=true;
						 
						 
						 String[] pom4=pom3[t].split(",");
						 if(Integer.parseInt(pom4[1])==nova_vrijednost){
							 zamjeniti_element=true;
							 novi_signal[t]=new String("("+vrijeme+","+nova_vrijednost+")");
						 }else{
							 novi_signal[t]=new String("("+vrijeme+","+nova_vrijednost+")");
							 t--;
						 }
						 
						 	
						 	
					 }else{
						 if(!promjena_unesena){
							 novi_signal[t]="("+pom3[t]+")";
						 }else{ 
							if(zamjeniti_element){
								 novi_signal[t]="("+pom3[t]+")";
							}else{
								novi_signal[t+1]="("+pom3[t]+")";
							}
							
							
						 }
					 }
					
				}
				
				 //ako treba staviti podatak na kraj---------------TODO ovo rijesiti bolje!!!!!!!
				if(promjena_unesena==false) novi_signal[pom3.length]=new String("("+vrijeme+","+nova_vrijednost+")"); 
				 
				 
				 
				for(int i=0;i<novi_signal.length;i++){
				
				signal_sa_unesenom_promjenom+=novi_signal[i];
				
				}
			
			
			
			}
			
			
			//krpanje prijasnjeg problema koji nastaje pro izbacivanju nepotrebnih podataki npr (20,0)(25,0)<--izbaceno(50,1) ali na kraju podataka se pojavljuje null(50,1)
			signal_sa_unesenom_promjenom=signal_sa_unesenom_promjenom.replace("null","");
			
			
			if(Integer.parseInt(bit)==-1){
				ul[redni_broj_u_polju].postaviPodatkeZaSkalar(signal_sa_unesenom_promjenom);
			}else{
				ul[redni_broj_u_polju].setBit(signal_sa_unesenom_promjenom,Integer.parseInt(bit));
			}
			nacrtaj_sve(); 
			
		
		}
		
		
	}

	@Override
	public void paint(Graphics arg0){
		if(this.podatci!=null)this.nacrtaj_sve();
		this.validate();
	}
	



	
	
	




}




