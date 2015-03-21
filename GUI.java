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
	private JButton enter,submit,clear,random;

	private Object[] options = {"Yes",
								"No" };     //JOptionPane options
	
	private GridBagConstraints c;	        //constraints for sidebar, visual 
										    //and inputs respectively
	private int w, x,x1,y1,y2,isize,div;
	private int xoff; 					    // x offset for visualisation
	private int sleepTime = 0;
	private double w2,unit;
	private boolean dpinit = false;
	private int[][] dimensions,points;
	private boolean[] donePoints;
	private String output;
	private String[] sInputs;
    private boolean submitting = false;
	private boolean stopped = false;
    private Graphics2D g2;

	private Timer timer;

	public ArrayList<Integer> iInputs = new ArrayList<Integer>();
    
	private Visualisation vis;
	
	public GUI(int w, int h) {
	//default constructor for the GUI
		super();
		this.setSize(w,h);
		setupLayout();

		vis	= new Visualisation(visual.getSize());
	}

	private void initVis() {
		//initializes all variables used in drawing
		vis.updateSize(visual.getSize());
		xoff = sidebar.getSize().width;
		x = vis.getRectX() + xoff;
        x1 = x/7;
        y2 = vis.getRectY();
        y1 = y2/10;//variables named after order of use
        w = vis.getCircWidth();

		isize = iInputs.size();

		w2 = (x1 / 127.0); //disk divided into units
		unit = ((x+10) - (div+20)) / 127.0; 
		//amount of distance between units on the graph
		
        double divider = x*0.75;
        div = (int) divider;
		
			
		points = new int[isize][2];
		if(!dpinit)
			initDonePoints();
			
		if(!vis.action)
			dimensions = vis.getPoly(0);
		else if(vis.action && !donePoints[0]) {
			dimensions = vis.getPoly(iInputs.get(0) *w2);
		}
		try{	
			if(donePoints[donePoints.length-1]) {
				vis.action = false;
				timer.stop();
			}
		} catch(Exception e) {}

	}
	private void initDonePoints() {	
		donePoints = new boolean[isize];
		for(int i=0;i<isize;i++) {
			donePoints[i] = false;
		}
		dpinit = true;
	}
	
	@Override
	public void paint(Graphics g) {
	//overriden JComponent paint function
		super.paint(g);
		g2 = (Graphics2D) g;
		initVis();
		g2.setColor(Color.BLACK);
		int pointX,pointY;
        
        if(submitting == true) {
			int th = y2-20-(y1+55);
			int hp = ((iInputs.size()-1)*30) > th ? th/iInputs.size():30;
			//sets 'gap' between points on graph

			int snap = vis.cmode>=3 ? Utility.findSnap(iInputs): 0;
			//saving the point the head 'snaps' to the opposite side
			
            for(int i = 0;i<iInputs.size();i++) {
				g2.setColor(Color.BLACK);
                pointX = (int)((unit*iInputs.get(i))+div+20);
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

				int strY = iInputs.get(i)%2 == 0 ? y1+15 : y1+55; 
				//if a value is even, place it above the graph, otherwise
				//place it below

                g2.drawString(iStr,pointX-5,strY);
                g2.drawLine(pointX,y1+20,pointX,y1+40);

				if(i+1== Utility.checkPoints(donePoints)) {
					System.out.println(i+1);
					g2.setColor(Color.RED);
				}

				g2.fillOval(pointX-5,pointY-5,10,10);
            }
			g2.setColor(Color.BLACK);
			
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
					//if the algorithm is C-SCAN or C-LOOK, set the line where
					//the disk head swaps to the other side of the platter to 
					//be dotted
					if(points[i][0] < points[i+1][0]) {
						for(int j = points[i][0];j < points[i+1][0];j+=20) {
							if(j+10>points[i+1][0]) {
								g2.drawLine(j,points[i][1],
											points[i+1][0],points[i][1]);
							} else {
								g2.drawLine(j,points[i][1],
											j+10,points[i][1]);
							}
						}
					} else {
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

        g2.drawLine(div+20,y1+30,x+10,y1+30);//line for graph
		g2.drawRect(x1,y1,(int)(x*0.9),y2); //main body of the visualisation

		g2.setColor(new Color(0x333333));
		g2.fillRect(x1,y1,div-x1,y2);//the background of the HDD
		g2.setColor(new Color(0xcccccc));
		g2.fillOval(x1,y1,w,y2); //The disk plate
		g2.setColor(new Color(0xb2b2b2));
		g2.fillPolygon(dimensions[0],dimensions[1],3);//The disk head)
		g2.fillOval(dimensions[0][1]-5,dimensions[1][2]-10,98,40);//base of disk head
	}

	public void update(Graphics g) {
		paint(g);
	}

	private void animations() { 
		int i = Utility.checkPoints(donePoints);//i is the next point to be snapped to
	
		vis.action = true;

		dimensions = vis.getPoly(iInputs.get(i)*w2);
		donePoints[i] = true;
		System.out.println(i);
		repaint();
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
		JButton FCFS = new JButton("FCFS");
		JButton SSTF = new JButton("SSTF");
		JButton SCAN = new JButton("SCAN");
		JButton CSCAN = new JButton("C-SCAN");
		JButton CLOOK = new JButton("C-LOOK");

		FCFS.addActionListener(this);
		SSTF.addActionListener(this);
		SCAN.addActionListener(this);
		CSCAN.addActionListener(this);
		CLOOK.addActionListener(this);

		sidebar.setLayout(new GridLayout(5,1));
		sidebar.add(FCFS);
		sidebar.add(SSTF);
		sidebar.add(SCAN);
		sidebar.add(CSCAN);
		sidebar.add(CLOOK);
	
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
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.ipadx = 1;
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
		enter = new JButton("Enter");
		enter.addActionListener(this);
		enterP.add(enter);

		random = new JButton("Random");
		random.addActionListener(this);
		enterP.add(random);

		c.gridx = 0;
		c.gridy = 3;
		inputfield.add(enterP,c);

		cInput = new JLabel("");
		c.gridx = 1;
		c.gridy = 1;
		inputfield.add(cInput,c);
		
		submit = new JButton("Submit");
		submit.addActionListener(this);
		c.gridx = 1;
		c.gridy = 2;
		inputfield.add(submit,c);

		clear = new JButton("Clear");
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
	//creates 5 random numbers to add to output
		clearText();
		Random r = new Random();		
		int rl;
		try {
			rl = Integer.parseInt(JOptionPane.showInputDialog(null,"How many numbers? [1-10]","5"));
			if(rl > 10 || rl < 0) {
				JOptionPane.showMessageDialog(null,"Out of bounds!");
				return;
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null,"Input a number");
			return;
		}

		int[] rands = new int[rl];
		for(int i=0;i<rands.length;i++)
			rands[i] = r.nextInt(125) + 1;
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
			String txt = textInput.getText();
			if(output == null) {
				output = "";
		    } else if(output.length() > 0) {		
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
            iInputs.clear();
			for (i=0; i < sInputs.length; i++) {
				if(!Utility.inBounds(Integer.parseInt(sInputs[i]))) {
					JOptionPane.showMessageDialog(null,"Input "+sInputs[i]+
												  " is not within bounds!");
					output = oldoutput;
					return;
				}
				iInputs.add(Integer.parseInt(sInputs[i]));
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
			iInputs = Utility.sort(vis.cmode,iInputs);
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

