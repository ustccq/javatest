package ustccq.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandTest {   
	   
    public static void main(String[] args) {   
        try {   
            Runtime rt = Runtime.getRuntime();   
            Process pr = rt.exec("cmd /c systeminfo"); // cmd /c calc   
            // Process pr = rt.exec("D:\xunlei\project.aspx");   
   
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
            String line = null;
   
            while ((line = input.readLine()) != null) {   
                System.out.println(line);   
            }   
   
            int exitVal = pr.waitFor();   
            System.out.println("Exited with error code " + exitVal);   
   
        } catch (Exception e) {   
            System.out.println(e.toString());   
            e.printStackTrace();   
        }   
    }   
}
