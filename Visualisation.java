import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Visualisation {
	
	private Dimension vSize;
	private int rectX;
	private int rectY;

	public Visualisation(Dimension vSize) {
		updateSize(vSize);
	}
	
	public void updateSize(Dimension vSize) {
		this.vSize = vSize;
		setRect();
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
