import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/* Class for all GUI setup */

@SuppressWarnings("serial")
class GUI extends JPanel implements ActionListener {
	private JPanel sidebar, visual, inputfield, 
				   center; 					//panels for sidebar, visualisation 
											//and input space
	private JLabel cInput;
	private JTextField textInput;
	private JButton enter,submit,clear,random;

	
	private GridBagConstraints c;	        //constraints for sidebar, visual 
										    //and inputs respectively
	private int w, x,x1,y1,y2,isize,div;
	private int xoff; 					    // x offset for visualisation
	private double w2,unit;
	private boolean dpinit = false;
	private int[][] dimensions,points;
	private boolean[] donePoints;
	private String output;
	private String[] sInputs;
    private boolean submitting = false;
    private Graphics2D g2;

	public ArrayList<Integer> iInputs = new ArrayList<Integer>();

    
    
	private Visualisation vis;
	
	public GUI(int w, int h) {
	//default constructor for the GUI
		this.setSize(w,h);
		setupLayout();

		vis	= new Visualisation(visual.getSize());
		//repaint();
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
			if(donePoints[donePoints.length-1])
				vis.action = false;
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
        
        if(submitting == true) {
            for(int i = 0;i<iInputs.size();i++) {
                int pointX = (int)((unit*iInputs.get(i))+div+20);
				//gets x coordinate of the point by multiplying the number by 
				//unit size and adding that to the divider's x coordinate.
				
				int pointY = y1+20+((i+1)*30);
				//orders points by the current value of i 
				
				points[i][0] = pointX;
				points[i][1] = pointY;
				
				//converting the individual entries into an x coordinate
                String iStr = iInputs.get(i) + "";

                g2.drawString(iStr,pointX,y1+15);
                g2.drawLine(pointX,y1+20,pointX,y1+40);
				g2.fillOval(pointX-5,pointY-5,10,10);
            }
			if(!vis.action)
            	clearText();
        }

        g2.drawLine(div,y1,div,y2+30);
        g2.drawLine(div+20,y1+30,x+10,y1+30);
		g2.drawRect(x1,y1,(int)(x*0.9),y2); //main body of the HDD
		g2.drawOval(x1,y1,w,y2); //The disk plate
		g2.drawPolygon(dimensions[0],dimensions[1],3);//The disk head)

		if(points.length > 0) {
			for(int i=0;i<(points.length-1);i++) {
				g2.drawLine(points[i][0],points[i][1],
							points[i+1][0],points[i+1][1]);
			}
		}
		if(vis.action) {
			animations();
		}
	}

	private void animations() {
		int i = Utility.checkPoints(donePoints);//i is the next point to be snapped to
		int sleepTime;
		//snap the disk head location straight to the default, otherwise wait
		if(donePoints[0]) 
			sleepTime = 1000;
		else
			sleepTime = 0;
	
		vis.action = true;

		dimensions = vis.getPoly(iInputs.get(i)*w2);
		donePoints[i] = true;
		System.out.println(i);
		System.out.println(sleepTime);
		Utility.sleep(sleepTime);	
		repaint();
	}

	private void setupLayout() {
	//sets up layout for program
		this.setLayout(new BorderLayout());
		
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
		Random r = new Random();		
		int[] rands = {r.nextInt(128),r.nextInt(128),r.nextInt(128),r.nextInt(128),r.nextInt(128)}; 
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
				if(cchar == ',' && i != 0) {
					output += cchar;
				} else if(Utility.isNum(cchar)) {
					output += cchar; //adds digits to a temp string for purposes of bound checking
				} else {
					JOptionPane.showMessageDialog(null,"Invalid input!");
					return;
				}
			}
			sInputs = output.split(",");
            iInputs.clear();
			for (i=0; i < sInputs.length; i++) {
				if(!Utility.inBounds(Integer.parseInt(sInputs[i]))) {
					JOptionPane.showMessageDialog(null,"Input "+sInputs[i]+" is not within bounds!");
					output = oldoutput;
					return;
				}
				iInputs.add(Integer.parseInt(sInputs[i]));
			}
			System.out.println(iInputs);
		
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
        if(cInput.getText().length() != 0) {
			dpinit = false;
            vis.setInputs(iInputs);
			vis.action = true;
            submitting = true;
            repaint();
        } else {
            JOptionPane.showMessageDialog(null,"Input text first!");
        }
    }

	public void actionPerformed(ActionEvent e) {
	//actionlistener, switch based on jbutton text
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
	}
}

