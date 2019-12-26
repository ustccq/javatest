package ustccq.test;

import java.io.File;
import java.util.Scanner;

public class ExceptionGenerate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int dived = 100;
			int aaa = 3;
			int bbb = 6;
			File nFile = new File("C:\\nothing.txt");
			Scanner myReader = new Scanner(nFile);
			while(myReader.hasNextLine()) {
				String data = myReader.nextLine();
				System.out.println(data);
			}
			myReader.close();
			
			double re = dived / (bbb - 2 * aaa);
			System.out.println(re);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			StackTraceElement[] stes = e.getStackTrace();
			e.printStackTrace();
			System.out.println(stes[0]);
		}
	}
}
