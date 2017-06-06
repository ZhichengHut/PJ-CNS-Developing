package steganalysis;

import javax.swing.JPanel;
import java.io.IOException;
import java.nio.file.Path;


public interface SteganalysisMethod {

	String getMethodName();
	
	String getSupportedImageType();
	
	boolean IsTypesupported(String Type);
	
	void initialize(Path file) throws IOException;
	
	JPanel OptionsofAnalysis();
	
	JPanel Analysis() throws Exception;
}
