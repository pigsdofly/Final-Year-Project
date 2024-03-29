/* Written by Samuel Pearce, ID: B223185*/
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/* Class to store details of visualisation */

class Visualisation {
	
	public int cmode; //current mode. Not static because visualisation object should only be made once
	private String[] modes = {"First Come First Serve","Shortest Seek Time First",
							  "SCAN","Circular-SCAN","C-LOOK"};
    public boolean action = false; //flag for animations

	private Dimension vSize;
	private int rectX, rectY,circX,graphX,graphY,centerX,centerY;
	private ArrayList<Integer> inputs;

	public Visualisation(Dimension vSize) {
	//constructor, parameter is the size of the parent JPanel
		cmode = 0;
		updateSize(vSize);
	}

	public void updateSize(Dimension vSize) {
		this.vSize = vSize;
		setRect();
        setGraph();
	}

	public void setInputs(ArrayList<Integer> inputs) {
		if (inputs.size() == 0) {
			JOptionPane.showMessageDialog(null,"No inputs!");
			return;
		}
		this.inputs = inputs;
	}

	public void changeMode(int mode) {
		cmode = mode;
		JOptionPane.showMessageDialog(null,"Algorithm set to "+modes[cmode]+"!");
	}

	public void setRect() {
        //setting x and y of rectangle to slightly smaller than the panel they live in
		rectX =(int) (vSize.width * 0.9); 
		rectY =(int) (vSize.height * 0.9);
		double cX = rectX * 0.4;
		circX = (int) cX;
	}
    
    public void setGraph() {
        graphX = (int) (rectX *0.75);
        graphY = rectY + 30;
    }

	public int getRectX() {
		return rectX;
	}

	public int getRectY() {
		return rectY;
	}
    
    public int getCircWidth() {
        return circX;
    }
    


	public int[][] getPoly(double u) {
    //dimensions for triangle representing disk head
	//tip of head position changes depending on given parameter
		centerX = (int) (rectX * 0.4) +(int) u - 5;
        centerY = (rectY + (rectY/10))/2;
		int[][] dimensions = {{centerX,(int)(rectX*0.7)-10, (int)(rectX*0.8)-10},{centerY,rectY+20, rectY}};
	
		System.out.println("centerX: "+centerX);

		return dimensions;	
	}

}
