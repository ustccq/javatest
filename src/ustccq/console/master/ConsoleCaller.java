package ustccq.console.master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConsoleCaller {

	public static void main(String[] args) {
		String bigStr = new String("LaTin");
		System.err.println(bigStr.toLowerCase());
		System.err.println(bigStr);
		int i = 0;
		while (++i < 2000) {
			System.err.println(String.format("第%d次运行脚本", i));
			doJmeterOnce();
			try {
				Long sleepSeconds = (new Random().nextInt() % 10) * 1000L;
				if (sleepSeconds < 1000L)
					sleepSeconds = 2000L;
				System.out.println(String.format("将要睡上%d秒", sleepSeconds/1000));
				Thread.sleep(sleepSeconds);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String jmeterCmd = "D:\\apache-jmeter-5.0\\bin\\jmeter -n -t D:\\apache-jmeter-5.0\\bin\\线程组.jmx -r -l D:\\apache-jmeter-5.0\\bin\\result%d.csv -e -o D:\\apache-jmeter-5.0\\bin\\tmp\\report%d";
	
	static void doJmeterOnce() {
		
		// TODO Auto-generated method stub
		String jmeterDir = "D:\\apache-jmeter-5.0\\bin\\";
		File dir = new File(jmeterDir);
		
		int index = 30;
		File indexFile = new File(jmeterDir + "\\remoteindex.ini");
		if (indexFile.exists() && indexFile.canRead()) {
			BufferedReader br = null;
			do {
				try {
					br = new BufferedReader(new FileReader(indexFile));
					String line = br.readLine();
					if (null == line || line.isEmpty())
						break;
					index = Integer.valueOf(line);
					++index;
				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} while (false);
		}

		try {
			OutputStream os = new FileOutputStream(indexFile);
			os.write(String.format("%d", index).getBytes());
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		doCall(index);
//		doCall1(dir, index);
		doCall2(dir, index);
	}

	private static void doCall1(File dir, int index) {
		List<String> commands = new ArrayList<String>();
//		commands.add(String.format("services.msc", index, index));
		commands.add("cmd");
		commands.add("/c");
//		commands.add(String.format("java -version", index, index));
		commands.add(String.format(jmeterCmd, index, index));
		
		System.out.println("command is:" + commands);
		try {
			CommandProcessBuilder cpBuilder = new CommandProcessBuilder(dir, commands, 600L);
			cpBuilder.execute();
			int exitCode = cpBuilder.getExitCode();
			System.err.println(exitCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void doCall(int index) {
		try {
			Runtime.getRuntime().exec("cmd /c " + String.format(jmeterCmd, index, index));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void doCall2(File dir, int index) {
		ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "D:\\apache-jmeter-5.0\\bin\\jmeter", "-n", "-t",
				"D:\\apache-jmeter-5.0\\bin\\线程组.jmx", "-r", "-l",
				String.format("D:\\apache-jmeter-5.0\\bin\\result%d.csv", index), "-e", "-o",
				String.format("D:\\apache-jmeter-5.0\\bin\\tmp\\report%d", index));

		pb.directory(dir);
		try {
			Process process = pb.start();
			BufferedReader cmdStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String cmdOutput;
			while ((cmdOutput = cmdStreamReader.readLine()) != null) {
				// outputStream.write((cmdOutput + "\n").getBytes());
				System.out.println(cmdOutput);
			}
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
