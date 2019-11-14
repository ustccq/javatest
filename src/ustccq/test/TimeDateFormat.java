package ustccq.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDateFormat {
	public static void main(String[] cs) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		SimpleDateFormat formatterT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");  
		SimpleDateFormat formatterT2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		SimpleDateFormat formatterT3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		SimpleDateFormat formatterT4 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
		String ss = formatter.format(curDate);
		System.err.println(ss);
		System.err.println(formatterT.format(new Date()));
		System.err.println(formatterT2.format(new Date()));
		System.err.println(formatterT3.format(new Date()));
		System.err.println(formatterT4.format(new Date()));
	}
}
