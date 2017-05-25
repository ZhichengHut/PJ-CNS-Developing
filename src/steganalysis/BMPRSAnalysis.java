/**
 * RS Analysis on BMP image
 * return the possibility of hidding message
 * 
 * created by Zhicheng Mao
 * on 24th. May
 */

package steganalysis;

import java.awt.image.BufferedImage;

public class BMPRSAnalysis {
	private int m = 2;
	private int n = 2;
	public static final int COLOUR_RED = 0;
	public static final int COLOUR_GREEN = 1;
	public static final int COLOUR_BLUE = 2;
	
	/**
	 * initialize the dimension of the mask
	 * @param m: The width of the mask
	 * @param n: The height of the mask
	 */
	public BMPRSAnalysis(int m, int n){
		this.m = m;
		this.n = n;
	}
	
	/**
	 * The function that user can used to get the analysis result
	 * @param img: the image to be analysized
	 * @param color: the color to be analysized
	 * @param overlap: whether the mask is overlapped
	 * @return: the analysis result
	 */
	public double[] Analysis(BufferedImage img, boolean overlap){
		double[] result = new double[3];
		for(int i=0; i < 3; i++){
			result[i] = Analysis_Color(img, i, overlap);
			System.out.println(result[i]);
		}
		
		return result;
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
		int[] mask2 = new int[m*n];
		int[] mask3 = new int[m*n];
		for(int i = 0; i < m * n; i++){
			mask0[i] = i % 2;
			mask1[i] = (i + 1) % 2;
			mask2[i] = 1;
			mask3[i] = -1;
		}
		
		
		double[] result0 = doAnalysis(img, color, overlap, mask0);
		double[] result1 = doAnalysis(img, color, overlap, mask1);
		double[] result2 = doAnalysis(img, color, overlap, mask2);
		double[] result3 = doAnalysis(img, color, overlap, mask3);
		
		
		double numGroup = result0[4] + result1[4];

		double r = result0[0] + result1[0];
		double rm = result0[2] + result1[2];
		double r1 = result2[0];
		double rm1 = result2[2];
		//double r1 = result2[0] + result3[0];
		//double rm1 = result2[2] + result3[2];
		double s = result0[1] + result1[1];
		double sm = result0[3] + result1[3];
		double s1 = result2[1];
		double sm1 = result2[3];
		//double s1 = result2[1] + result3[1];
		//double sm1 = result2[3] + result3[3];
		
		System.out.println(color);
		System.out.println(r+" "+rm+" "+r1+" "+rm1);
		System.out.println(s+" "+sm+" "+s1+" "+sm1);
		
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
		
		double dzero = r - s; // d0 = Rm(p/2) - Sm(p/2)
		double dminuszero = rm - sm; // d-0 = R-m(p/2) - S-m(p/2)
		double done = r1 - s1; // d1 = Rm(1-p/2) - Sm(1-p/2)
		double dminusone = rm1 - sm1; // d-1 = R-m(1-p/2) - S-m(1-p/2)
		
		//get x as the root of the equation 
		//2(d1 + d0)x^2 + (d-0 - d-1 - d1 - 3d0)x + d0 - d-0 = 0
		//x = (-b +or- sqrt(b^2-4ac))/2a
		//where ax^2 + bx + c = 0 and this is the form of the equation
		
		//thanks to a good friend in Dunedin, NZ for helping with maths
		//and to Miroslav Goljan's fantastic Matlab code
		
		double a = 2 * (done + dzero);
		double b = dminuszero - dminusone - done - (3 * dzero);
		double c = dzero - dminuszero;
		
		if(a == 0)
		    //take it as a straight line
		    x = c / b;
		  		
		//take it as a curve
		double discriminant = Math.pow(b,2) - (4 * a * c);
		
		if(discriminant >= 0){
			double rootpos = ((-1 * b) + Math.sqrt(discriminant)) / (2 * a);
			double rootneg = ((-1 * b) - Math.sqrt(discriminant)) / (2 * a);
			
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
	private double[] doAnalysis(BufferedImage img, int color, boolean overlap, int[] mask){
		int width = img.getWidth();
		int height = img.getHeight();
		int[] block = new int[m * n];
		double discriminationO, discriminationF, discriminationN;
		double numReg = 0, numNegReg = 0, numSin = 0, numNegSin = 0, numUnu = 0, numNegUnu = 0;
		
		int step_x = overlap ? 1 : m;
		int step_y = overlap ? 1 : n;
		
		for(int x = 0; x < width - step_x; x += step_x){
			for(int y = 0; y < height - step_y; y += step_y){
				//Operation on each block
				//Get the pixel value and save in block
				int k = 0;
				for(int i = 0; i < m; i++){
					for(int j = 0; j < n; j++){
						block[k] = img.getRGB(x + i, y + j);
						k++;
					}
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
				block = flipping(block, mask);
				discriminationN = getDiscrimination(block, color);
				
				//turn the block and mask back
				block = flipping(block, mask);
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
	double getDiscrimination(int[] block, int color){
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
			new_color = (new_color | 0x01);
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
		if(color == 0)
			return getRed(pixel);
		else if(color == 1)
			return getGreen(pixel);
		else if(color == 2)
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
}
