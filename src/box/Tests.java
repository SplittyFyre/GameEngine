package box;

import java.io.IOException;
import java.util.HashMap;

public class Tests {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		HashMap<Integer, String> map = new HashMap<>();
		
		map.put(1, "ONE");
		map.put(2, "TWO");
		map.put(3, "THREE");
		map.put(4, "FOUR");
		
		System.out.println(map.get(4));

	}

}
