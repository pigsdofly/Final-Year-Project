import javax.swing.*;
import java.lang.Exception;

public class main {
	private static final int w = 640;
	private static final int h = 480;
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
				UIManager.getCrossPlatformLookAndFeelClassName());//changes look and feel from 
																  //ugly java default
			for (javax.swing.UIManager.LookAndFeelInfo info : 
				javax.swing.UIManager.getInstalledLookAndFeels()) {
		    	if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
					.equals(info.getClassName())) {   
			       	javax.swing.UIManager.setLookAndFeel(info.getClassName());
				          break;
					} 
			} //hacky way of getting look and feel working with GTK+
		} catch (Exception e) {
			return;
		}	
		
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(w,h);
		frame.setTitle("Algorithm Visualisation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GUI g = new GUI(w,h);
		frame.add(g);

	}
}
