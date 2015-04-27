/* Written by Samuel Pearce, ID: B223185*/
import javax.swing.*;
import java.lang.Exception;

public class main {
    //default size of frame hard-coded
	private static final int w = 1024;
	private static final int h = 768;
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
				UIManager.getCrossPlatformLookAndFeelClassName());//tries to change look and feel to  
																  //system default
		} catch (Exception e) {
			System.exit(0);
		}	
        //setting up the frame
		JFrame frame = new JFrame();
		frame.setSize(w,h);
		frame.setTitle("Algorithm Visualisation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //creating a new GUI instance, where the 'meat' of the program is
		GUI g = new GUI(w,h);
        //adding instance to frame and making it visible
		frame.add(g);
        frame.setResizable(false);
        frame.setVisible(true);
	}
}
