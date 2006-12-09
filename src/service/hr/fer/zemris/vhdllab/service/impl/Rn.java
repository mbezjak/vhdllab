package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Rn{

	
	//ovo se mjenja po potrebi, na toj lokaciji trebaju biti sve potreble datateke sa kolima radi ghdl
	private static java.io.File dd;
	
	private static ArrayList izv(String naredba) throws IOException, InterruptedException{
		
		
		Runtime r = Runtime.getRuntime();
		Process pr = r.exec(naredba,null,dd);
	
		ArrayList messageList =new ArrayList<CompilationMessage>();
		
		InputStream in = pr.getInputStream();
		InputStreamReader re = new InputStreamReader(in);
		BufferedReader b= new BufferedReader(re);
	
		InputStream err = pr.getErrorStream();
		InputStreamReader re_err = new InputStreamReader(err);
		BufferedReader b_err= new BufferedReader(re_err);
		
		String d,e;
		boolean dd = true;
		boolean de=true;
		int i=0;
		
		while(dd||de){
			
			dd=((d=b.readLine())!=null);
			de=((e=b_err.readLine())!=null);
			
			
			
			if (dd){
				i++;
				System.out.println("out "+d);
				messageList.add(new CompilationMessage(d,i,0));
			}
			if(de){
				i++;
				System.out.println("err "+e);
				messageList.add(new CompilationMessage(e,i,0));
				
			}
		}
		pr.waitFor();
		return messageList;
	}
	
	
	public synchronized static ArrayList cmp(String dat, String entity, String dataDir, String simulator) throws IOException, InterruptedException{
		
		//mjesto gdje trebaju biti svi file-ovi
		dd =new java.io.File(dataDir);
		
		/*
			String dat="adder.vhdl";//"hello.vhdl";
			String arh="adder";//"hello_world";
			String tb_dat="adder_tb.vhdl";
			String tb_arh="adder_tb";
		*/	
		ArrayList messageList1;
		ArrayList messageList2;
		ArrayList messageList =new ArrayList<CompilationMessage>();
		
		//postavlja se po potrebi--- rijesiti bolje 
		String an=simulator+" -a ";
		String elab=simulator+" -e ";
			
		   
		   
		messageList1=izv(an+dat);
		messageList2=izv(elab+entity);
		
		//TODO provjeriti dali ovo radi ispravno
		messageList.addAll(messageList1);
		messageList.addAll(messageList2);
		
		return messageList;
		
	}
	
	public synchronized static ArrayList sim(String entity,String dataDir, String simulator) throws IOException, InterruptedException{
		//postavlja se po potrebi--- rijesiti bolje 
		String sm=simulator+" -r ";
		//mjesto gdje trebaju biti svi file-ovi
		dd =new java.io.File(dataDir);
		
		ArrayList messageList;
		messageList=izv(sm+entity+" --vcd="+entity+".vcd");
		
		return messageList; 
	}
	
	
	
	
}