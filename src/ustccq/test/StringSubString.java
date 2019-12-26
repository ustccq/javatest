package ustccq.test;

public class StringSubString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String theContent = "中国人民站起来了，世界真的就是一砣一砣翔。天天儿吃饭喝酒哈哈哈。";
		if (theContent .length() > 20)
			theContent = theContent.substring(0, 9) + "...";
		
		System.out.println("theContent:" + theContent);
	}

}
