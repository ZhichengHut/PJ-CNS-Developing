package exceptions;

import utils.StringGenerator;

/**
 * Exception to throw if the data does not fit in the image.<br>
 *     Created by Dennis Kuhnert on 31.05.2016.
 */
public class DataTooLargeException extends Exception {

    private long actual;
    private long maximum;

    /**
     * Constructor of {@link DataTooLargeException}.
     * @param actual size of the data in bits
     * @param maximum size of the maximum bits that can be embedded
     */
    public DataTooLargeException(long actual, long maximum) {
        super("You are trying to embed " + StringGenerator.humanReadableByteCount(actual, false) +
                " to an image, where a maximum of " + StringGenerator.humanReadableByteCount(maximum, false) +
                " can be embedded.");
        this.actual = actual;
        this.maximum = maximum;
    }

    public long getActual() {
        return actual;
    }

    public long getMaximum() {
        return maximum;
    }
}
