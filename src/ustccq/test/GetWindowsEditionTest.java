package ustccq.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class GetWindowsEditionTest {

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
	
		try {
			rt = Runtime.getRuntime();
			pr = rt.exec("SYSTEMINFO");
			in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
			try {
				if(!pr.waitFor(1, TimeUnit.MINUTES)) {
				    //timeout - kill the process. 
				    pr.destroy(); // consider using destroyForcibly instead
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// add all the lines into a variable
			StringBuilder sb = new StringBuilder();
			while ((line = in.readLine()) != null) {
//				System.out.println(line);
				if (line.startsWith("OS ")) {
					sb.append(line + "\n");
				}
//				if (line.contains(SEARCH_TERM)) // found the OS you are using
//				{
//					// extract the full os name
//					fullOSName = line.substring(line.lastIndexOf(SEARCH_TERM) + SEARCH_TERM.length(),
//							line.length() - 1);
//					break;
//				}
			}
			
			System.out.println(sb.toString());

			// extract the edition of windows you are using
//			for (String s : EDITIONS) {
//				if (fullOSName.trim().contains(s)) {
//					edition = s;
//				}
//			}

			System.out.println("The edition of Windows you are using is " + edition);

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
}
