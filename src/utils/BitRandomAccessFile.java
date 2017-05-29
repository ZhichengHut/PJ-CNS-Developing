package utils;

import java.io.*;

/**
 * This class can be used to read a file bit by bit or even multiple bits at once.
 * Handling the whole input and offset stuff for you.<br>
 *     Created by Dennis Kuhnert on 26.05.16.<br>
 *     <br>
 *     // Feel free to implement some more methods you need.
 */
public class BitRandomAccessFile extends RandomAccessFile {

    private byte filePointerBitOffset;
    private long filePointerOffset;
    private int dataToWrite = 0;

    /**
     * Instances of this class support all methods {@link RandomAccessFile} does.
     * Additionally, there are methods that provide the access to single bits of the file.
     * @param file to open
     * @param mode in which the file will be opened
     * @throws FileNotFoundException if the mode is "r" but the given file object does not denote an existing regular
     *                               file, or if the mode begins with "rw" but the given file object does not denote an
     *                               existing, writable regular file and a new regular file of that name cannot be
     *                               created, or if some other error occurs while opening or creating the file
     */
    public BitRandomAccessFile(File file, String mode) throws FileNotFoundException {
        super(file, mode);
        this.filePointerBitOffset = 0;
    }

    /**
     * Reads a number of bits from this file, starting at the current file pointer, with an internal bit offset,
     * and returns that as an int.
     * If EOF is reached during read, actual data is returned, left padded with zeroes.<br>
     *     <b>Important:</b> Be careful if you combine this with the other methods of {@link RandomAccessFile}.
     * @param len number of bits you want to read
     * @return the next bits of this file
     * @throws IOException if an I/O error occurs.
     */
    public int readBits(int len) throws IOException {
        assert len > 0;
        assert len <= 32;

        int result = 0;
        int toRead = len;
        while (toRead > 0) {
            try {
                result |= _readBits(Math.min(8, toRead)) << (toRead > 8 ? toRead - 8 : 0);
            } catch (EOFException e) {
                // EOF reached, return current result, shifted to right
                return result >> toRead;
            }
            toRead = (toRead > 8 ? toRead - 8 : 0);
        }
        return result;
    }

    /**
     * Reads a number of bits from this file, starting at the current file pointer, with an internal bit offset,
     * and returns that as an int.<br>
     *     <b>Important:</b> Be careful if you combine this with the other methods of {@link RandomAccessFile}.
     * @param len number of bits you want to read
     * @return the next bits of this file
     * @throws IOException if an I/O error occurs.
     * @throws EOFException if the end of the file is reached
     */
    private int _readBits(int len) throws IOException {
        assert len > 0;
        assert len <= 8;
        int result;

        super.seek(filePointerOffset);
        long result1 = (long)readUnsignedByte();
        if (filePointerBitOffset + len > 8) {
            int bitsFrom1 = 8 - filePointerBitOffset;
            long from1 = result1 & ((1 << bitsFrom1) - 1);
            try {
                long result2 = readUnsignedByte();
                result = (int)(from1 << len - bitsFrom1) | firstBitsFromByte(result2, len - bitsFrom1);
            } catch (EOFException e) {
                // EOF reached, read result1 only
                result = (int) result1;
            }
        } else {
            result = firstBitsFromByte((result1 << filePointerBitOffset) & ((1 << 8) - 1), len);
        }

        if (filePointerBitOffset + len >= 8) {
            filePointerOffset++;
        }
        filePointerBitOffset = (byte) ((filePointerBitOffset + len) % 8);
        return result;
    }

    /**
     * Writes bits to the file at the current position and bitOffset.<br>
     *     Data will only be written to the file system, if a whole byte can be written.
     *     Call {@link #flush()} to write bits that are in the buffer.
     * @param data contains data to write, starting at the LSB
     * @param len number of bits to write
     * @throws IOException if an I/O error occurs.
     */
    public void writeBits(int data, int len) throws IOException {
        int toWrite = len;
        while (toWrite > 0) {
            _writeBits(data >> (toWrite > 8 ? toWrite - 8 : 0), Math.min(8, toWrite));
            toWrite = (toWrite > 8 ? toWrite - 8 : 0);

        }
    }
    /**
     * Writes bits to the file at the current position and bitOffset.<br>
     *     Data will only be written to the file system, if a whole byte can be written.
     *     Call {@link #flush()} to write bits that are in the buffer.
     * @param data contains data to write, starting at the LSB
     * @param len number of bits to write
     * @throws IOException if an I/O error occurs.
     */
    private void _writeBits(int data, int len) throws IOException {
        assert len > 0;
        assert len <= 8;

        if (filePointerBitOffset + len < 8) {
            dataToWrite |= (data & ((1 << len) - 1)) << (8 - filePointerBitOffset - len);
        } else {
            dataToWrite |= (data & ((1 << len) - 1)) >> (len - 8 + filePointerBitOffset);
            writeByte(dataToWrite);
            int bitsLeft = len - (8 - filePointerBitOffset);
            dataToWrite = (data & ((1 << bitsLeft) - 1)) << (8 - bitsLeft);
        }
        filePointerBitOffset = (byte) ((filePointerBitOffset + len) % 8);
    }

    /**
     * Writes data (bits) from the buffer to the file, filling the byte with zeroes at the end.
     * @throws IOException if an I/O error occurs.
     */
    public void flush() throws IOException {
        if (filePointerBitOffset > 0) {
            writeByte(dataToWrite);
            dataToWrite = 0;
            filePointerBitOffset = 0;
        }
    }

    /**
     * Setter of the bit offset, where the reader starts to fead the current byte.
     * @param offset between 0 and 7
     */
    public void setBitOffset(byte offset) {
        assert offset >= 0;
        assert offset < 8;
        this.filePointerBitOffset = offset;
    }

    @Override
    public void seek(long pos) throws IOException {
        super.seek(pos);
        this.filePointerOffset = pos;
        this.filePointerBitOffset = 0;
    }

    /**
     * Returns bits 8 to 8-number of data. data is interpreted as byte, so a number of MSB bits is returned.
     * @param data treated as {@link byte}
     * @param number of bits you want to recieve
     * @return MSBs of the input data
     */
    private int firstBitsFromByte(long data, int number) {
        int filter = (1 << 8) - 1;
        return (int)((data & filter) >> (8-number));
    }
}
