package com.shpp.p2p.cs.ldebryniuk.assignment14;

public class Archiver {

    protected final int megaByte = 1024 * 1024; // bytes
    protected final int bytesForSavingTableSize = 4;
    protected final int bytesForSavingUncompressedData = 8;
    protected final int dataForEncodingOffset = bytesForSavingTableSize + bytesForSavingUncompressedData;

    protected int findEncodingLen(int uniqueBytesSize) {
//        int[] powers = {1, 2, 4, 8, 16, 32, 64, 127};
        int exponent = 0;
        int byteSize = 8;
        for (int i = 0; i <= byteSize; i++) {
            if (uniqueBytesSize <= Math.pow(2, exponent)) {
                return exponent;
            }

            exponent++;
        }

        return -1;
    }

}
