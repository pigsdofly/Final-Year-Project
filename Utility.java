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
		if(cmode == 0)
			return inputs;
		
		
		return inputs;
	}
	
	static int checkPoints(boolean[] donePoints) {
		for(int i=0;i<donePoints.length;i++) {
			if(!donePoints[i])
				return i;
		}
		return donePoints.length -1;
	}

	static int countMovements(ArrayList<Integer> inputs) {
		int movements = 0;
		for(int i=0; i < inputs.size() -1;i++) {
			if(inputs.get(i) > inputs.get(i+1))
				movements += inputs.get(i) - inputs.get(i+1);
			else
				movements += inputs.get(i+1) - inputs.get(i);
		}
		return movements;

		}
}
