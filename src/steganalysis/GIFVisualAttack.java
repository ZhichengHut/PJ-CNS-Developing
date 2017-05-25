package steganalysis;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GIFVisualAttack implements VisualAttack{
	private File sourceimage = null;
	private BufferedImage img = null;
	private int width = 0;
	private int height = 0;
	
	public GIFVisualAttack(File file){
	    try {
	        sourceimage = file; 
	        img = ImageIO.read(sourceimage);
	        width = img.getWidth();
	        height = img.getHeight();
	    } catch (IOException e) {
	    	System.out.println("not find");
	    }
	}
	
	public boolean supportedType(){
		String name = sourceimage.getName();
		return name.endsWith(".gif"); 
	}

	public BufferedImage[] displayLSB(String name) {
		int black=new Color(255,255,255).getRGB();
		int white=new Color(0,0,0).getRGB();
		
		BufferedImage Gray_LSB = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		BufferedImage[] LSB = new BufferedImage[1];
		
		//extract the LSB and save the information in the output image
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				
				int gray = img.getRGB(i,j);
				String Gray = Integer.toBinaryString(gray);
				
				if(Gray.charAt(Gray.length()-1) == '0')
					Gray_LSB.setRGB(i, j, black);
				else
					Gray_LSB.setRGB(i, j, white);
			}
		}
		
		/**need to be improved:
		 * what if the name inputed illegal
		 * showing on panel directly, instead of saving as a file
		 */
		File outputfile = new File("Image/"+name);
		try {
			ImageIO.write(Gray_LSB, "gif", outputfile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("here error");
			e.printStackTrace();
		}
		
		LSB[0] = Gray_LSB;
		return LSB;
		
	}
	
	

}
