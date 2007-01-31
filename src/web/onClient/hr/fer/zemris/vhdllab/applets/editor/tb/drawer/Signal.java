package hr.fer.zemris.vhdllab.applets.editor.tb.drawer;

public class Signal {

	
	
	
	public String[] razdvojeni_bitovi;
	public String ime;
	public String tip;
	public String data;
	private int rangeFrom;
	private int rangeTo;
	public boolean exp; //ako je vektor prosiren da se vide bitovi ovo je true
	public int brojBitova;

	
	
	
	
	public Signal(String ime,String tip,String data){
		this.ime=ime;
		this.tip=tip;
		this.data=data;
		if(tip.contentEquals("scalar")){
			String[] data1 =new String[1];
			data1[0]=data;
			data=this.ocisti_signal(data1)[0];
		}
		
	}
	
	
	public boolean isEmpty(){
		return data==null;
	}
	
	
	
	
	/**setRange(int from, int to)*/
	public void setRange(int from, int to){
		this.rangeFrom=from;
		this.rangeTo=to;
		this.brojBitova=Math.abs(this.rangeFrom-this.rangeTo)+1;
	}
	
	
	/**vraca p[0]=this.rangeFrom;
		 p[1]=this.rangeTo;*/
	public int[] getRange(){
		int[] p;
		p=new int[2];
		if(this.tip.contentEquals("vector")){
		 p[0]=this.rangeFrom;
		 p[1]=this.rangeTo;
		 return p;
		 }else return null;
	}
	
	
	
	
	
	/**iz grozno izmjesanih podataka za vektor vadi bitove po redu*/ 
	public String[] razdvoji_bitove(){
		String data=this.data;
		String[] bitovi=new String[this.brojBitova];
		//System.out.println("bb "+brojBitova+data);
		data=data.replace(")","");
		data=data.replace("(","#");
		data=data.substring(1,data.length());
		
		String[] pom= data.split("#");
		
		for(int i=0;i<bitovi.length;i++){
			bitovi[i]="";
		}
		
		
		
		
		for(int i=0;i<pom.length;i++){
			String[] p;
			p=pom[i].split(",");
			//System.out.println("p "+p[0]+" " +p[1]);
			for(int k=0;k<this.brojBitova;k++){
				bitovi[k]+="("+p[0]+","+p[1].charAt(k)+")";
			}
		
		}
		
		bitovi = this.ocisti_signal(bitovi);
		//System.out.println("bit0"+bitovi[1]);
		return bitovi;
	}
	
	
	
	
	
	
	
	public String[] ocisti_signal(String[] polje_podataka2) {
		
		
		
	
		for(int j=0;j<polje_podataka2.length;j++){	
		
			polje_podataka2[j]=polje_podataka2[j].replace("(","");
			polje_podataka2[j]=polje_podataka2[j].substring(0,polje_podataka2[j].length()-1);
			polje_podataka2[j]=polje_podataka2[j].replace(")","/");
		
			String[] polje_podataka3;
			polje_podataka3=polje_podataka2[j].split("/");
		
			String p="("+polje_podataka3[0]+")";
			for(int i=1;i<polje_podataka3.length;i++){
				String p1=polje_podataka3[i-1].split(",")[1];
				String p2=polje_podataka3[i].split(",")[1];
		
			
				if(Integer.parseInt(p1)!=Integer.parseInt(p2)){
					p+="("+polje_podataka3[i]+")";
				}
			
	
			}
		
			polje_podataka2[j]=p;
	
		}
		return polje_podataka2;
	}


	/**privma podatke za odredeni bit u signalu , drugi argument je REDNI BROJ BITA 0,1.... ne 
	 * broj pod kojim je taj bit inace poznat*/
	public void setBit(String bitData, int bit){
		
		this.razdvojeni_bitovi=this.razdvoji_bitove();
		this.razdvojeni_bitovi[bit]=bitData;
		
		this.razdvojeni_bitovi=this.ocisti_signal(this.razdvojeni_bitovi);
		System.out.println("bit "+razdvojeni_bitovi[bit]);
		
		String[] pom = this.razdvojeni_bitovi;
		int l=0;
		//int najdulji=0;
		
		for(int i =0; i<this.razdvojeni_bitovi.length;i++){
			pom[i]=pom[i].replace(")(","#").replace("(","").replace(")","");
			if(pom[i].split("#").length>l){
				l=pom[i].split("#").length;
				//najdulji=i;
			}
		}
		//[0,0#100,1#120,1#140,1#150,0#200,0#250,1#260,0][.....]
	
		String[][] pod=new String[this.razdvojeni_bitovi.length][];//opasno ogranicenje   najdulji+100
		
		for(int i=0;i<this.razdvojeni_bitovi.length;i++){
			pod[i]=pom[i].split("#");
		}
		/* sada imam
		 *        i=0   i=1   ...
		 *  j=0 [[***][****][****][***]...]
		 *  j=1 [[***][**][****][***]...]
		 *  j.. ..
		 * ..
		 */
		
		//ova nastrana grozota bi trebala spjiti bitove udogovoreni format koji je ocajno nepraktican samo ja to nisam znao do sada
		String[] pod0=pod[0];
		for(int i=1;i<this.razdvojeni_bitovi.length;i++){
			String[] pod1=pod[i];
			int l1=pod0.length;
			int l2=pod1.length;
			for(int j=0;j<Math.max(l1,l2);j++){
				if(j<Math.min(l1,l2)){
					String[] pom1 = pod0[j].split(",");
					String[] pom2 = pod1[j].split(",");
					System.out.println("nulti bit prije "+pod0[j]+" j "+j);
					if(Integer.parseInt(pom1[0])==Integer.parseInt(pom2[0])){
						pom1[1]+=pom2[1];
						pod0[j]=pom1[0]+","+pom1[1];
						System.out.println("1) nulti bit poslje "+pod0[j]);
					
					}else if(Integer.parseInt(pom1[0])>Integer.parseInt(pom2[0])){
						String[] temp=new String[pod0.length+1];
						for(int k=0;k<temp.length;k++){
							if(k<j)temp[k]=pod0[k];
							if(k==j){
								int t=pod0[j-1].split(",")[1].length();
								if(t==2)temp[k]=pom2[0]+","+pod0[j-1].split(",")[1].charAt(0)+pom2[1];
								if(t>2)temp[k]=pom2[0]+","+pod0[j-1].split(",")[1].substring(0,t-1)+pom2[1];
							}
							if(k>j) temp[k]=pod0[k-1];
						}
						pod0=temp;
						System.out.println("2) nulti bit poslje "+pod0[j]);
						l1=pod0.length;
						l2=pod1.length;
						continue;
					}else if(Integer.parseInt(pom1[0])<Integer.parseInt(pom2[0])){
						pod0[j]=pom1[0]+","+pom1[1]+pod1[j-1].split(",")[1];
						String[] temp=new String[pod1.length+1];
						temp[0]="";
						for(int k=1;k<temp.length;k++){
							temp[k]=pod1[k-1];
						}
						pod1=temp;
						l1=pod0.length;
						l2=pod1.length;
						System.out.println("3) nulti bit poslje l1 l2 "+pod0[j]+" "+l1+" "+l2);
						continue;
					}
				}else if(j>=Math.min(l1,l2)){
					
					if(l1<l2){
						String[] temp= new String[l2];
						for(int t=0;t<temp.length;t++){
							if(t<j)temp[t]=pod0[t];
							if(t>=j){
								int t1=pod0[j-1].split(",")[1].length();
								if(t1==2)temp[t]=pod1[t].split(",")[0]+","+pod0[j-1].split(",")[1].charAt(0)+pod1[t].split(",")[1];
								if(t1>2)temp[t]=pod1[t].split(",")[0]+","+pod0[j-1].split(",")[1].substring(0,t1-1)+pod1[t].split(",")[1];
							}
							System.out.println("4) temp "+temp[t]);
						}
						pod0=temp;
						break;
						
					}else if(l1>l2){
						String[] temp= new String[l1];
						for(int t=0;t<temp.length;t++){
							if(t<j)temp[t]=pod0[t];
							if(t>=j){
								temp[t]=pod0[t].split(",")[0]+","+pod0[t].split(",")[1]+pod1[l2-1].split(",")[1];
							
							}
							System.out.println("5) temp "+temp[t]);
						}
						pod0=temp;
						
						break;
					}
						
				}
				
				
			}
		}
		this.data="";			
		for(int i=0;i<pod0.length;i++){
			data+="("+pod0[i]+")";
			System.out.println("gotovi signal "+data);
		}
				
			
			
	}
	
	
	
	
	
	
	/**vraca podatke u dogovorenom obliku:    
	 	<signal name="a" type="scalar">(0,0)(100,1)(200,0)(250,1)(430,0)</signal>
		<signal name="b">(0,0)(15,1)(200,0)(250,1)(430,0)</signal>
		<signal name="c" type="vector" rangeFrom="0" rangeTo="1">(0,00)(100,01)(100,10)(100,11)</signal>*/
	
	@Override
	public String toString(){
		
		String temp=null;
		if(this.tip.contentEquals("scalar")){
			temp="<signal name=\""+this.ime.trim()+"\" type=\""+this.tip+"\">"+this.data+"</signal>";
		}else if(this.tip.contentEquals("vector")){
			temp="<signal name=\""+this.ime.trim()+"\" type=\""+this.tip+"\" rangeFrom=\""+this.rangeFrom+"\" rangeTo=\""+this.rangeTo+"\">"+this.data+"</signal>";                           
		}
		
		return temp;
	}


	public void postavi_sve_bitove(String[] signal_za_promjeniti_p) {
				
		
		signal_za_promjeniti_p=this.ocisti_signal(signal_za_promjeniti_p);
		this.razdvojeni_bitovi=signal_za_promjeniti_p;
				
				String[] pom = this.razdvojeni_bitovi;
				int l=0;
				int najdulji=0;
				
				for(int i =0; i<this.razdvojeni_bitovi.length;i++){
					pom[i]=pom[i].replace(")(","#").replace("(","").replace("(","");
					if(pom[i].split("#").length>l){
						l=pom[i].split("#").length;
						najdulji=i;
					}
				}
				//[0,0#100,1#120,1#140,1#150,0#200,0#250,1#260,0][.....]
			
				String[][] pod=new String[this.razdvojeni_bitovi.length][];//opasno ogranicenje   najdulji+100
				
				for(int i=0;i<this.razdvojeni_bitovi.length;i++){
					pod[i]=pom[i].split("#");
				}
				/* sada imam
				 *        i=0   i=1   ...
				 *  j=0 [[***][****][****][***]...]
				 *  j=1 [[***][**][****][***]...]
				 *  j.. ..
				 * ..
				 */
				
				//ova nastrana grozota bi trebala spjiti bitove udogovoreni format koji je ocajno nepraktican samo ja to nisam znao do sada
				for(int i=0;i<this.razdvojeni_bitovi.length;i++){
					for(int j=1;j<najdulji+100;j++){
						String[] pom1 = pod[0][i].split(",");
						String[] pom2 = pod[j][i].split(",");
						
						if(Integer.parseInt(pom1[0])==Integer.parseInt(pom2[0])){
							pom1[1]+=pom2[1];
							pod[0][i]=pom1[0]+","+pom1[1];
						}else if(Integer.parseInt(pom1[0])>Integer.parseInt(pom2[0])){
							String[] temp=pod[0].clone();
							pod[0][i]=pom2[0]+","+pod[0][i-1].split(",")[1].substring(0,j-1)+pom2[1];
							String[] jos_jedan_string=new String[temp.length+1];
							for(int no=0;no<temp.length+1;no++){
								if(no<i){
									jos_jedan_string[no]=pod[0][no];
								}else{
									jos_jedan_string[no]=temp[no];
								}
							}
							pod[0]=jos_jedan_string;
						}else if(Integer.parseInt(pom1[0])<Integer.parseInt(pom2[0])){
							if(pom1[1].length()<j+1){
								pom[1]+= pod[j][i-1].split(",")[1];
							}
						}
						
					}
				}
								
				this.data=pom[0];	
	}


	public void postaviPodatkeZaSkalar(String signal_sa_unesenom_promjenom) {
		String[] t= new String[1];
		t[0]=signal_sa_unesenom_promjenom;
		t=this.ocisti_signal(t);
		this.data=t[0];
		
	}


	


	
	
	
}
