/* Written by Samuel Pearce, ID: B223185*/
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/* Class for all GUI setup and output */

@SuppressWarnings("serial")
class GUI extends JPanel implements ActionListener {
	private JPanel sidebar, visual, inputfield, 
				   center; 					//panels for sidebar, visualisation 
											//and input space
	private JLabel cInput;
	private JTextField textInput;

	private Object[] options = {"Yes",
								"No" };     //JOptionPane options
	
	private GridBagConstraints c;	        //constraints for sidebar, visual 
										    //and inputs respectively
	private int x1,x2,isize,div;
	private int pointX,pointY;
	private int xoff; 					    // x offset for visualisation
	//private int sleepTime = 0;              
	private double diskUnit,graphUnit;
	private int[][] dimensions,points;
	private boolean[] donePoints;           //array that keeps track of which 
                                            //points on the graph have been visited
	private String output;
	private String[] sInputs;
    private boolean dpinit = false;         // flag to check if donePoints is initialised
	private boolean submitting = false;     // flag to check if the visualisation is active
	//private boolean stopped = false;
	private Graphics2D g2;

	private Timer timer;
    
	private ArrayList<Integer> iInputs = new ArrayList<Integer>();
	
	private Visualisation vis;
	
	public GUI(int width, int height) {
	//default constructor for the GUI
		super();
		this.setSize(width,height);
		setupLayout();

		vis	= new Visualisation(visual.getSize()); 
	}

	private void animations() { 
	//main loop function for the animations
		int i = Utility.checkPoints(donePoints);
		//i is the 'current' point being snapped to
		vis.action = true;
		//boolean variable showing that an action is taking place

		dimensions = vis.getPoly(iInputs.get(i)*diskUnit);
        //changes disk head position ('tip' of triangle) to current point
		donePoints[i] = true;
		repaint();

	}
	

	private void initVis() {
	//initializes all class variables used in drawing
		vis.updateSize(visual.getSize());
		xoff = sidebar.getSize().width;
		x1 = vis.getRectX() + xoff;
        x2 = x1/7;

		isize = iInputs.size();

		diskUnit = (x2 / 127.0); //disk divided into units
		graphUnit = ((x1+10) - (div+20)) / 127.0; 
		//amount of distance between units on the graph
		
        double divider = x1*0.75;
        div = (int) divider;
		
			
		points = new int[isize][2];
		if(!dpinit)
			initDonePoints();
			
		if(!vis.action)
			dimensions = vis.getPoly(0);
		else if(vis.action && !donePoints[0]) {
			dimensions = vis.getPoly(iInputs.get(0) *diskUnit);
		}
		try{	
			if(donePoints[donePoints.length-1]) {
				vis.action = false;
				timer.stop();
			}
		} catch(Exception e) {}

	}
	private void initDonePoints() {	
	//initializes the done points array as an array the same size as iInputs
		donePoints = new boolean[isize];
		for(int i=0;i<isize;i++) {
			donePoints[i] = false;
		}
		dpinit = true;
	}

	@Override
	public void paint(Graphics g) {
	//overriden JComponent paint function, this is where all the actual drawing goes
		super.paint(g);
		g2 = (Graphics2D) g;
		initVis();

        int y2 = vis.getRectY();
        int y1 = y2/10;//variables named after order of use
        int w = vis.getCircWidth();

		g2.setColor(Color.BLACK);
        
        if(submitting == true) {
			int th = y2-20-(y1+55);
			int hp = ((iInputs.size()-1)*30) > th ? th/iInputs.size():30;
			//sets 'gap' between points on graph

			int snap = vis.cmode>=3 ? Utility.findSnap(iInputs): 0;
			//saving the point the head 'snaps' to the opposite side
			
            for(int i = 0;i<iInputs.size();i++) {
				g2.setColor(Color.BLACK);
                pointX = (int)((graphUnit*iInputs.get(i))+div+20);
				//gets x coordinate of the point by multiplying the number by 
				//unit size and adding that to the divider's x coordinate.
				if(i==snap) {
					pointY = y1+40+(i*hp);
				} else
					pointY = y1+40+((i+1)*hp);
				//orders points by the current value of i 
				
				points[i][0] = pointX;
				points[i][1] = pointY;
				
				//converting the individual entries into an x coordinate
                String iStr = iInputs.get(i) + "";

				int strY = y1+15; 
				//draw label for point on graph, and line between current point and next one
                g2.drawString(iStr,pointX-5,strY);
                g2.drawLine(pointX,y1+20,pointX,y1+40);

				if(i+1 == Utility.checkPoints(donePoints)) {
					System.out.println(i+1);
					g2.setColor(Color.RED);
				}

				g2.fillOval(pointX-5,pointY-5,10,10);
            }
			g2.setColor(Color.BLACK);

			
			//code for set of strings at lower end of graph listing head movements,
			//a static queue of requests, and a moving queue of requests
			String mStr = "Total head movements: "+
						  Utility.countMovements(iInputs,vis.cmode);
			g2.drawString(mStr,div+30,y2-20);

			String inputString = "Requests: ";
			inputString += Utility.queueToString(iInputs,donePoints,false);

			String qString = "Queue: ";
			qString += Utility.queueToString(iInputs,donePoints,true);

			g2.drawString(inputString,div+30,y2-5);
			g2.drawString(qString,div+30,y2+10);

			for(int i=0;i<(points.length-1);i++) { 
				if(snap != 0 && i+1 == snap) {
					//if the algorithm is C-SCAN or C-LOOK, draws a dotted line where the disk
					//head jumps to the other side of the platter
					//there are two variations for each direction the line could be drawn
					if(points[i][0] < points[i+1][0]) {
					//line going from left-to-right
						for(int j = points[i][0];j < points[i+1][0];j+=20) {
						//loops and draws a group of smaller lines
							if(j+10>points[i+1][0]) {
							//if the line endpoint is past the x value of the next point
							//set the endpoint to that value
								g2.drawLine(j,points[i][1],
											points[i+1][0],points[i][1]);
							} else {
							//otherwise draw a line 10 pixels wide
								g2.drawLine(j,points[i][1],
											j+10,points[i][1]);
							}
						}
					} else {
					//line going from right-to-left
					//same as left-to-right, but conditionals are different
						for(int j = points[i][0];j > points[i+1][0];j-=20) {
							if(j-10<points[i+1][0]) {
								g2.drawLine(j,points[i][1],
											points[i+1][0],points[i][1]);
							} else {
								g2.drawLine(j,points[i][1],
											j-10,points[i][1]);
							}
						}
					}
				}  else {
					//otherwise just draw a line
					g2.drawLine(points[i][0],points[i][1],
								points[i+1][0],points[i+1][1]);
				}
			}
			
        }

        g2.drawLine(div+20,y1+30,x1+10,y1+30);//line for graph
		g2.drawRect(x2,y1,(int)(x1*0.9),y2); //main body of the visualisation

		g2.setColor(new Color(0x333333));
		g2.fillRect(x2,y1,div-x2,y2);//the background of the HDD
		g2.setColor(new Color(0xcccccc));
		g2.fillOval(x2,y1,w,y2); //The disk plate
		g2.setColor(new Color(0xb2b2b2));
		g2.fillPolygon(dimensions[0],dimensions[1],3);//The disk head)
		g2.fillOval(dimensions[0][1]-5,dimensions[1][2]-10,98,40);//base of disk head
	}


	private void setupLayout() {
	//sets up layout for program
		setLayout(new BorderLayout());
		
		//setup split into multiple functions to make code cleaner
		sidebarSetup();
		centerSetup();

	}

	private void sidebarSetup() {
	//setup for the sidebar
		sidebar = new JPanel();
		sidebar.setLayout(new GridLayout(5,1));
		String[] Labels = {"FCFS","SSTF","SCAN",
							"C-SCAN","C-LOOK"};
		String[] Tooltips = {"Serves requests in order they were input",
							 "Sorts requests by shortest seek time between them",
							 "Serves all requests closest to the disk head in" +
							 "one direction, then moves in the opposite " + 
							 "direction",
							 "Like SCAN, but snaps to the opposite end when it" + " reaches an edge",
							 "Like C-SCAN, but snaps to the request " + 
							 "closest to that edge" };
		JButton[] buttons = new JButton[5];
		for(int i=0;i<5;i++) {
		//loops through arrays of labels and tooltips and creates buttons from them
			buttons[i] = new JButton(Labels[i]);
			buttons[i].setToolTipText(Tooltips[i]);
			buttons[i].addActionListener(this);
			sidebar.add(buttons[i]);
		}	

		this.add(sidebar,BorderLayout.WEST);
	}

	private void centerSetup() {
	//setup for rest of the window
		visual = new JPanel(); //Panel for the visualisation
		visual.add(new JLabel("Visualisation: (HDD/Graph)"));
		inputfield = new JPanel();
		inputfield.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		//layout for the user inputs and buttons
		//default padding and weights for gridbag layout
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.ipadx = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		inputfield.add(new JLabel("Inputs"),c);
		
		c.gridx = 0;
		c.gridy = 1;
		inputfield.add(new JLabel("Enter a list of numbers (between 0 and 127) separated by commas (first number is disk head starting position):"),c);
		
		textInput = new JTextField(20);
		c.gridx = 0;
		c.gridy = 2;
		c.ipadx = 5;
		inputfield.add(textInput,c);

		JPanel enterP = new JPanel();

		enterP.setLayout(new GridLayout(1,2));
		JButton enter = new JButton("Enter");
		enter.setToolTipText("Enter the numbers");
		enter.addActionListener(this);
		enterP.add(enter);

		JButton random = new JButton("Random");
		random.setToolTipText("Add a specified number of random ints");
		random.addActionListener(this);
		enterP.add(random);

		c.gridx = 0;
		c.gridy = 3;
		inputfield.add(enterP,c);

		cInput = new JLabel("");
		c.gridx = 1;
		c.gridy = 1;
		inputfield.add(cInput,c);
		
		JButton submit = new JButton("Submit");
		submit.setToolTipText("Submit the entered numbers and start animation");
		submit.addActionListener(this);
		c.gridx = 1;
		c.gridy = 2;
		inputfield.add(submit,c);

		JButton clear = new JButton("Clear");
		clear.setToolTipText("Clear all inputs and stop animation");
		clear.addActionListener(this);
		c.gridx = 1;
		c.gridy = 3;
		inputfield.add(clear,c);

		
		center = new JPanel(); //panel containing visualisation and input space

		center.setLayout(new GridLayout(2,1));

		center.add(visual);
		center.add(inputfield);

		this.add(center,BorderLayout.CENTER);
	}
	private void addRandom() {
	//creates a user-specified amount of random numbers to add to output
		clearText();
		Random r = new Random();		
		int rl;
		try {
		//simple bounds test
			rl = Integer.parseInt(JOptionPane.showInputDialog(null,"How many numbers? [1-10]","5"));
			if(rl > 10 || rl < 0) {
				JOptionPane.showMessageDialog(null,"Out of bounds!");
				return;
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,"Input a number");
			return;
		}
		//fills an array with r1 random ints
		int[] rands = new int[rl];
		for(int i=0;i<rands.length;i++)
			rands[i] = r.nextInt(125) + 1;
		//adds randoms to iInputs and converts the numbers to string to be displayed
		String rString = "";
		for(int i=0;i<rands.length;i++) {
			iInputs.add(rands[i]);
			rString += ""+rands[i];
			if(!(i == rands.length -1)) {
				rString += ",";
			}
		}
		cInput.setText(rString);

	}

	private void textEntered() {
	//input handling and validation for the text input box
		int i;
		try {
			//checks if there is already an output string in the program memory
			String txt = textInput.getText();
			if(output == null) {
				output = "";
		    } else if(output.length() > 0) {	
			//adds a comma to the end of the output string if there isn't one
			//so added characters aren't rejected
				if(output.charAt((output.length() -1)) != ',' && 
					txt.charAt(0)!= ',') {
				   output += ",";
				}
			}
			String oldoutput = output;
			for (i=0; i < txt.length(); i++) {
				char cchar = txt.charAt(i);
				if((cchar == ',' && i != 0) || Utility.isNum(cchar)) {
					output += cchar; 
					//adds digits to a temp string for purposes of bound checking
				} else {
					JOptionPane.showMessageDialog(null,"Invalid input!");
					return;
				}
			}
			sInputs = output.split(",");
			//creates an array out of output to check bounds of each entry
			for (i=0; i < sInputs.length; i++) {
				if(!Utility.inBounds(Integer.parseInt(sInputs[i]))) {
					JOptionPane.showMessageDialog(null,"Input "+sInputs[i]+
												  " is not within bounds!");
					output = oldoutput;
					return;
				}
			}
		
			textInput.setText("");
			String temp = Utility.join(",",sInputs);
			cInput.setText(temp);	
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,"Input text first!");
			return;
		}
	}


	private void clearText() {
	//clears all inputs
		textInput.setText("");
		iInputs.clear();
		cInput.setText("");
		output = "";
        submitting = false;
	}
    
    private void submitInputs() {
	//sets all the flags for animating, then begins animation timer
		
		if(cInput.getText().length() != 0) {
			dpinit = false;
			iInputs = Utility.sort(vis.cmode,
								   Utility.textToArrayList(cInput.getText()));
          	//iInputs is reassigned each time to prevent a bug with C-SCAN
			vis.setInputs(iInputs);
			vis.action = true;
            submitting = true;
			repaint();

			timer = new Timer(1000,this);
			//create a timer that waits a second between animations
			timer.setInitialDelay(0);
			//will run immediately at first
			timer.start();
			
        } else {
            JOptionPane.showMessageDialog(null,"Input text first!");
        }
    }

	public void actionPerformed(ActionEvent e) {
	//actionlistener, switch based on jbutton text
		try {
		//if the action is from pressing on a button, go through this switch
			switch(e.getActionCommand()) {
				case "Enter":  textEntered();
							   break;
				case "Submit": submitInputs();
							   break;
				case "Clear":  clearText();
   	        	               repaint();
							   break;
				case "Random": addRandom();
							   break;
				case "FCFS": vis.changeMode(0);
							 break;
				case "SSTF": vis.changeMode(1);
							 break;
				case "SCAN": vis.changeMode(2);
							 break;
				case "C-SCAN":vis.changeMode(3); 
							  break;
				case "C-LOOK":vis.changeMode(4);
							  break;
			}
		} catch(Exception ex) {
		//otherwise, its from the timer
			animations();
		}
	}
}

