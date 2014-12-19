import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/* Class for all GUI setup */

@SuppressWarnings("serial")
class GUI extends JPanel implements ActionListener {
	private static boolean layoutDone = false;
	private JPanel buttons, visual, inputfield, 
				   center; 					//panels for buttons, visualisation 
											//and input space
	private JLabel cInput;
	private JTextField textInput;
	private JButton enter;

	
	private GridBagConstraints c;	        //constraints for buttons, visual 
										    //and inputs respectively
	private int w, h;
	private String[] sInputs;
	public int[] iInputs;
	
	public GUI(int w, int h) {
	//default constructor for the GUI
		super();
		this.w = w;
		this.h = h;
		this.setSize(w,h);
		setupLayout();
	}

	private void setupLayout() {
	//sets up layout for program
		if(layoutDone)
			return;
		this.setLayout(new BorderLayout());
		
		//setup split into multiple functions to make code cleaner
		buttonsSetup();
		centerSetup();

		layoutDone = true;
	}

	private void buttonsSetup() {
		buttons = new JPanel();
		JButton FCFS = new JButton("FCFS");
		JButton SSTF = new JButton("SSTF");
		JButton SCAN = new JButton("SCAN");

		buttons.setLayout(new GridLayout(4,2));
		buttons.add(FCFS);
		buttons.add(SSTF);
		buttons.add(SCAN);
	
		this.add(buttons,BorderLayout.WEST);
	}

	private void centerSetup() {
		visual = new JPanel();
		visual.add(new JLabel("Visualisation"));

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
		
		
		textInput = new JTextField(20);
		c.gridx = 0;
		c.gridy = 1;
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
		
		c.gridx = 1;
		c.gridy = 2;
		inputfield.add(new JButton("Submit"),c);

		c.gridx = 1;
		c.gridy = 3;
		inputfield.add(new JButton("Clear"),c);

		
		center = new JPanel(); //panel containing visualisation and input space

		center.setLayout(new GridLayout(2,1));

		center.add(visual);
		center.add(inputfield);

		this.add(center,BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		int i;
		String txt = textInput.getText();
		String output = "";
		for (i=0; i < txt.length(); i++) {
			if(txt.charAt(i) == ',' && i != 0) {
				output += txt.charAt(i);
			} else if(Utility.isNum(txt.charAt(i))) {
				output += txt.charAt(i);
			} else {
				continue;
			}
		}
		sInputs = output.split(",");
		iInputs = new int[sInputs.length];

		for (i=0; i < sInputs.length; i++) {
			iInputs[i] = Integer.parseInt(sInputs[i]);
		}

		System.out.println(iInputs[0]);
		cInput.setText(output);
	}
}
