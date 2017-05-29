package utils;

/**
 * Builds human readable {@link String}s from different inputs.<br>
 *     Created by Dennis Kuhnert on 31.05.2016.
 */
public final class StringGenerator {

    /**
     * Creates a human readable string with the file size.<br>
     *     Source: http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
     * @param bits size in bits.
     * @param si iff true, use 1000 as calculation factor instead of 1024.
     * @return human readable {@link String} for the file size.
     */
    public static String humanReadableByteCount(long bits, boolean si) {
        long bytes = (long)Math.ceil(bits / 8.0);
        if (bytes < 0)
            bytes = 0;
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Private empty constructor that no one creates an instance of this class with private methods only.
     */
    private StringGenerator() {}
}
