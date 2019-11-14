package ustccq.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CP1252 {

	public static void main(String[] args) {
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("systeminfo");
		Process shell;
		try {
			System.err.println("1当前时间:" + System.currentTimeMillis());
			shell = pb.start();
			synchronized (pb) {
				pb.wait(3000);
			}
			System.err.println("2当前时间:" + System.currentTimeMillis());

			InputStream shellIn = shell.getInputStream();

			InputStreamReader reader = new InputStreamReader(shellIn, "GBK");
			BufferedReader br = new BufferedReader(reader);

			String sCurrentLine;
			System.err.println("3当前时间:" + System.currentTimeMillis());
			while ((sCurrentLine = br.readLine()) != null) {
				System.err.println("4当前时间:" + System.currentTimeMillis());

				// ... omitting parse of sCurrentLine for brevity
				System.out.println("DOS String:" + sCurrentLine);
//				System.out.println("Ver".equals(sCurrentLine));
			}
			System.err.println("5当前时间:" + System.currentTimeMillis());

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("exception occured.");
		}

	}

}
