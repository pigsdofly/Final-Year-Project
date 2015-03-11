import java.lang.Exception;
import java.util.ArrayList;
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

	static ArrayList<Integer> sort(int cmode,ArrayList<Integer> inputs) {
		System.out.println("Nothing");
		return inputs;
	}
	
	static int checkPoints(boolean[] donePoints) {
		for(int i=0;i<donePoints.length;i++) {
			if(!donePoints[i])
				return i;
		}
		return donePoints.length -1;
	}

	static ArrayList<Integer> copyAL(ArrayList<Integer> inputs) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i< inputs.size();i++) {
			output.add(inputs.get(i));
		}
		return output;
	}
}
