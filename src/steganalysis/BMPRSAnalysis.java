/**
 * RS Analysis algorithm derives from "Reliable detection of LSB steganography
 * in color and grayscale images" by J. Fridrich, M. Goljan and R. Du. 
 * 
 * During coding I have consulted the code b3dk7 publishes on GitHub
 * {@link https://github.com/b3dk7/StegExpose/}
 * And should also thank Bastien Faure and Kathryn Hempstalk for publishing their source code
 */


package steganalysis;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class BMPRSAnalysis implements SteganalysisMethod{
	private File sourceimage;
	private BufferedImage img = null;
	private int m = 2;
	private int n = 2;
	private boolean overlap = true;
	public static final int COLOR_RED = 0;
	public static final int COLOR_GREEN = 1;
	public static final int COLOR_BLUE = 2;
	
	/**
	 * Initialize the image to be read in
	 */
	@Override
	public void initialize(Path file) throws IOException{
		sourceimage = new File(file.toString());
		img = ImageIO.read(sourceimage);
	}

	@Override
	/**
	 * Set the size of analysis block to be m*n
	 * and the Overlap
	 * @param n
	 */
	public JPanel OptionsofAnalysis(){
		// TODO Auto-generated method stub
		JPanel SetPara = new JPanel();
		
		//set the size of mask
		//need to be improved: when the parameter is illegal, show the warning message
		//such as: when M and N are too big, are not an integer
		SetPara.add(new JLabel("Please set the value of M: "));
		JTextField M_num = new JTextField("2", 5);
		SetPara.add(M_num);
		
		SetPara.add(new JLabel(" and the value of N: "));
		JTextField N_num = new JTextField("2", 5);
		SetPara.add(N_num);
		
		//the set button
		JButton SetActive = new JButton("Set");
		SetPara.add(SetActive);
		SetActive.addActionListener(e -> {
			this.m = Integer.parseInt(M_num.getText());
			this.n = Integer.parseInt(N_num.getText());
			//System.out.println("m = " + this.m + " n = " + this.n);
		});
		
		//Set the overlap parameter
		SetPara.add(new JLabel("Do you want to overlap the mask?"));
		JRadioButton OverLap_true = new JRadioButton("True");
        JRadioButton OverLap_false = new JRadioButton("False");  
        OverLap_true.setSelected(true);
        
        ButtonGroup buttonGroup = new ButtonGroup();  
        buttonGroup.add(OverLap_true);  
        buttonGroup.add(OverLap_false);  
        
        SetPara.add(OverLap_false);
        SetPara.add(OverLap_true);
        
        OverLap_false.addActionListener(e -> {
        	OverLap_false.setSelected(true);
        	this.overlap = false;
        	//System.out.println(this.overlap);
        });
		
        OverLap_true.addActionListener(e -> {
        	OverLap_true.setSelected(true);
        	this.overlap = true;
        	//System.out.println(this.overlap);
        });
        
		return SetPara;
	}
	
	/**
	 * The function that user can used to get the analysis result
	 * @return: the analysis result
	 */
	@Override
	public JPanel Analysis() throws Exception {
		double[] result = new double[3];
		for(int i=0; i < 3; i++){
			result[i] = Analysis_Color(img, i, overlap);
			System.out.println(result[i]);
		}
		
		JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        panel.add(new JLabel("R  : " + result[0]));
        panel.add(new JLabel("G  : " + result[1]));
        panel.add(new JLabel("B  : " + result[2]));
        panel.add(new JLabel("Average : " + ((result[0] + result[1] + result[2])/3.0)));
        return panel;
	}
	
	/**
	 * Analysis for 3 colors
	 * @param img
	 * @param color
	 * @param overlap
	 * @return
	 */
	public double Analysis_Color(BufferedImage img, int color, boolean overlap){
		int[] mask0 = new int[m*n];
		int[] mask1 = new int[m*n];
		int[] allmask = new int[m*n];
		for(int i = 0; i < m * n; i++){
			mask0[i] = i % 2;
			mask1[i] = (i + 1) % 2;
			allmask[i] = 1;
		}
		
		double[] result0 = doAnalysis(img, color, overlap, mask0, allmask, false);
		double[] result1 = doAnalysis(img, color, overlap, mask1, allmask, false);
		double[] result2 = doAnalysis(img, color, overlap, mask0, allmask, true);
		double[] result3 = doAnalysis(img, color, overlap, mask1, allmask, true);
		
		//double numGroup = result0[4] + result1[4];

		double r = result0[0] + result1[0];
		double rm = result0[2] + result1[2];
		double r1 = result2[0] + result3[0];
		double rm1 = result2[2] + result3[2];
		double s = result0[1] + result1[1];
		double sm = result0[3] + result1[3];
		double s1 = result2[1] + result3[1];
		double sm1 = result2[3] + result3[3];
		
		/*System.out.println(color);
		System.out.println(r+" "+rm+" "+r1+" "+rm1);
		System.out.println(s+" "+sm+" "+s1+" "+sm1);
		System.out.println(1 + " " + result0[0] +" "+ result1[0]);
		System.out.println(2 + " " + result0[2] +" "+ result1[2]);
		System.out.println(3 + " " + result2[0] +" "+ result3[0]);
		System.out.println(4 + " " + result2[2] +" "+ result3[2]);
		System.out.println(5 + " " + result0[1] +" "+  result1[1]);
		System.out.println(6 + " " + result0[3] +" "+  result1[3]);
		System.out.println(7 + " " + result2[1] +" "+  result3[1]);
		System.out.println(8 + " " + result2[3] +" "+  result3[3]);*/
		
		double x = getX(r,  rm,  r1,  rm1, s,  sm,  s1,  sm1);
		
		double ml;
		
		if(x - 0.5 == 0)
			ml = 0;
		else
			ml = Math.abs(x / (x - 0.5));
		
		return ml;
	}
	
	/**
	 * Gets the x value to get p=x/(x-1/2) according to RS equation. 
	 * Described in paper 
	 * "Reliable detection of LSB steganography in color and grayscale images".
	 * Quote from https://github.com/b3dk7/StegExpose
	 *
	 * @param r The value of Rm(p/2).
	 * @param rm The value of R-m(p/2).
	 * @param r1 The value of Rm(1-p/2).
	 * @param rm1 The value of R-m(1-p/2).
	 * @param s The value of Sm(p/2).
	 * @param sm The value of S-m(p/2).
	 * @param s1 The value of Sm(1-p/2).
	 * @param sm1 The value of S-m(1-p/2).
	 * @return The value of x.
	 */
	private double getX(double r, double rm, double r1, double rm1,
			double s, double sm, double s1, double sm1){
		double x = 0; //the cross point.
		
		double d0 = r - s; 		// d0 = Rm(p/2) - Sm(p/2)
		double dm0 = rm - sm; 	// d-0 = R-m(p/2) - S-m(p/2)
		double d1 = r1 - s1; 	// d1 = Rm(1-p/2) - Sm(1-p/2)
		double dm1 = rm1 - sm1; // d-1 = R-m(1-p/2) - S-m(1-p/2)
		
		//get x as the root of the equation 
		//2(d1 + d0)x^2 + (d-0 - d-1 - d1 - 3d0)x + d0 - d-0 = 0
		//derive from the paper on page 3
		
		double a = 2 * (d1 + d0);
		double b = dm0 - dm1 - d1 - (3 * d0);
		double c = d0 - dm0;
		
		if(a == 0)
		    //take it as a straight line
		    x = c / b;
		  		
		//take it as a curve
		double delta = Math.pow(b, 2) - (4 * a * c);
		
		if(delta >= 0){
			double rootpos = ((-1 * b) + Math.sqrt(delta)) / (2 * a);
			double rootneg = ((-1 * b) - Math.sqrt(delta)) / (2 * a);
			
			//return the root with the smallest absolute value (as per paper)
			if(Math.abs(rootpos) <= Math.abs(rootneg))
				x = rootpos;
			else
				x = rootneg;
		}else{
			//maybe it's not the curve we think (straight line)
			double cr = (rm - r) / (r1 - r + rm - rm1);
			double cs = (sm - s) / (s1 -s + sm - sm1);
			x = (cr + cs) / 2;
		}
		
		if(x == 0){
			double ar = ((rm1 - r1 + r - rm) + (rm - r) / x) / (x - 1);
			double as = ((sm1 - s1 + s - sm) + (sm - s) / x) / (x - 1);
			if(as > 0 | ar < 0){
				//let's assume straight lines again...
				double cr = (rm - r) / (r1 - r + rm - rm1);
				double cs = (sm - s) / (s1 -s + sm - sm1);
				x = (cr + cs) / 2; 
			}
		}
		return x;
	}
	
	/**
	 * 
	 * @param img
	 * @param color
	 * @param overlap
	 * @param mask
	 * @return: return a double array with size of 5
	 * result[0] = number of Regular
	 * result[1] = number of Singular
	 * result[2] = number of Negative Regular;
	 * result[3] = number of Negative Singular;
	 * result[4] = the total number of groups
	 */
	private double[] doAnalysis(BufferedImage img, int color, boolean overlap, 
		int[] mask,int[] allmask, boolean if_flip){
		int width = img.getWidth();
		int height = img.getHeight();
		int[] block = new int[m * n];
		double discriminationO, discriminationF, discriminationN;
		double numReg = 0, numNegReg = 0, numSin = 0, numNegSin = 0, numUnu = 0, numNegUnu = 0;
		
		int step_x = overlap ? 1 : m;
		int step_y = overlap ? 1 : n;
		
		for(int x = 0; x < width - m; x += step_x){
			for(int y = 0; y < height - n; y += step_y){
				//Operation on each block
				//Get the pixel value and save in block
				int k = 0;
				for(int i = 0; i < n; i++){
					for(int j = 0; j < m; j++){
						block[k] = img.getRGB(x + j, y + i);
						k++;
					}
				}
				
				if(if_flip){
					block = flipping(block, allmask);
				}
				
				//Calculate the discrimination on original blocks
				discriminationO = getDiscrimination(block, color);

				//Calculate the discrimination on flipped blocks
				block = flipping(block, mask);
				discriminationF = getDiscrimination(block, color);

				//flip the block back
				block = flipping(block, mask);
				
				//negative the mask and calculate the discrimination
				mask = negMask(mask);
				//In order to keep value 256, getDiscrimination can not be reused here
				discriminationN = getNegativeDiscrimination(block, color, mask);
				mask = negMask(mask);
				
				//determine the block belong to which group
				if(discriminationF > discriminationO)
					numReg++;
				else if(discriminationF < discriminationO)
					numSin++;
				else
					numUnu++;
				
				if(discriminationN > discriminationO)
					numNegReg++;
				else if(discriminationN < discriminationO)
					numNegSin++;
				else
					numNegUnu++;
			}
		}
		
		double[] result = new double[5];
		result[0] = numReg;
		result[1] = numSin;
		result[2] = numNegReg;
		result[3] = numNegSin;
		result[4] = numReg + numSin + numUnu;  //the total number of groups
		
		return result;
	}
	
	/**
	 * Calculate the discrimination, the block size is divided into 4
	 * If the block is bigger than 4, it will be divided into sub-block
	 * and operate on sub-block respectively
	 * The function is defined as |x0-x1|+|x1-x3|+|x3-x2|+|x2-x0|
	 * @param block: 
	 * @param color: The color to be analysized
	 * @return: the value of discrimination
	 */
	private double getDiscrimination(int[] block, int color){
		double discrimination = 0;
		int color1, color2;
		for(int i = 0; i < block.length; i += 4){
			color1 = getPixelColor(block[i + 0], color);
			color2 = getPixelColor(block[i + 1], color);
			discrimination += Math.abs(color1 - color2);
			
			color1 = getPixelColor(block[i + 1], color);
			color2 = getPixelColor(block[i + 3], color);
			discrimination += Math.abs(color1 - color2);
			
			color1 = getPixelColor(block[i + 3], color);
			color2 = getPixelColor(block[i + 2], color);
			discrimination += Math.abs(color1 - color2);
			
			color1 = getPixelColor(block[i + 2], color);
			color2 = getPixelColor(block[i + 0], color);
			discrimination += Math.abs(color1 - color2);
		}
		return discrimination;
	}

	/**
	 * Similar to discrimination.
	 * But 256 & 0xff = 0, so we can't reuse the function getDiscrimination
	 * Because we need the color value to be 256 in this case
	 * @param block: 
	 * @param color: The color to be analysized
	 * @param mask: the inversed mask
	 * @return: the value of discrimination
	 */
	private double getNegativeDiscrimination(int[] block, int color, int[] mask){
		double discrimination = 0;
		int color1, color2;
		for(int i = 0; i < block.length; i = i + 4){
			color1 = getPixelColor(block[0 + i], color);
			color2 = getPixelColor(block[1 + i], color);
			if(mask[0 + i] == -1)
				color1 = shiftflipFunc(color1);
			if(mask[1 + i] == -1)
				color2 = shiftflipFunc(color2);
			discrimination += Math.abs(color1 - color2);
			
			color1 = getPixelColor(block[1 + i], color);
			color2 = getPixelColor(block[3 + i], color);
			if(mask[1 + i] == -1)
				color1 = shiftflipFunc(color1);
			if(mask[3 + i] == -1)
				color2 = shiftflipFunc(color2);
			discrimination += Math.abs(color1 - color2);
			
			color1 = getPixelColor(block[3 + i], color);
			color2 = getPixelColor(block[2 + i], color);
			if(mask[3 + i] == -1)
				color1 = shiftflipFunc(color1);
			if(mask[2 + i] == -1)
				color2 = shiftflipFunc(color2);
			discrimination += Math.abs(color1 - color2);
			
			color1 = getPixelColor(block[2 + i], color);
			color2 = getPixelColor(block[0 + i], color);
			if(mask[2 + i] == -1)
				color1 = shiftflipFunc(color1);
			if(mask[0 + i] == -1)
				color2 = shiftflipFunc(color2);
			discrimination += Math.abs(color1 - color2);
		}
		return discrimination;
	}
	
	//flipping the block according to the mask
	private int[] flipping(int[] block, int[] mask){
		int new_color = 0;
		int red, green, blue;
		for(int i = 0; i < block.length; i++){
			if(mask[i] == 1){
				red = flipFunc(getRed(block[i]));
				green = flipFunc(getGreen(block[i]));
				blue = flipFunc(getBlue(block[i]));
				new_color = (0xff << 24) | ((red  & 0xff) << 16) | ((green & 0xff) << 8) | ((blue & 0xff));
				block[i] = new_color;
			}
			
			else if(mask[i] == -1){
				red = shiftflipFunc(getRed(block[i]));
				green = shiftflipFunc(getGreen(block[i]));
				blue = shiftflipFunc(getBlue(block[i]));
				new_color = (0xff << 24) | ((red  & 0xff) << 16) | ((green & 0xff) << 8) | ((blue & 0xff));
				block[i] = new_color;
			}
		}
		return block;
	}
	
	//The flip and shift flip function
	//Defined in "Reliable detection of LSB steganography in color and grayscale images"
	private int flipFunc(int color){
		int new_color = (color & 0xfe);
		if(new_color == color)
			return new_color | 0x1;
		else
			return new_color;
	}
	
	private int shiftflipFunc(int color){
		if(color == 255)
			return 256;
		if(color == 256)
			return 255;
		return (flipFunc(color + 1) - 1);			
	}
	
	//Negative the mask
	private int[] negMask(int[] mask){
		for(int i=0; i< mask.length; i++)
			mask[i] *= (-1);
		return mask;
	}
	
	//Get the RGB channel from the pixel, respectively
	private int getPixelColor(int pixel, int color){
		if(color == BMPRSAnalysis.COLOR_RED)
			return getRed(pixel);
		else if(color == BMPRSAnalysis.COLOR_GREEN)
			return getGreen(pixel);
		else if(color == BMPRSAnalysis.COLOR_BLUE)
			return getBlue(pixel);
		else
			return 0;		
	}
	
	private int getRed(int pixel){
		return ((pixel >> 16) & 0xff);
	}
	
	private int getGreen(int pixel){
		return ((pixel >> 8) & 0xff);
	}
	
	private int getBlue(int pixel){
		return (pixel & 0xff);
	}
	
	@Override
	public String getMethodName() {
		return "[BMP] RS Analysis";
	}

	@Override
	public String getSupportedImageType() {
		return "bmp";
	}

	@Override
	public boolean IsTypesupported(String Type) {
		return Type.equals("bmp");
	}
}
