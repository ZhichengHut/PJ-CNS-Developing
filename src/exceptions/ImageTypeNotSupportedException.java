package exceptions;

import java.nio.file.Path;
import java.util.Arrays;

/**
 * Exception which indicates that a selected image type is not supported.
 * <p>
 * Created by Dennis Kuhnert on 03.06.16.
 */
public class ImageTypeNotSupportedException extends Exception {

    /**
     * Constructor of {@link ImageTypeNotSupportedException}.
     *
     * @param file      file that caused the exception.
     * @param supported array of supported file types.
     */
    public ImageTypeNotSupportedException(Path file, String[] supported) {
        super("The image type is not supported by this method. Only " +
                Arrays.toString(supported) + " are supported. File: " + file);
    }
}
