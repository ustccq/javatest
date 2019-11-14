package ustccq.test;

public class StringFormatTest {

	public static void main(String[] args) {
		String container = "$(a)=b&$(a)=123;";
		Object box = container;
		String tmpString = (String)box;
		String tmpString2 = box.toString();
		System.out.println("Cast to string from box:" + tmpString);
		System.out.println("Call toString method from box:" + tmpString2);
		container = container.replace("$(a)", "kk");
		System.out.println(container);
		String nullStr = null;
		String resultString = String.format("the result is %s", nullStr);
		System.out.println(resultString);
		
		String runUrl = "http://www.meowlomo.com/{testCaseId}/delete";
		int testCaseId = 1011;
		runUrl = runUrl.replace("{testCaseId}", String.valueOf(testCaseId));
		System.out.println("runUrl:" + runUrl);
		// TODO Auto-generated method stub
		System.out.println(String.format("%1$04s", "12"));
	}

}
