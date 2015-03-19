import java.lang.Exception;
import java.util.ArrayList;
class Utility {
	public Utility() {
	}

	static Boolean isNum(char c) {
	//returns boolean value if character is a number
		try { 
			Integer.parseInt(Character.toString(c));
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	static Boolean inBounds(int i) {
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
		switch(cmode) {
			case 0: return inputs;
			case 1: return sstf(inputs);
			case 2: return scan(inputs);
			case 3: return cscan(inputs,false);
			case 4: return cscan(inputs,true);
		}

		return inputs;
	
	}
	
	static ArrayList<Integer> scan(ArrayList<Integer>inputs) {
	//sort for SCANs of all kinds
		int i,k,temp;
		int p = 0;
		boolean d;//direction of movement
		d = inputs.get(0) >= 64 ? true : false;
		for(i=0;i<inputs.size()-1;i++) {
			System.out.println(inputs.get(p));
			k = scanLoop(i,d,inputs);
			if(k != 0) {
				inputs = swap(i+1,k,inputs);
			} else if(p==0) {
				d = !d;
				if(inputs.get(i) >= 64) {
					inputs.add(i+1,127);
				} else if(inputs.get(i) <64) {
					inputs.add(i+1,0);
				}
				p = i+1;
				//adding a temporary min/max value
			}
		}
		if(p != 0)
			inputs.remove(p);
		
		return inputs;
	}

	static ArrayList<Integer> cscan(ArrayList<Integer> inputs,boolean look) {
		int i,k,temp;
		int p = 0;
		boolean d;//direction of movement
		d = inputs.get(0) >= 64 ? true : false;
		for(i=0;i<inputs.size()-1;i++) {
			k = scanLoop(i,d,inputs);
			if(k!=0)
				inputs = swap(i+1,k,inputs);
			else if(p==0) {
				if(inputs.get(i) >=64) {
					inputs.add(i+1,0);
				} if(inputs.get(i) <64)
					inputs.add(i+1,127);
				p=i+1;
			}
		}
		if(look)
			inputs.remove(p);
		return inputs;
	}
	
	static int scanLoop(int i,boolean d,ArrayList<Integer>inputs) {
		int diff,k,j;
		diff = 127;
		k = 0;
		for(j=i+1;j<inputs.size();j++) {
			if(d && inputs.get(i) < inputs.get(j)) {
				if(checkDiff(inputs.get(j),inputs.get(i),diff)) {
					k = j;
					diff = inputs.get(j) - inputs.get(i);
				} 	
			} else if(!d && inputs.get(i) > inputs.get(j)) {
				if(checkDiff(inputs.get(i),inputs.get(j),diff)) {
					k = j;
					diff = inputs.get(i) - inputs.get(j);
				}	
			}
		}
		return k;
	}


	static ArrayList<Integer> sstf(ArrayList<Integer>inputs) {
	//sort for SSTF
		int i,j,k,temp,diff;

		for(i=0;i<inputs.size() -1;i++) {
			diff = 127;
			k=0;
			for(j=i+1;j<inputs.size();j++) {
			//inits j as next value in queue
				if(checkDiff(inputs.get(j),inputs.get(i),diff)) {
				//checks difference between values at j and i
					k = j;
					diff = Math.abs(inputs.get(j)-inputs.get(i));
					//saves the lowest
				}
					
			}
			if(k!=0) {
				inputs = swap(i+1,k,inputs);
			}
		}
		return inputs;
	}

	static ArrayList<Integer> swap(int i,int j, ArrayList<Integer>inputs) {
		int temp = inputs.get(i);
		inputs.set(i,inputs.get(j));
		inputs.set(j,temp);
		return inputs;
	}

	static boolean checkDiff(int i, int j, int diff) {
		if(Math.abs(j - i) <= diff) {
			return true;
		}	

		return false;
	}
	
	static int checkPoints(boolean[] donePoints) {
	//loops through donepoints and returns the first value that isn't true
		for(int i=0;i<donePoints.length;i++) {
			if(!donePoints[i])
				return i;
		}
		return donePoints.length;
	}

	static int countMovements(ArrayList<Integer> inputs,int mode) {
	//counts the disk head movements
		int movements = 0;
		for(int i=0; i < inputs.size() -1;i++) {
			if(mode == 3 && (inputs.get(i+1) == 0 || inputs.get(i+1) == 127))
				movements += 0;
			else
				movements += Math.abs(inputs.get(i) - inputs.get(i+1));
		}
		return movements;

	}

	static String queueToString(ArrayList<Integer> inputs,boolean[] donePoints,boolean pop) {
	//converts array list to a string representation
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
