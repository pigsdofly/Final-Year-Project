import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

/* Class for all GUI setup */

@SuppressWarnings("serial")
class GUI extends JPanel implements ActionListener {
	private JPanel sidebar, visual, inputfield, 
				   center; 					//panels for sidebar, visualisation 
											//and input space
	private JLabel cInput;
	private JTextField textInput;
	private JButton enter,submit,clear;

	
	private GridBagConstraints c;	        //constraints for sidebar, visual 
										    //and inputs respectively
	private int w, h;
	private int xoff; 					    // x offset for visualisation
	private String output;
	private String[] sInputs;
	public ArrayList<Integer> iInputs = new ArrayList<Integer>();

	private Visualisation vis;
	
	public GUI(int w, int h) {
	//default constructor for the GUI
		super();
		this.w = w;
		this.h = h;
		this.setSize(w,h);
		setupLayout();

		vis	= new Visualisation(visual.getSize());
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		vis.updateSize(visual.getSize());
		int x = vis.getRectX() + xoff;
		int y = vis.getRectY();

		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(xoff + (x/9),x,y/9,y);
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

		sidebar.setLayout(new GridLayout(4,2));
		sidebar.add(FCFS);
		sidebar.add(SSTF);
		sidebar.add(SCAN);
	
		this.add(sidebar,BorderLayout.WEST);
	
	}

	private void centerSetup() {
	//setup for everything that isn't the sidebar
		visual = new JPanel(); //Panel for the visualisation

		inputfield = new JPanel();
		inputfield.setLayout(new GridBagLayout());
		c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.ipadx = 1;
		inputfield.add(new JLabel("Inputs"),c);
		
		c.gridx = 0;
		c.gridy = 1;
		inputfield.add(new JLabel("Enter a list of numbers (between 1 and 255) separated by commas:"),c);
		
		textInput = new JTextField(20);
		c.gridx = 0;
		c.gridy = 2;
		c.ipadx = 5;
		inputfield.add(textInput,c);

		enter = new JButton("Enter");
		enter.addActionListener(this);
		c.gridx = 0;
		c.gridy = 3;
		inputfield.add(enter,c);

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

	private void textEntered() {
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
	
			for (i=0; i < sInputs.length; i++) {
				if(!Utility.inBounds(Integer.parseInt(sInputs[i]))) {
					JOptionPane.showMessageDialog(null,"Input "+sInputs[i]+" is not within bounds!");
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
		textInput.setText("");
		iInputs.clear();
		cInput.setText("");
		output = "";
	}

	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "Enter":  textEntered();
						   break;
			case "Submit": vis.setInputs(iInputs);
						   clearText();
						   break;
			case "Clear":  clearText();
						   break;
			default: System.out.println("How?");
		}
	}
}
