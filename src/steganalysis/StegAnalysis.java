package steganalysis;

import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import exceptions.ImageTypeNotSupportedException;

public interface StegAnalysis {
	/**
     * Provides the name of the {@link SteganalysisMethod}.
     * @return Name of the method.
     */
    String getName();
    
    /**
     * Provides an array of supported image types.
     * @return array of file endings.
     */
    String[] getSupportedImageTypes();

    /**
     * Check if the method supports a file type
     * @param type file ending
     * @return true, iff the given file type is supported
     */
    boolean supportsType(String type);
    
    /**
     * Initializes a method for the given file.
     * This should check the applicability of the method to the given file.
     * This should also configure any method specific parameters.
     * @param file File which is used for embedding or extraction.
     * @throws ImageTypeNotSupportedException if the given file cannot be handled by the current method
     * @throws IOException if any I/O Error occurs
     */
    void init(File file) throws ImageTypeNotSupportedException, IOException;

    /**
     * Provides a GUI element that enables the user to choose between different options of the method.
     * @return A {@link JPanel} containing all available options
     */
    JPanel getOptionsPanel();

    /**
     * Execute the analysis.
     * @return Provide a JPanel with the result of analysis.
     * @throws Exception if any Error occurs
     */
    JPanel analyze() throws Exception;
}
