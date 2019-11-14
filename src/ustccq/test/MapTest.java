package ustccq.test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("ch-zn", "中国人");
		maps.put("en-US", "美国人");
		
		System.err.println(maps.toString());
		
		System.err.println(maps.keySet().toString());
	}

}
