package ustccq.console.master;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandProcessBuilder {
//	private Logger logger = LoggerFactory.getLogger(CommandProcessBuilder.class);
    private StringWriter infos;
    private StringWriter errors;
    private int exitCode = -1;
    private String status = null;
    private String msg = null;
    
    private Process process = null;
    private StreamBoozer seInfo = null;
    private StreamBoozer seError = null;
    
    private File directory = null;
    private List<String> command = null;
    private Long timeoutDuration = null;
    
	private void waitStreamThreadDieAndLogIt() throws InterruptedException {
		this.seInfo.join();
		System.out.println("Command Input Stream \n"+this.getInfos());				    
		this.seError.join();
		System.out.println("Command Error Stream \n"+this.getErrors());
	}

	private int normalWaitSubProcessEnd() throws InterruptedException {
		do{
			this.exitCode = this.process.waitFor();
		}while(this.process.isAlive());
		this.msg = "Process terminated normally";
		waitStreamThreadDieAndLogIt();
		return 0;
	}
	
    public CommandProcessBuilder(File directory, List<String> command, Long timeoutDurationInSecond) {
       this.directory = directory;
       this.command = command;
       this.timeoutDuration = timeoutDurationInSecond;
    }
    
    /*return a status code
     * 0 : normal
     * 1 : timeout
     * 2 : Error
     * in order to get the exit code, the parent object should get the exit code from this process builder object.
    */
    public int execute(){
    	int statusCode = 2;
    	try {
	    	System.out.println("===============Executing command===============");
	    	System.out.println("Direcorty:"+directory);
	    	System.out.println(command.toString());
	    	System.out.println("Timeout:"+timeoutDuration);
	        this.infos = new StringWriter();
	        this.errors = new StringWriter();
	        ProcessBuilder pb = new ProcessBuilder(command);
	        if(directory != null){
	            pb.directory(directory);
	        }
	        this.process = pb.start();
	        this.seInfo = new StreamBoozer(process.getInputStream(), new PrintWriter(infos, true));
	        this.seError = new StreamBoozer(process.getErrorStream(), new PrintWriter(errors, true));
	        this.seInfo.start();
	        this.seError.start();
	        if(timeoutDuration != null){	        	
				if(!this.process.waitFor(timeoutDuration, TimeUnit.SECONDS)){
					this.process = this.process.destroyForcibly();
					//check immediately
					if(this.process.isAlive()){
						do{
							System.out.println("CommandProcessBuilder :: try to kill process again");
							this.process.destroyForcibly();
							this.process.waitFor();
						}while(this.process.isAlive());
					}
					System.out.println("Forcibly destroy signal of cmd "+command+" is triggered.");
					/*after sending the kill signal
					 * the program will not wait the sub process to be killed.
					 * the corresponding thread which started this process should be terminated. 
					*/
					this.msg = "Process destroyed due to timeout "+timeoutDuration+" "+TimeUnit.SECONDS.toString();
				    System.out.println("Command Input Stream \n"+this.infos.toString());
//				    this.seInfo.join();
				    System.out.println("Command Error Stream \n"+this.errors.toString());
//				    this.seError.join();
				    this.exitCode = Integer.MIN_VALUE; 
				    return statusCode = 1;
				}else{
					return statusCode = normalWaitSubProcessEnd();
				}	
	        }else{
	    		return statusCode = normalWaitSubProcessEnd();
	        }
		} catch (InterruptedException e) {
			System.err.println("CMD "+command+" is interrupted while waiting for timeout.");
			this.msg = "Process is interrupted while waiting for timeout.";
		    System.out.println("Command Input Stream \n"+this.infos.toString());
		    try {
			    waitStreamThreadDieAndLogIt();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return statusCode;
		} catch (IOException e) {			
			System.err.println("an I/O error occurs for CMD "+command);
			e.printStackTrace();
			this.msg = "An I/O error occurs";
		    System.out.println("Command Input Stream \n"+this.infos.toString());
		    try {
			    waitStreamThreadDieAndLogIt();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return statusCode;
		}   	
    }

    public CommandProcessBuilder(File directory, List<String> command) throws Exception {
        this(directory,command,null);
    }
    
    public CommandProcessBuilder(List<String> command) throws Exception {
        this(null,command,null);
    }
    
	public void kill() {
		if (this.process != null && this.process.isAlive()) {
			// try {
			System.out.println("CommandProcessBuilder : killing the process");
			this.process.destroyForcibly();
			// this.seError.join();
			// this.seInfo.join();
			System.out.println("CommandProcessBuilder : killed the process");
			// } catch (InterruptedException e) {
			// logger.error("Process already interrupted.",e);
			// }
		} else {
			System.out.println("Process is NULL or is already terminated.");
		}
	}
    
    public boolean isAlive(){
    	return this.process.isAlive();
    }

    public synchronized void setSeInfo(StreamBoozer seInfo) {
		this.seInfo = seInfo;
	}

	public synchronized void setSeError(StreamBoozer seError) {
		this.seError = seError;
	}

	public String getErrors() {
        return errors.toString();
    }
 
    public String getInfos() {
        return infos.toString();
    }
 
    public int getExitCode() {
        return this.exitCode;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public String getMessage() {
        return msg;
    }

	class StreamBoozer extends Thread {
        private InputStream in;
        private PrintWriter pw;
 
        StreamBoozer(InputStream in, PrintWriter pw) {
            this.in = in;
            this.pw = pw;
        }
 
        @Override
        public void run() {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ( (line = br.readLine()) != null) {
                    pw.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
