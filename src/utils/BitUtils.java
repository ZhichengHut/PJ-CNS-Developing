package utils;

import java.math.BigInteger;

/**
 * Contains helper functions to work with bits.<br>
 *     Created by Dennis Kuhnert on 07.06.2016.
 */
public final class BitUtils {

    /**
     *
     * @param color RGB value of the color
     * @param colorIndex 2 - R<br>1 - G<br>0 - B
     * @param data data to be embedded to the color
     * @param bitCount number of LSBs to embed
     * @return RGB value of the new color
     */
    public static int dataToColor(int color, int colorIndex, long data, int bitCount) {
        if (bitCount > 8 || colorIndex > 2) throw new IllegalArgumentException();
        int dataCleanup = ((0xffffffff << bitCount) << (colorIndex * 8)) | ((1 << colorIndex * 8)-1);
        return (int)((color & dataCleanup) | ((data & ((1 << bitCount)-1)) << colorIndex*8));
    }

    /**
     *
     * @param color RGB value of the color
     * @param colorIndex 2 - R<br>1 - G<br>0 - B
     * @param bitCount number of LSBs to extract
     * @return Byte containing the data
     */
    public static byte dataFromColor(int color, int colorIndex, int bitCount) {
        if (bitCount > 8 || colorIndex > 2) throw new IllegalArgumentException();
        return (byte)((color >> (colorIndex * 8)) & ((1 << bitCount) - 1));
    }

    /**
     * Generates a color to store the given data to one channel and set the indicator bits.
     * @param color RGB value of the color
     * @param indicatorChannel channel that indicates where the data is stored
     * @param data data to be embedded to the color
     * @param bitCount number of LSBs to embed
     * @return RGB value of the new color
     */
    public static int dataToColorWithIndicatorOneChannel(int color, byte indicatorChannel, long data, int bitCount) {
        String dataBinaryString = BigInteger.valueOf(data).toString(2);
        while (dataBinaryString.length() < bitCount) {
            dataBinaryString = "0" + dataBinaryString;
        }
        int value1, value2;
        switch (indicatorChannel) {
            case 0:
                value1 = color >> 16;
                value2 = color >> 8;
                break;
            case 1:
                value1 = color;
                value2 = color >> 16;
                break;
            case 2:
                value1 = color >> 8;
                value2 = color;
                break;
            default:
                throw new IllegalArgumentException("Invalid indicator channel (expected [0,2]): " + indicatorChannel);
        }
        value1 &=  (1 << bitCount) - 1;
        value2 &=  (1 << bitCount) - 1;
        String value1BinaryString = BigInteger.valueOf(value1).toString(2);
        while (value1BinaryString.length() < bitCount) {
            value1BinaryString = "0" + value1BinaryString;
        }
        String value2BinaryString = BigInteger.valueOf(value2).toString(2);
        while (value2BinaryString.length() < bitCount) {
            value2BinaryString = "0" + value2BinaryString;
        }

        // calculate matching lengths
        int matchingBits1 = 0, matchingBits2 = 0;
        for (int i = 1; i <= bitCount; i++) {
            if (!value1BinaryString.regionMatches(0, dataBinaryString, 0, i)) {
                matchingBits1 = i - 1;
                break;
            } else {
                matchingBits1 = i;
            }
        }
        for (int i = 1; i <= bitCount; i++) {
            if (!value2BinaryString.regionMatches(0, dataBinaryString, 0, i)) {
                matchingBits2 = i - 1;
                break;
            } else {
                matchingBits2 = i;
            }
        }

        // choose channel and calculate color
        int resultColor = color;
        if (matchingBits1 > matchingBits2) {
            resultColor = dataToColor(resultColor, indicatorChannel, 0b10, 2);
            resultColor = dataToColor(resultColor, (indicatorChannel + 2) % 3, data, bitCount);
        } else if (matchingBits1 == matchingBits2) {
            // random number 1 or 2
            int randomChannel = (int) (Math.random() * 2) + 1;
            resultColor = dataToColor(resultColor, indicatorChannel, randomChannel, 2);
            resultColor = dataToColor(resultColor, (indicatorChannel + randomChannel) % 3, data, bitCount);
        } else {
            resultColor = dataToColor(resultColor, indicatorChannel, 0b01, 2);
            resultColor = dataToColor(resultColor, (indicatorChannel + 1) % 3, data, bitCount);
        }
        return resultColor;
    }

    /**
     * Generates a color to store the given data to two channels and set the indicator bits.
     * @param color RGB value of the color
     * @param indicatorChannel channel that indicates where the data is stored
     * @param data data to be embedded to the color
     * @param bitCount number of LSBs to embed
     * @return RGB value of the new color
     */
    public static int dataToColorWithIndicatorTwoChannels(int color, byte indicatorChannel, long data, int bitCount) {
        int resultColor = dataToColor(color, indicatorChannel, 0b11, 2);
        resultColor = dataToColor(resultColor, (indicatorChannel + 2) % 3, data >> bitCount, bitCount);
        resultColor = dataToColor(resultColor, (indicatorChannel + 1) % 3, data, bitCount);

        return resultColor;
    }

    /**
     * Private empty constructor that no one creates an instance of this class with private methods only.
     */
    private BitUtils() {}
}
