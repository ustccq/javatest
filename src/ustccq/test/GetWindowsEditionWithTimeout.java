package ustccq.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

//import javafx.concurrent.*;

public class GetWindowsEditionWithTimeout {
	
	private static class Worker
    extends Thread
{
    private final Process process;
    private Integer exitValue;

    Worker(final Process process)
    {
        this.process = process;
    }

    public Integer getExitValue()
    {
        return exitValue;
    }

    @Override
    public void run()
    {
        try
        {
            exitValue = process.waitFor();
        }
        catch (InterruptedException ignore)
        {
            return;
        }
    }
}
	
	public static void main(String[] args) {
		Runtime rt;
		Process pr;
		BufferedReader in;
		String line = "";
		String sysInfo = "";
		String edition = "";
		String fullOSName = "";
		final String SEARCH_TERM = "OS Name:";
		final String[] EDITIONS = { "Basic", "Home", "Professional", "Enterprise" };

		Properties sysProps = System.getProperties();
//		sysProps.list(System.out);
		StringBuilder sb = new StringBuilder();
		try {
			rt = Runtime.getRuntime();
			pr = rt.exec("SYSTEMINFO");
			Worker worker = new Worker(pr);
			
			try {
				System.err.println(System.currentTimeMillis());

				worker.start();
				worker.join(3000);			
				System.err.println(System.currentTimeMillis());
				InputStream shellIn = pr.getInputStream();
				InputStreamReader reader = new InputStreamReader(shellIn, "GBK");
				BufferedReader br = new BufferedReader(reader);
				
				while ((line = br.readLine()) != null) {
					if (line.startsWith("OS ")) {
//						sb.append(line + "\n");
						String[] lines = line.split(":");
						if (2 == lines.length)
							sb.append(lines[0] + "_" + lines[1] + "\n");
					}
				}
				System.err.println(System.currentTimeMillis());
				
			} catch (Exception ex) {
				worker.interrupt();
			    ex.printStackTrace();
			}

			System.out.println(sb.toString());

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
}
