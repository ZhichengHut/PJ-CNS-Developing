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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import exceptions.ImageTypeNotSupportedException;

public class TestSteganalysis {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		/**
		 * BMPVisualAttack test
		 */
		File bmpfile = new File("Image/test7.bmp");
		BMPVisualAttack bmpimg = new BMPVisualAttack();
		bmpimg.init(bmpfile);
		bmpimg.setName("test");
		JPanel panelbmp = bmpimg.analyze();
		//JPanel Option = bmpimg.getOptionsPanel();
		
		JFrame frame0 = new JFrame("BMPVisualAttack");
		frame0.setLayout(new GridLayout(2,1,10,10));
		frame0.setSize(600, 400); 
		frame0.add(panelbmp);
		//frame0.add(Option);
		//frame0.setContentPane(Option);
		frame0.setContentPane(panelbmp);
		frame0.setVisible(true);

		
		/**GIFVisualAttack test
		 */
		/*File giffile = new File("Image/5.gif");
		GIFVisualAttack gifimg = new GIFVisualAttack();
		gifimg.init(giffile);
		gifimg.setName("a-5.gif");
		JPanel panelgif = gifimg.analyze();
		
		JFrame frame1 = new JFrame("GIFVisualAttack");
		frame1.setSize(600, 400); 
		frame1.setContentPane(panelgif);
		frame1.setVisible(true);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/

		/**BMPRSAnalysis test 
		 */
		/*File bmpfile = new File("Image/test7.bmp");
		BMPRSAnalysis test = new BMPRSAnalysis();
		test.init(bmpfile);
		JFrame frame = new JFrame("BMPRSAnalysis");
		JPanel panel = test.analyze();
		frame.setContentPane(panel);
		frame.setSize(300, 200); 
		frame.setVisible(true); 
		
	    //关闭窗口--退出调试
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
		
		/*BMPRSAnalysis test = new BMPRSAnalysis(2,2);
		File bmpfile = new File("Image/test2.bmp");
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
