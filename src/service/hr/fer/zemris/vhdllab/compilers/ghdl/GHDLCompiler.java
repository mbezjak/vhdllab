package hr.fer.zemris.vhdllab.compilers.ghdl;

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

import hr.fer.zemris.vhdllab.compilers.ICompiler;
import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;
import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;

/**
 * @author marcupic
 * 
 * This is implementation of Compiler Wrapper for ghdl. When constructed, 
 * it will require two parameters in configuration file:
 * <ul>
 * <li><b>executable</b> - full path to compiler program.</li>
 * <li><b>tmpRootDir</b> - full path to temporary directory.</li>
 * </ul>
 */
public class GHDLCompiler implements ICompiler {
	private String executable;
	private String tmpRoot;

	public GHDLCompiler(Properties params) {
		super();
		executable = params.getProperty("executable");
		if(executable==null) {
			throw new IllegalArgumentException("Compiler executable file not specified!");
		}
		tmpRoot = params.getProperty("tmpRootDir");
		if(tmpRoot==null) {
			throw new IllegalArgumentException("No root temporary directory specified!");
		}
	}

	public CompilationResult compile(List<File> dbFiles, List<File> otherFiles,
			File compileFile, VHDLLabManager vhdlman) {
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
			// STEP 3: prepare compiler call
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
				return new CompilationResult(Integer.valueOf(retVal),false,listToCompilationMessages(errors));
			}
			return new CompilationResult(Integer.valueOf(retVal),true,listToCompilationMessages(errors));
		} catch(Exception ex) {
			ex.printStackTrace();
		} catch(Throwable tr) {
			tr.printStackTrace();
		} finally {
			if(tmpDir != null) {
				try {
					recursiveDelete(tmpDir);
				} catch(Exception ignorable) {
					ignorable.printStackTrace();
				}
			}
			if(tmpFile!=null) {
				tmpFile.delete();
			}
		}
		return new CompilationResult(Integer.valueOf(-1),false,messageToCompilationMessage("Error running compiler."));
	}

	private List<? extends CompilationMessage> listToCompilationMessages(List<String> errors) {
		List<CompilationMessage> list = new ArrayList<CompilationMessage>(errors.size());
		for(String e : errors) {
			String[] line = parseGHDLErrorMessage(e);
			if(line.length==4) {
				list.add(new CompilationMessage(line[0],line[3],Integer.parseInt(line[1]),Integer.parseInt(line[2])));
			} else {
				list.add(new CompilationMessage("",line[1],1,1));
			}
		}
		return list;
	}

	private List<? extends CompilationMessage> messageToCompilationMessage(String error) {
		List<CompilationMessage> list = new ArrayList<CompilationMessage>(1);
		list.add(new CompilationMessage("",error,1,1));
		return list;
	}

	private String[] parseGHDLErrorMessage(String msg) {
		String[] res = new String[] {msg,null,null,null};
		for(int i = 0; i < 3; i++) {
			int pos = msg.indexOf(':');
			if(pos!=-1) {
				res[i] = msg.substring(0,pos);
				msg = msg.substring(pos+1);
				res[i+1] = msg;
			} else {
				return new String[] {res[0],(res[1]==null?"":res[1])+(res[2]==null?"":":"+res[2])+(res[3]==null?"":":"+res[3])};
			}
		}
		if(res[0].toUpperCase().endsWith(".VHDL")) {
			res[0] = res[0].substring(0,res[0].length()-5);
		} else if(res[0].toUpperCase().endsWith(".VHD")) {
			res[0] = res[0].substring(0,res[0].length()-4);
		}
		return res;
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
				Thread t1 = consume("ulaz",procOutput, outputs); 
				t1.start();
				Thread t2 = consume("error",procError, errors);
				t2.start();
			} catch(Exception ignorable) {
			}
		}

		private Thread consume(final String name, final InputStream procStream, final List<String> list) {
			final Thread t = new Thread(new Runnable() {
				public void run() {
					if(procStream!=null) {
						try {
							BufferedReader br = new BufferedReader(new InputStreamReader(procStream));
							while(true) {
								String line = br.readLine();
								if(line == null) {
									break;
								}
								if(line.equals("")) {
									continue;
								}
								synchronized(list) {
									list.add(line);
								}
							}
						} catch(Exception ignorable) {
						}
					}
					synchronized (InputConsumer.this) {
						runningThreads--;
						InputConsumer.this.notifyAll();
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
