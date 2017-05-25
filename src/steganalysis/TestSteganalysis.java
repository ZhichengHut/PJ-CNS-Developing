/**
 * Used to test Steganalysis
 * Delete later
 */
package steganalysis;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestSteganalysis {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//BMPVisualAttack test, length of output is 3
		File bmpfile = new File("Image/test.bmp");
		BMPVisualAttack bmpimg = new BMPVisualAttack(bmpfile);
		BufferedImage[] bmplsb = bmpimg.displayLSB("test2");
		
		//GIFVisualAttack test, length of output is 1
		/*File giffile = new File("Image/r3.gif");
		GIFVisualAttack gifimg = new GIFVisualAttack(giffile);
		BufferedImage[] giflsb = gifimg.displayLSB("a-r3.gif");*/
		
		//BMPRSAnalysis test
		/*BMPRSAnalysis test = new BMPRSAnalysis(2,2);
		File bmpfile = new File("Image/test.bmp");
		BufferedImage img;
		try {
			img = ImageIO.read(bmpfile);
			double[] result = test.Analysis(img, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*JFrame frame = new JFrame();
	    JLabel label = new JLabel(new ImageIcon(giflsb[0]));
	    frame.getContentPane().add(label, BorderLayout.CENTER);
	    frame.pack();
	    frame.setVisible(true); 
	    //关闭窗口--退出调试
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		
		
		System.out.println("Completed");

	}

}
