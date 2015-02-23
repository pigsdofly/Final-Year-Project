import java.lang.Exception;
class Utility {
	public Utility() {
	}

	static Boolean isNum(char c) {
		try { 
			Integer.parseInt(Character.toString(c));
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	static Boolean inBounds(int i) {
		System.out.println(i);
		if(i > 127 || i < 0) 
			return false;
		else
			return true;
	}

	static String join(String glue,String[] arr) {
		int i;
		String result = "";
		for(i=0; i < arr.length; i++) {
			if(i == arr.length - 1)
				result += arr[i];
			else
				result += arr[i] + glue;
		}
		return result;
	}
}
