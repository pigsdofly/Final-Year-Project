import javax.swing.*;
import java.lang.Exception;

public class main {
	private static final int w = 640;
	private static final int h = 480;
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
				UIManager.getCrossPlatformLookAndFeelClassName());
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
