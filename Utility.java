/* Written by Samuel Pearce, ID: B223185*/
import java.lang.Exception;
import java.util.ArrayList;

/* Class of static utility methods that wouldn't fit in elsewhere */

class Utility {
	public Utility() {
	}

	public static Boolean isNum(char c) {
	//returns boolean value if character is a number
		try { 
			Integer.parseInt(Character.toString(c));
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	public static Boolean inBounds(int i) {
    //method for checking if input is within bounds
		if(i > 127 || i < 0) 
			return false;
		else
			return true;
	}

	public static String join(String glue,String[] arr) {
	//joins an array of strings together with the 'glue' string
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

	public static ArrayList<Integer> sort(int cmode,ArrayList<Integer> inputs) {
	//chooses the type of sort to be used depending on the current mode
		switch(cmode) {
			case 0: return inputs;
			case 1: return sstf(inputs);
			case 2: return scan(inputs);
			case 3: return cscan(inputs,false);
			case 4: return cscan(inputs,true);
		}

		return inputs;
	
	}
	
	public static ArrayList<Integer> scan(ArrayList<Integer>inputs) {
	//sort for SCAN specifically
		int i,k,temp;
		int p = 0;
		boolean d;//flag for direction of movement
		d = inputs.get(0) >= 64 ? true : false; //set direction
		for(i=0;i<inputs.size()-1;i++) {
		
			k = scanLoop(i,d,inputs);
			if(k != 0) {
				inputs = swap(i+1,k,inputs);
			} else if(p==0) {
			//if nothing was swapped and there hasn't been a change of direction
			//set next value to nearest 'edge'
				d = !d;
				if(inputs.get(i) >= 64) {
					inputs.add(i+1,127);
				} else if(inputs.get(i) <64) {
					inputs.add(i+1,0);
				}
				p = i+1;
				//adding a temporary min/max value to find next point from
			}
		}
		if(p != 0)
			inputs.remove(p);
		
		return inputs;
	}

	public static ArrayList<Integer> cscan(ArrayList<Integer> inputs,boolean look) {
	//sort for C-SCAN or C-LOOK, depending on flag passed to function
		int i,k,temp;
		int p = 0;
		boolean d;//direction of movement
		d = inputs.get(0) >= 64 ? true : false;
		for(i=0;i<inputs.size()-1;i++) {
			k = scanLoop(i,d,inputs);
			if(k!=0)
				inputs = swap(i+1,k,inputs);
			else if(p==0) {
			//if nothing was swapped and there hasn't been a change of direction
			//swap to opposite side of disk
				if(inputs.get(i) >=64) {
					inputs.add(i+1,0);
				} if(inputs.get(i) <64)
					inputs.add(i+1,127);
				p=i+1;
			}
		}
		//if C-LOOK flag set, remove the temp value
		if(look)
			inputs.remove(p);
		return inputs;
	}
	
	public static int scanLoop(int i,boolean d,ArrayList<Integer>inputs) {
	//loops through inputs and looks for the value with the shortest seek time distance
	//in the same direction
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


	public static ArrayList<Integer> sstf(ArrayList<Integer>inputs) {
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

	private static ArrayList<Integer> swap(int i,int j, ArrayList<Integer>inputs) {
	//swaps ints in an arraylist
		int temp = inputs.get(i);
		inputs.set(i,inputs.get(j));
		inputs.set(j,temp);
		return inputs;
	}

	public static boolean checkDiff(int i, int j, int diff) {
	//method for checking if the difference betwen two ints is greater than the specified difference
		if(Math.abs(j - i) <= diff) {
			return true;
		}	

		return false;
	}
	
	public static int checkPoints(boolean[] donePoints) {
	//loops through donePoints and returns the first value that isn't true
		for(int i=0;i<donePoints.length;i++) {
			if(!donePoints[i])
				return i;
		}
		return donePoints.length;
	}

	public static int countMovements(ArrayList<Integer> inputs,int mode) {
	//counts the disk head movements
		int movements = 0;
		int snap = findSnap(inputs);
		for(int i=0; i < inputs.size() -1;i++) {
			if(mode >= 3 && i==snap-1 )
			//if the algorithm is C-LOOK or C-SCAN, don't count the 'snap'
				movements += 0;
			else
				movements += Math.abs(inputs.get(i) - inputs.get(i+1));
		}
		return movements;

	}

	public static int findSnap(ArrayList<Integer> inputs) {
	//function to find where the disk head snaps to the other side of the platter
	//in C-LOOK and C-SCAN
		int s,d;
		s=d=0;
		for(int i=0; i < inputs.size() - 1; i++) {
			if(Math.abs(inputs.get(i)-inputs.get(i+1)) > d) {
				System.out.println(d);
				d = Math.abs(inputs.get(i)-inputs.get(i+1));
				s = i+1;
			}
		}
		return s;
	}

	public static String queueToString(ArrayList<Integer> inputs,boolean[] donePoints,boolean pop) {
	//converts array list to a string representation
	//if the pop flag is set, method will ignore done points
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

	public static ArrayList<Integer> textToArrayList(String textInput) {
	//converts a string of numbers to an arraylist
		ArrayList<Integer> inputs = new ArrayList<Integer>();
		String[] sInputs = textInput.split(",");
		for(int i=0;i<sInputs.length;i++) {
			inputs.add(Integer.parseInt(sInputs[i]));
		}
		return inputs;	
	}
}
