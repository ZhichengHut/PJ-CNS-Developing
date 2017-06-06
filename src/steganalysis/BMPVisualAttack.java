package steganalysis;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class BMPVisualAttack implements SteganalysisMethod{
	private File sourceimage = null;
	private BufferedImage img = null;
	private String name = "BMPVisualAttack";
	private int width = 0;
	private int height = 0;
	private int displayChannel = 0;
	
	private JPanel panel;
	private BufferedImage R_LSB;
	private BufferedImage G_LSB;
	private BufferedImage B_LSB;
	private boolean channelFlag = false;	
	
	//the parameter should be File
	public void initialize(Path file) throws IOException{
		try {
			sourceimage = new File(file.toString());
	        img = ImageIO.read(sourceimage);
	        width = img.getWidth();
	        height = img.getHeight();
	        panel = new JPanel();
	        panel.setBounds(20, 20, 400, 300);
	        R_LSB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			G_LSB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			B_LSB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        
	    } catch (IOException e) {
	    	System.out.println("not find");
	    }
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	@Override
	public JPanel OptionsofAnalysis() {
		// TODO Auto-generated method stub
		JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1));

        optionsPanel.add(new JLabel("The Channel to be displayed: "));
        
        //Create buttons
        JRadioButton R_Channel = new JRadioButton("R-LSB");
        JRadioButton G_Channel = new JRadioButton("G-LSB");  
        JRadioButton B_Channel = new JRadioButton("B-LSB");  
        R_Channel.setSelected(true);
        
        //Create ButtonGroup
        ButtonGroup buttonGroup = new ButtonGroup();  
        buttonGroup.add(R_Channel);  
        buttonGroup.add(G_Channel);  
        buttonGroup.add(B_Channel);   
        
        //Add button to panel
        optionsPanel.add(R_Channel);  
        optionsPanel.add(G_Channel);  
        optionsPanel.add(B_Channel); 

        R_Channel.addActionListener(e -> {
        	R_Channel.setSelected(true);
        	displayChannel = 0;
        	//The panel will be repaint only after the analysis
        	if(channelFlag){
        		JLabel label = new JLabel(new ImageIcon(R_LSB));
        		panel.removeAll();
        		panel.add(label);
        		panel.revalidate();
        		panel.repaint();
        	}
        });
        G_Channel.addActionListener(e -> {
        	G_Channel.setSelected(true);
        	displayChannel = 1;
        	if(channelFlag){
        		JLabel label = new JLabel(new ImageIcon(G_LSB));
        		panel.removeAll();
        		panel.add(label);
        		panel.revalidate();
        		panel.repaint();
        	}
        });
        B_Channel.addActionListener(e -> {
        	B_Channel.setSelected(true);
        	displayChannel = 2;
        	if(channelFlag){
        		JLabel label = new JLabel(new ImageIcon(B_LSB));
        		panel.removeAll();
        		panel.add(label);
        		panel.revalidate();
        		panel.repaint();
        	}
        });
        return optionsPanel;
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
	//derive 3 images to display the LSB for RGB channels respectively
	@Override
	public JPanel Analysis() throws Exception{
		this.channelFlag = true;
		
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
		
		//Put the image into the JPanel
		
		BufferedImage displayImg;
		if (displayChannel == 0) {
			displayImg = R_LSB;
        } else if (displayChannel == 1) {
        	displayImg = G_LSB;
        } else {
        	displayImg = B_LSB;
        }
		
		//JPanel panel = null;
		JLabel label = new JLabel(new ImageIcon(displayImg));
		panel.add(label);
		//panel.revalidate();
		panel.repaint();
	    
        return panel;
	}

	@Override
	public String getMethodName() {
		// TODO Auto-generated method stub
		return "[BMP] Visual Attack";
	}

	@Override
	public String getSupportedImageType() {
		// TODO Auto-generated method stub
		return "bmp";
	}

	@Override
	public boolean IsTypesupported(String Type) {
		// TODO Auto-generated method stub
		return Type.equals("bmp");
	}
}
