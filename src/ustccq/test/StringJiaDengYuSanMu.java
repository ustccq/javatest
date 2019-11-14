package ustccq.test;

public class StringJiaDengYuSanMu {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long a = System.currentTimeMillis() / 1000000;
		long b = System.currentTimeMillis() / 1000000;
		
		boolean flag = a == b;
		
		int orderIndex = 0;
		orderIndex += flag ? 111 : 0;
		System.out.println("odrerIndex is:" + orderIndex);
	}

}
