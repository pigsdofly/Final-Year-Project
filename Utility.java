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
		if(cmode == 0 || inputs.size() == 1)
		//if the current mode is FCFS or the queue is too small
			return inputs;

		if(cmode == 1) {
			return sstf(inputs);
		}

		return inputs;
	
	}

	static ArrayList<Integer> sstf(ArrayList<Integer>inputs) {
	//sort for SSTF
	//there are probably more efficient sorts out there
	
		int i,j,k,temp,diff;

		for(i=0;i<inputs.size() -1;i++) {
			diff = 127;
			k=0;
			for(j=i+1;j<inputs.size();j++) {
			//inits j as next value in queue
				if(Math.abs(inputs.get(j)-inputs.get(i)) < diff) {
				//checks difference between values at j and i
					k = j;
					diff = Math.abs(inputs.get(j)-inputs.get(i));
					//saves the lowest
				}
					
			}
			if(k!=0) {
				temp = inputs.get(i+1);
				inputs.set(i+1,inputs.get(k));
				inputs.set(k,temp);
				System.out.println("Swapped");
			}
		}
		return inputs;
	}
	
	static int checkPoints(boolean[] donePoints) {
	//loops through donepoints and returns the first value that isn't true
		for(int i=0;i<donePoints.length;i++) {
			if(!donePoints[i])
				return i;
		}
		return donePoints.length -1;
	}

	static int countMovements(ArrayList<Integer> inputs) {
	//counts the disk head movements
		int movements = 0;
		for(int i=0; i < inputs.size() -1;i++) {
			movements += Math.abs(inputs.get(i) - inputs.get(i+1));
		}
		return movements;

	}

	static String queueToString(ArrayList<Integer> inputs,boolean[] donePoints,boolean pop) {
		int i;
		String result = ""; 
		for(i=0; i<inputs.size();i++) {
			if(!pop || (pop && !donePoints[i])) {
				result += inputs.get(i);
				if(i != inputs.size() -1) 
					result += ",";
			}

		}
	
		return result;
	}
}
