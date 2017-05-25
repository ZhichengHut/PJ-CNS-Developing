package steganalysis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BMPVisualAttack implements VisualAttack{
	private File sourceimage = null;
	private BufferedImage img = null;
	private int width = 0;
	private int height = 0;
	
	//modify later
	//the parameter should be File
	public BMPVisualAttack(File file){
	    try {
	        sourceimage = file;
	        img = ImageIO.read(sourceimage);
	        width = img.getWidth();
	        height = img.getHeight();

	    } catch (IOException e) {
	    	System.out.println("not find");
	    }
	}
	
	//return whether the type of the image is supported
	public boolean supportedType(){
		String name = sourceimage.getName();
		return name.endsWith(".bmp"); 
	}
	
	//derive 3 images to display the LSB for RGB channels respectively and save them on disk
	public BufferedImage[] displayLSB(String name){	
		BufferedImage R_LSB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage G_LSB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage B_LSB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage[] LSB = new BufferedImage[3];
		
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				//extract RGB channels and convert them to Binary String
				int rgb = img.getRGB(i,j);
				int r = (rgb & 0x00ff0000) >> 16;
				int g = (rgb & 0x0000ff00) >> 8;
				int b = (rgb & 0x000000ff);
				String r_BinStr = Integer.toBinaryString(r);
				String g_BinStr = Integer.toBinaryString(g);
				String b_BinStr = Integer.toBinaryString(b);
				
				//extract LSB for 3 channels
				if(r_BinStr.charAt(r_BinStr.length()-1) == '0')
					R_LSB.setRGB(i, j, 0x000000);
				else
					R_LSB.setRGB(i, j, 0xff0000);
				
				if(g_BinStr.charAt(g_BinStr.length()-1) == '0')
					G_LSB.setRGB(i, j, 0x000000);
				else
					G_LSB.setRGB(i, j, 0x00ff00);
				
				if(b_BinStr.charAt(b_BinStr.length()-1) == '0')
					B_LSB.setRGB(i, j, 0x000000);
				else
					B_LSB.setRGB(i, j, 0x0000ff);
			}
		}
		
		/**need to be improved:
		 * what if the name inputed illegal
		 * showing on panel directly, instead of saving as a file
		 */
		File outputfileR = new File("Image/" + name + "R_LSB.bmp");
		File outputfileG = new File("Image/" + name + "G_LSB.bmp");
		File outputfileB = new File("Image/" + name + "B_LSB.bmp");
		try {
			ImageIO.write(R_LSB, "bmp", outputfileR);
			ImageIO.write(G_LSB, "bmp", outputfileG);
			ImageIO.write(B_LSB, "bmp", outputfileB);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("here error");
			e.printStackTrace();
		}
		
		LSB[0] = R_LSB;
		LSB[1] = G_LSB;
		LSB[2] = B_LSB;
		return LSB;
	}
	
	

}
