/**
 * Used to test Steganalysis
 * Delete later
 */
package steganalysis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TestSteganalysis {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		/**
		 * BMPVisualAttack test
		 */
		/*BMPVisualAttack bmpimg = new BMPVisualAttack();
		bmpimg.initialize(Paths.get("Image/test7.bmp"));
		bmpimg.setName("test");
		JPanel panelbmp = bmpimg.Analysis();
		JPanel Option = bmpimg.OptionsofAnalysis();
		
		JFrame frame0 = new JFrame("BMPVisualAttack");
		frame0.setSize(600, 400); 
		frame0.add(panelbmp);
		frame0.setContentPane(panelbmp);
		frame0.setVisible(true);
		
		JFrame frame1 = new JFrame("Option");
		frame1.setSize(600, 400); 
		frame1.add(Option);
		frame1.setContentPane(Option);
		frame1.setVisible(true);
		
		frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		
		/**GIFVisualAttack test
		 */
		/*GIFVisualAttack gifimg = new GIFVisualAttack();
		gifimg.initialize(Paths.get("Image/5.gif"));
		JPanel panelgif = gifimg.Analysis();
		
		JFrame frame1 = new JFrame("GIFVisualAttack");
		frame1.setSize(600, 400); 
		frame1.setContentPane(panelgif);
		frame1.setVisible(true);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/

		
		/**BMPRSAnalysis test 
		 */
		/*BMPRSAnalysis test = new BMPRSAnalysis();
		test.initialize(Paths.get("Image/test7.bmp"));
		JFrame frame = new JFrame("BMPRSAnalysis");
		JPanel panel = test.Analysis();
		JPanel setParameter = test.OptionsofAnalysis();
		
		frame.setContentPane(panel);
		frame.setSize(300, 200); 
		frame.setVisible(true); 
		
		JFrame frame1 = new JFrame("Set Parameter");
		frame1.setSize(600, 400); 
		frame1.add(setParameter);
		frame1.setContentPane(setParameter);
		frame1.setVisible(true);
		
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		
		/**GIFRSAnalysis test 
		 */
		/*GIFRSAnalysis test = new GIFRSAnalysis();
		test.initialize(Paths.get("Image/5.gif"));
		JFrame frame = new JFrame("GIFRSAnalysis");
		JPanel panel = test.Analysis();
		JPanel setParameter = test.OptionsofAnalysis();
		
		frame.setContentPane(panel);
		frame.setSize(300, 200); 
		frame.setVisible(true); 
		
		JFrame frame1 = new JFrame("Set Parameter");
		frame1.setSize(600, 400); 
		frame1.add(setParameter);
		frame1.setContentPane(setParameter);
		frame1.setVisible(true);
		
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		
		System.out.println("Completed");

	}

}
