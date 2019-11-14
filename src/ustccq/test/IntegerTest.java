package ustccq.test;

public class IntegerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.err.println(Integer.valueOf("22222222"));
		System.err.println(Integer.valueOf("0012300123"));
		System.err.println(Integer.valueOf("中国人哈哈哈123"));
		System.err.println(Integer.valueOf("2222222222222222222222222222222"));
		System.err.println(Integer.valueOf("1.23"));
		System.err.println(Integer.valueOf("111111111111111111111111.2999999999993"));
		System.err.println(Integer.valueOf("中国人哈哈哈1.23"));
		System.err.println(Integer.valueOf("1.123123000"));
		System.err.println(Integer.valueOf("12....23"));
		System.err.println(Integer.valueOf("1e5"));
		System.err.println(Integer.valueOf("log1000"));
		System.err.println(Integer.valueOf("pow(2,3)"));
	}

}
