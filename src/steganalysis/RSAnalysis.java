/**
 * RS Analysis algorithm derives from "Reliable detection of LSB steganography
 * in color and grayscale images" by J. Fridrich, M. Goljan and R. Du. 
 * 
 * During coding I have consulted the code b3dk7 publishes on GitHub
 * {@link https://github.com/b3dk7/StegExpose/}
 * And should also thank Bastien Faure and Kathryn Hempstalk for publishing their source code
 */

package steganalysis;

import java.awt.image.BufferedImage;

public interface RSAnalysis {
	double[] Analysis(BufferedImage img, boolean overlap);

}
