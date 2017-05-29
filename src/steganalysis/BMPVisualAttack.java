package steganalysis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import exceptions.ImageTypeNotSupportedException;

public class BMPVisualAttack implements StegAnalysis{
	private File sourceimage = null;
	private BufferedImage img = null;
	private String name = "Image.gif";
	private int width = 0;
	private int height = 0;
	private int displayChannel = 0;
	
	//the parameter should be File
	public void init(File file) throws ImageTypeNotSupportedException, IOException {
		if (!file.getName().endsWith(".bmp")) {
            throw new ImageTypeNotSupportedException(file.toPath(), this.getSupportedImageTypes());
        }
		try {
	        sourceimage = file;
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
	public JPanel getOptionsPanel() {
		// TODO Auto-generated method stub
		JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1));

        optionsPanel.add(new JLabel("The Channel to be displayed: "));
        // create options panel
        JCheckBox R_Channel = new JCheckBox("R");
        JCheckBox G_Channel = new JCheckBox("G");
        JCheckBox B_Channel = new JCheckBox("B");
        optionsPanel.add(R_Channel);
        optionsPanel.add(G_Channel);
        optionsPanel.add(B_Channel);

        R_Channel.setSelected(true);

        R_Channel.addActionListener(e -> {
        	R_Channel.setSelected(true);
        	G_Channel.setSelected(false);
        	B_Channel.setSelected(false);
        	displayChannel = 0;
        });
        G_Channel.addActionListener(e -> {
        	R_Channel.setSelected(false);
        	G_Channel.setSelected(true);
        	B_Channel.setSelected(false);
        	displayChannel = 1;
        });
        B_Channel.addActionListener(e -> {
        	R_Channel.setSelected(false);
        	G_Channel.setSelected(false);
        	B_Channel.setSelected(true);
        	displayChannel =2;
        });
        return optionsPanel;
	}
	
	//derive 3 images to display the LSB for RGB channels respectively
	@Override
	public JPanel analyze() throws Exception {
		BufferedImage R_LSB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage G_LSB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImage B_LSB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//BufferedImage[] LSB = new BufferedImage[3];
		
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
			System.out.println("Image write error");
			e.printStackTrace();
		}
		
		BufferedImage displayImg;
		if (displayChannel == 0) {
			displayImg = R_LSB;
        } else if (displayChannel == 1) {
        	displayImg = G_LSB;
        } else {
        	displayImg = B_LSB;
        }
		
		JPanel panel = null;
		panel = new JPanel(){
			protected void paintComponent(Graphics g) {
				ImageIcon icon =new ImageIcon(displayImg);
				Dimension size = this.getParent().getSize();
				g.drawImage(icon.getImage(), 0, 0, size.width, size.height, icon.getImageObserver());
				}
		};

        return panel;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "[BMP] Visual Attack";
	}

	@Override
	public String[] getSupportedImageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"bmp"};
	}

	@Override
	public boolean supportsType(String type) {
		// TODO Auto-generated method stub
		return type.equals("bmp");
	}
}
