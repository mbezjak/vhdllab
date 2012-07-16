/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.automat;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Testpan extends JFrame {

	private static final long serialVersionUID = 3329611576585955404L;
	
	Automat aut=null;

	public Testpan() throws FileNotFoundException{
		super();
		this.getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		BufferedReader reader=new BufferedReader(new FileReader("./src/main/resources/hr/fer/zemris/vhdllab/applets/editor/automat/automat1.xml"));
		//BufferedReader reader=new BufferedReader(new FileReader("c:\\dd.txt"));
		String xmlAut="";
		String pom;
		try {
			while((pom=reader.readLine())!=null) xmlAut=new StringBuffer(xmlAut).append(pom).append("\n").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(xmlAut);
		JButton b1=new JButton("Ispisi interni kod");
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println(aut.getData()+"\n"+aut.isModified());
				//Automat aut2=new Automat();
				//aut2.setFileContent(new FileContent("df","safas",data));
				//getContentPane().add(aut2,BorderLayout.EAST);*/
//				VHDLGenerator parser = null;
//				try {
//					parser = new VHDLGenerator(aut.getData());
//				} catch (ServiceException e1) {
//					e1.printStackTrace();
//				}
//				System.out.println(parser.getParsedVHDL());
			}
		});
		this.add(b1,BorderLayout.NORTH);
		
		aut = new Automat();
		//FileContent fc=new FileContent("ljd","skadh",xmlAut);
		//aut.setFileContent(fc);
		aut.setContainer(null);
//		FileContent fc=aut.getInitialFileContent(b1, new Caseless("default project"));
//		if (fc!=null){
//			aut.init();
//			aut.setFile(new FileInfo(FileType.AUTOMATON, fc.getFileName(), fc.getContent(), 1));
//			this.getContentPane().add(aut,BorderLayout.CENTER);
//		}
	
		this.setSize(aut.getControl().getPreferredSize());
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		Testpan tp=new Testpan();
		tp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tp.pack();
		tp.setVisible(true);	
	}
}
