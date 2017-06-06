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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import utils.libs.GifDecoder;

public class GIFRSAnalysis implements SteganalysisMethod{
	private GifDecoder decoder;
	private int width;
	private int height;
	private int m = 2;
	private int n = 2;
	private boolean overlap = true;
	
	/**
	 * Initialize the image to be read in
	 */
	@Override
	public void initialize(Path file) throws IOException{
		try (InputStream is = Files.newInputStream(file)) {
			decoder = new GifDecoder();
			decoder.read(is);
			width = decoder.getWidth();
			height = decoder.getHeight();
			}
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
			m = Integer.parseInt(M_num.getText());
			n = Integer.parseInt(N_num.getText());
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
		int[] mask0 = new int[m*n];
		int[] mask1 = new int[m*n];
		int[] allmask = new int[m*n];
		for(int i = 0; i < m * n; i++){
			mask0[i] = i % 2;
			mask1[i] = (i + 1) % 2;
			allmask[i] = 1;
		}
		
		double[] result0 = doAnalysis(decoder, overlap, mask0, allmask, false);
		double[] result1 = doAnalysis(decoder, overlap, mask1, allmask, false);
		double[] result2 = doAnalysis(decoder, overlap, mask0, allmask, true);
		double[] result3 = doAnalysis(decoder, overlap, mask1, allmask, true);
		
		//double numGroup = result0[4] + result1[4];

		double r = result0[0] + result1[0];
		double rm = result0[2] + result1[2];
		double r1 = result2[0] + result3[0];
		double rm1 = result2[2] + result3[2];
		double s = result0[1] + result1[1];
		double sm = result0[3] + result1[3];
		double s1 = result2[1] + result3[1];
		double sm1 = result2[3] + result3[3];
		
		/*System.out.println(r+" "+rm+" "+r1+" "+rm1);
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
		
		JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));

        panel.add(new JLabel("Average : " + ml));
        return panel;
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
	 * @param overlap
	 * @param mask
	 * @return: return a double array with size of 5
	 * result[0] = number of Regular
	 * result[1] = number of Singular
	 * result[2] = number of Negative Regular;
	 * result[3] = number of Negative Singular;
	 * result[4] = the total number of groups
	 */
	private double[] doAnalysis(GifDecoder img, boolean overlap, int[] mask,int[] allmask, boolean if_flip){
		int[] block = new int[m * n];
		double discriminationO, discriminationF, discriminationN;
		double numReg = 0, numNegReg = 0, numSin = 0, numNegSin = 0, numUnu = 0, numNegUnu = 0;
		
		int step_x = overlap ? 1 : m;
		int step_y = overlap ? 1 : n;
		
		byte[] bytePixels = img.getPixels();
		
		for(int x = 0; x < width - m; x += step_x){
			for(int y = 0; y < height - n; y += step_y){
				//Operation on each block
				//Get the pixel value and save in block
				int k = 0;
				for(int i = 0; i < n; i++){
					for(int j = 0; j < m; j++){
						block[k] = bytePixels[(width * (y + i) + x + j)] & 0xff;
						k++;
					}
				}
				
				if(if_flip){
					block = flipping(block, allmask);
				}
				
				//Calculate the discrimination on original blocks
				discriminationO = getDiscrimination(block);

				//Calculate the discrimination on flipped blocks
				block = flipping(block, mask);
				discriminationF = getDiscrimination(block);

				//flip the block back
				block = flipping(block, mask);
				
				//negative the mask and calculate the discrimination
				mask = negMask(mask);
				//In order to keep value 256, getDiscrimination can not be reused here
				discriminationN = getNegativeDiscrimination(block, mask);
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
	private double getDiscrimination(int[] block){
		double discrimination = 0;
		int index1, index2;
		for(int i = 0; i < block.length; i += 4){
			index1 = block[i + 0];
			index2 = block[i + 1];
			discrimination += Math.abs(index1 - index2);
			
			index1 = block[i + 1];
			index2 = block[i + 3];
			discrimination += Math.abs(index1 - index2);
			
			index1 = block[i + 3];
			index2 = block[i + 2];
			discrimination += Math.abs(index1 - index2);
			
			index1 = block[i + 2];
			index2 = block[i + 0];
			discrimination += Math.abs(index1 - index2);
		}
		return discrimination;
	}

	/**
	 * Similar to discrimination.
	 * But 256 & 0xff = 0, so we can't reuse the function getDiscrimination
	 * Because we need the index value to be 256 in this case
	 * @param block: 
	 * @param mask: the inversed mask
	 * @return: the value of discrimination
	 */
	private double getNegativeDiscrimination(int[] block, int[] mask){
		double discrimination = 0;
		int index1, index2;
		for(int i = 0; i < block.length; i = i + 4){
			index1 = block[i + 0];
			index2 = block[i + 1];
			if(mask[0 + i] == -1)
				index1 = shiftflipFunc(index1);
			if(mask[1 + i] == -1)
				index2 = shiftflipFunc(index2);
			discrimination += Math.abs(index1 - index2);
			
			index1 = block[i + 1];
			index2 = block[i + 3];
			if(mask[1 + i] == -1)
				index1 = shiftflipFunc(index1);
			if(mask[3 + i] == -1)
				index2 = shiftflipFunc(index2);
			discrimination += Math.abs(index1 - index2);
			
			index1 = block[i + 3];
			index2 = block[i + 2];
			if(mask[3 + i] == -1)
				index1 = shiftflipFunc(index1);
			if(mask[2 + i] == -1)
				index2 = shiftflipFunc(index2);
			discrimination += Math.abs(index1 - index2);
			
			index1 = block[i + 2];
			index2 = block[i + 0];
			if(mask[2 + i] == -1)
				index1 = shiftflipFunc(index1);
			if(mask[0 + i] == -1)
				index2 = shiftflipFunc(index2);
			discrimination += Math.abs(index1 - index2);
		}
		return discrimination;
	}
	
	//flipping the block according to the mask
	private int[] flipping(int[] block, int[] mask){
		int new_index = 0;
		for(int i = 0; i < block.length; i++){
			if(mask[i] == 1){
				new_index = flipFunc(block[i]);
				block[i] = new_index;
			}
			
			else if(mask[i] == -1){
				new_index = shiftflipFunc(block[i]);
				block[i] = new_index;
			}
		}
		return block;
	}
	
	//The flip and shift flip function
	//Defined in "Reliable detection of LSB steganography in color and grayscale images"
	private int flipFunc(int index){
		int new_index = (index & 0xfe);
		if(new_index == index)
			return new_index | 0x1;
		else
			return new_index;
	}
	
	private int shiftflipFunc(int index){
		if(index == 255)
			return 256;
		if(index == 256)
			return 255;
		return (flipFunc(index + 1) - 1);			
	}
	
	//Negative the mask
	private int[] negMask(int[] mask){
		for(int i=0; i< mask.length; i++)
			mask[i] *= (-1);
		return mask;
	}
	
	@Override
	public String getMethodName() {
		return "[GIF] RS Analysis";
	}

	@Override
	public String getSupportedImageType() {
		return "gif";
	}

	@Override
	public boolean IsTypesupported(String Type) {
		return Type.equals("gif");
	}
}
