package steganalysis;

import java.awt.image.BufferedImage;

public interface VisualAttack {
	
	
	/**
	 * 
	 * @return whether the type of the image is supported
	 */
	boolean supportedType();
	
	/**
	 * save the LSB for the image
	 * return as an array because bmp has 3 channels
	 */
	BufferedImage[] displayLSB(String name);

}
