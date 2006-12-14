package hr.fer.zemris.vhdllab.simulators.ghdl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.simulators.ISimulator;
import hr.fer.zemris.vhdllab.vhdl.SimulationMessage;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.simulations.VcdParser;

/**
 * @author marcupic
 * 
 * This is implementation of Simulator Wrapper for ghdl. When constructed, 
 * it will require two parameters in configuration file:
 * <ul>
 * <li><b>executable</b> - full path to simulator program.</li>
 * <li><b>tmpRootDir</b> - full path to temporary directory.</li>
 * </ul>
 */
public class GHDLSimulator implements ISimulator {

	private String executable;
	private String tmpRoot;
	
	public GHDLSimulator(Properties params) {
		super();
		executable = params.getProperty("executable");
		if(executable==null) {
			throw new IllegalArgumentException("Simulator executable file not specified!");
		}
		tmpRoot = params.getProperty("tmpRootDir");
		if(tmpRoot==null) {
			throw new IllegalArgumentException("No root temporary directory specified!");
		}
	}

	public SimulationResult simulate(List<File> dbFiles, List<File> otherFiles,
			File simFile, VHDLLabManager vhdlman) {
		java.io.File tmpRootFile = new java.io.File(tmpRoot);
		java.io.File tmpFile = null;
		java.io.File tmpDir = null;
		try {
			// STEP 1: create temporary directory
			// -----------------------------------------------------------
			tmpFile = java.io.File.createTempFile("ghd",null,tmpRootFile);
			tmpDir = new java.io.File(tmpRootFile,"DIR"+tmpFile.getName());
			tmpDir.mkdirs();
			// STEP 2: copy all vhdl files there
			// -----------------------------------------------------------
			//      2a: copy all database files
			for(File f : dbFiles) {
				copyFile(f, tmpDir,vhdlman);
			}
			//      2b: copy all disk files
			if(otherFiles != null) {
				for(File f : otherFiles) {
					copyFile(f, tmpDir,vhdlman);
				}
			}
			// STEP 3: prepare simulator call
			// -----------------------------------------------------------
			List<String> cmdList = new ArrayList<String>(dbFiles.size()+(otherFiles==null?0:otherFiles.size())+2);
			cmdList.add(executable);
			cmdList.add("-a");
			if(otherFiles!=null) {
				for(int i = otherFiles.size()-1; i >= 0; i--) {
					cmdList.add(otherFiles.get(i).getFileName()+".vhdl");
				}
			}
			for(int i = dbFiles.size()-1; i >= 0; i--) {
				cmdList.add(dbFiles.get(i).getFileName()+".vhdl");
			}
			String[] cmd = new String[cmdList.size()];
			cmdList.toArray(cmd);
			// STEP 4: execute the call
			// -----------------------------------------------------------
			final List<String> errors = new LinkedList<String>();
			Process proc = Runtime.getRuntime().exec(cmd,null,tmpDir);
			InputConsumer consumer = new InputConsumer(proc.getInputStream(),proc.getErrorStream(),errors,errors);
			consumer.waitForThreads();
			int retVal = proc.waitFor();
			if(retVal != 0) {
				return new SimulationResult(Integer.valueOf(retVal),false,listToSimMessages(errors),null);
			}
			// OK, here we have added files into project. Now we have to simulate it.
			// STEP 5: prepare simulator call
			// -----------------------------------------------------------
			cmdList.clear();
			cmdList.add(executable);
			cmdList.add("--elab-run");
			cmdList.add(simFile.getFileName());
			cmdList.add("--vcd=simout.vcd");
			cmd = new String[cmdList.size()];
			cmdList.toArray(cmd);
			// STEP 6: execute the call
			// -----------------------------------------------------------
			errors.clear();
			proc = Runtime.getRuntime().exec(cmd,null,tmpDir);
			consumer = new InputConsumer(proc.getInputStream(),proc.getErrorStream(),errors,errors);
			consumer.waitForThreads();
			retVal = proc.waitFor();
			String waveform = null;
			if(retVal==0) {
				VcdParser vcd = new VcdParser(new java.io.File(tmpDir,"simout.vcd").getAbsolutePath());
				vcd.parse();
				waveform = vcd.getResultInString();
			}
			return new SimulationResult(Integer.valueOf(retVal),retVal==0,listToSimMessages(errors),waveform);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(tmpDir != null) {
				try {
					recursiveDelete(tmpDir);
				} catch(Exception ignorable) {
				}
			}
			if(tmpFile!=null) {
				tmpFile.delete();
			}
		}
		return null;
	}

	private List<? extends SimulationMessage> listToSimMessages(List<String> errors) {
		List<SimulationMessage> list = new ArrayList<SimulationMessage>(errors.size());
		for(String e : errors) {
			list.add(new SimulationMessage(e));
		}
		return list;
	}

	private void recursiveDelete(java.io.File tmpDir) {
		java.io.File[] children = tmpDir.listFiles();
		if(children!=null) {
			for(java.io.File f : children) {
				if(f.isDirectory()) {
					recursiveDelete(f);
				} else {
					f.delete();
				}
			}
		}
		tmpDir.delete();
	}

	private void copyFile(File src, java.io.File destDir, VHDLLabManager vhdlman) throws IOException {
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new java.io.File(destDir,src.getFileName()+".vhdl")),"UTF-8"));
			bw.write(vhdlman.generateVHDL(src));
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IOException("Could not copy file.");
		} finally {
			if(bw != null) try { bw.close(); } catch(Exception ignorable) {}
		}
	}
	
	private static class InputConsumer {
		private int runningThreads = 0;
		
		public InputConsumer(InputStream procOutput, InputStream procError, List<String> outputs, List<String> errors) {
			try {
				Thread t1 = consume(procOutput, outputs); 
				t1.start();
				Thread t2 = consume(procError, errors);
				t2.start();
			} catch(Exception ignorable) {
			}
		}

		private Thread consume(final InputStream procStream, final List<String> list) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(procStream));
						while(true) {
							String line = br.readLine();
							if(line == null) break;
							if(line.equals("")) continue;
							synchronized(list) {
								list.add(line);
							}
						}
					} catch(Exception ignorable) {
					}
					synchronized (this) {
						runningThreads--;
						this.notifyAll();
					}
				}
			});
			runningThreads++;
			return t;
		}
		
		public void waitForThreads() {
			synchronized(this) {
				while(runningThreads>0) {
					try { 
						this.wait(); 
					} catch(Exception ignorable) {
					}
				}
			}
		}
	}
	
}
