package steganalysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GIFVisualAttack implements SteganalysisMethod{
	private File sourceimage = null;
	private BufferedImage img = null;
	private String name = "GIFVisualAttack";
	private int width = 0;
	private int height = 0;
	
	public void initialize(Path file) throws IOException{
		try {
	        sourceimage = new File(file.toString()); 
	        img = ImageIO.read(sourceimage);
	        width = img.getWidth();
	        height = img.getHeight();
	    } catch (IOException e) {
	    	System.out.println("not find");
	    }
	}
	
	public void setName(String n){
		this.name = n;
	}

	@Override
	public JPanel Analysis() throws Exception {
		int black=new Color(255,255,255).getRGB();
		int white=new Color(0,0,0).getRGB();
		
		BufferedImage Gray_LSB = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		
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
		
		//Save in a JPanel
		JPanel panel = null;
		panel = new JPanel(){
			protected void paintComponent(Graphics g) {
				ImageIcon icon =new ImageIcon(Gray_LSB);
				//Change the image size with the Frame
				Dimension size = this.getParent().getSize();
				g.drawImage(icon.getImage(), 0, 0, size.width, size.height, icon.getImageObserver());
			}
		};
		
		return panel;
	}

	@Override
	public String getMethodName() {
		return "[GIF] Visual Attack";
	}

	@Override
	public String getSupportedImageType(){
		return "gif";
	}

	@Override
	public boolean IsTypesupported(String Type){
		return Type.equals("bmp");
	}

	@Override
	public JPanel OptionsofAnalysis() {
		// TODO Auto-generated method stub
		return null;
	}
}
