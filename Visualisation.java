import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Visualisation {
	
	private enum mode {FCFS,SSTF,SCAN};

	private Dimension vSize;
	private int rectX;
	private int rectY;
	private ArrayList<Integer> inputs;

	public Visualisation(Dimension vSize) {
		updateSize(vSize);
	}

	public void updateSize(Dimension vSize) {
		this.vSize = vSize;
		setRect();
	}

	public void setInputs(ArrayList<Integer> inputs) {
		if (inputs.size() == 0) {
			JOptionPane.showMessageDialog(null,"No inputs!");
			return;
		}
		this.inputs = inputs;
	}

	public void setRect() {
		rectX =(int) (vSize.width * 0.9);
		rectY =(int) (vSize.height * 0.9);
	}

	public int getRectX() {
		return rectX;
	}

	public int getRectY() {
		return rectY;
	}
}
