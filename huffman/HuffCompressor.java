package com.shpp.p2p.cs.ldebryniuk.assignment15;

import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.BTree;
import com.shpp.p2p.cs.ldebryniuk.assignment15.binaryTree.TreeLeaf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

class HuffCompressor {

    private final int MEGABYTE = 1024 * 1024; // bytes
    private final HashMap<Byte, TreeLeaf> byteToItsTreeLeaf = new HashMap<>();
    /**
     * contains chunk of data that is read from the read channel
     */
    private final ByteBuffer readBuff = ByteBuffer.allocate(MEGABYTE);
    ByteBuffer writeBuff;

    void compressFile(FileChannel inputFChan, FileChannel outputFChan, long inputFileSize) throws IOException {
        countUniqueBytes(inputFChan);

        PriorityQueue<TreeLeaf> orderedByEncodingLengthDesc = new BTree().prioritizeAndBuildTree(byteToItsTreeLeaf);

        createTable(orderedByEncodingLengthDesc, outputFChan);

        compressAndWriteData(inputFChan, outputFChan);
    }

    private void countUniqueBytes(FileChannel inputFChan) throws IOException {
        // read the whole input file by chunks and search for unique bytes
        int bytesInsideReadBuffer;
        while ((bytesInsideReadBuffer = inputFChan.read(readBuff)) != -1) { // read file till end; -1 means end of file
            readBuff.rewind(); // set the position inside the buff to the beginning

            // count unique bytes in the buffer
            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte byteFromTheInputFile = readBuff.get();

                TreeLeaf treeNode = byteToItsTreeLeaf.get(byteFromTheInputFile);
                if (treeNode != null) {
                    treeNode.incrementWeight();
                } else {
                    byteToItsTreeLeaf.put(byteFromTheInputFile, new TreeLeaf(byteFromTheInputFile));
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning
        }
    }

    private void createTable(PriorityQueue<TreeLeaf> prioritizedTreeLeaves, FileChannel outputFChan) throws IOException {
        int longestEncodingLength = prioritizedTreeLeaves.peek().getEncodingLength();

        HashMap<Integer, Integer> encodingLengthToByteCount = new HashMap<>();
        int uniqueBytesCount = prioritizedTreeLeaves.size();
        ArrayList<Byte> table = new ArrayList<>();

        // create table and count bytes with certain encoding length
        for (int i = 0; i < uniqueBytesCount; i++) {
            TreeLeaf byteAsATreeLeaf = prioritizedTreeLeaves.poll();

            // write byte to the table
            table.add(byteAsATreeLeaf.getByteValue());

            int encodingLengthOfCurrentByte = byteAsATreeLeaf.getEncodingLength();

            // write encoding for the byte to the table
            if (encodingLengthOfCurrentByte > 8) {
                int secondLowerByteInInt = byteAsATreeLeaf.getEncodingOfTheByte() & 0xFF00;
                secondLowerByteInInt >>>= 8;
                table.add((byte) secondLowerByteInInt); // get second lower byte from int
                table.add((byte) byteAsATreeLeaf.getEncodingOfTheByte()); // get second lower byte from int
            } else {
                table.add((byte) byteAsATreeLeaf.getEncodingOfTheByte());
            }

            // count bytes with certain encoding length
            int usedBitesForEncoding = byteAsATreeLeaf.getEncodingLength();
            // increment bytes count with usedBitesForEncoding length
            Integer byteCount = encodingLengthToByteCount.getOrDefault(usedBitesForEncoding, 0);
            encodingLengthToByteCount.put(usedBitesForEncoding, byteCount + 1);
        }

        byte[] tableInfo = new byte[encodingLengthToByteCount.size() * 2];
        TreeMap<Integer, Integer> sortedEncodingLengthToByteCount = new TreeMap<>(encodingLengthToByteCount);
        NavigableMap<Integer, Integer> descendingMap = sortedEncodingLengthToByteCount.descendingMap();

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : descendingMap.entrySet()) {
            tableInfo[index] = entry.getValue().byteValue(); // bytesCount with the same encoding length
            tableInfo[index + 1] = entry.getKey().byteValue();//encoding length of bytesCount with the same encoding len
            index += 2;
        }

        // Please read algorithmExlanation.txt in order to understand code below
        int buffCapacity = 2 + tableInfo.length + table.size();
        writeBuff = ByteBuffer.allocate(buffCapacity);

        int firstByte = 0; // later will contain redundant bits count In Last compressed Byte;
        writeBuff.put((byte) firstByte); // first byte will be added at the very end. Now this is a placeholder
        writeBuff.put((byte) tableInfo.length); // second byte
        writeBuff.put(tableInfo);
        // put table to the buffer
        for (Byte byteFromTable : table) {
            writeBuff.put(byteFromTable);
        }
        writeBuff.rewind();

        outputFChan.write(writeBuff);
        writeBuff.rewind();
    }

    private void compressAndWriteData(FileChannel inputFChan, FileChannel outputFChan) throws IOException {
        inputFChan.position(0); // read file from the beginning again
        int totalNumberOfRBuffers = (int) Math.ceil(inputFChan.size() / (double) MEGABYTE);
        int[] compressedDataChunk = new int[MEGABYTE];
        int compressedChunkIndex = 0;

        int fourBytesContainer = 0;
        int freeBitsInContainer = 32;

        int rememberedBits = 0;
        int bitsCountInRemembered = 0;

        int lastContainer = 0;
        int bitsCountInLastContainer = 0;

        // read file by chunks till end of file. each iteration is a new read buffer (chunk of data)
        int bytesInsideReadBuffer;
        for (int bufferSequentialNum = 1; bufferSequentialNum <= totalNumberOfRBuffers; bufferSequentialNum++) {
            bytesInsideReadBuffer = inputFChan.read(readBuff);
            readBuff.rewind(); // set the position inside the buff to the beginning

            // each iteration is a new byte within current read buffer
            for (int i = 0; i < bytesInsideReadBuffer; i++) {
                byte byteFromTheReadFile = readBuff.get();
                TreeLeaf leafForCurrentByte = byteToItsTreeLeaf.get(byteFromTheReadFile);
                int encodingOfTheByte = leafForCurrentByte.getEncodingOfTheByte();
                int usedBitsForEncodingTheByte = leafForCurrentByte.getEncodingLength();

                if (bitsCountInRemembered > 0) {
                    fourBytesContainer |= rememberedBits; // move rememberedBits to fourBytesContainer
                    freeBitsInContainer -= bitsCountInRemembered;
                    bitsCountInRemembered = 0;
                    rememberedBits = 0;
                }

                freeBitsInContainer -= usedBitsForEncodingTheByte;
                if (freeBitsInContainer >= 0) { // true when there is a place to fit the whole encoding
                    // move low order bits to left 00000010 << 5 becomes 01000000
                    encodingOfTheByte <<= freeBitsInContainer;
                    // combine new byte encoded bits with bits that encoded previous byte(s)
                    fourBytesContainer |= encodingOfTheByte; // 01000000 | 00011000 becomes 01011000
                } else { // compressedByte can not fit all the encoding in it
                    bitsCountInRemembered = Math.abs(freeBitsInContainer);
                    rememberedBits = encodingOfTheByte << (32 - bitsCountInRemembered);// remember bits that can not fit to container
                    encodingOfTheByte >>>= bitsCountInRemembered; // remove bits that can not fit to the container
                    fourBytesContainer |= encodingOfTheByte;
                    freeBitsInContainer = 0;
                }

                if (freeBitsInContainer == 0) { // true when twoBytesContainer is full
                    compressedDataChunk[compressedChunkIndex] = fourBytesContainer;
                    compressedChunkIndex++;
                    fourBytesContainer = 0;
                    freeBitsInContainer = 32;
                }
            }
            readBuff.rewind(); // set the position inside the buff to the beginning

            // if this is the last Read Buffer
            if (bufferSequentialNum == totalNumberOfRBuffers) {
                if (bitsCountInRemembered > 0) { // true when there are some remembered bits
                    lastContainer = rememberedBits;
                    bitsCountInLastContainer = bitsCountInRemembered;
                } else if (freeBitsInContainer < 32) { // true if fourBytesContainer is not empty
                    lastContainer = fourBytesContainer;
                    bitsCountInLastContainer = 32 - freeBitsInContainer;
                }
            }

            if (writeBuff.capacity() != (compressedChunkIndex * 4)) {
                writeBuff = ByteBuffer.allocate(compressedChunkIndex * 4);
            }

            for (int i = 0; i < compressedChunkIndex; i++) {
                writeBuff.putInt(compressedDataChunk[i]);
            }
            writeBuff.rewind();

            outputFChan.write(writeBuff);
            writeBuff.rewind();

            compressedChunkIndex = 0;
        }

        // write bytes from last container to the file
        int bytesInsideLastContainer = (int) Math.ceil(bitsCountInLastContainer / 8.0);
        ByteBuffer lastWBuffer = ByteBuffer.allocate(bytesInsideLastContainer);
        int shift = 24;
        for (int i = 0; i < bytesInsideLastContainer; i++) {
            byte byteFromContainer = (byte) (lastContainer >>> shift);
            lastWBuffer.put(byteFromContainer);
            shift -= 8;
        }
        lastWBuffer.rewind();
        outputFChan.write(lastWBuffer);

        // write redundantZeroesInLastCompressedByte to the beginning of the file
        outputFChan.position(0); // set the position of outputFChan to the beginning
        int redundantZeroesInLastCompressedByte = (bytesInsideLastContainer * 8) - bitsCountInLastContainer;
        ByteBuffer oneByteBuffer = ByteBuffer.allocate(1);
        oneByteBuffer.put((byte) redundantZeroesInLastCompressedByte);
        oneByteBuffer.rewind();
        outputFChan.write(oneByteBuffer);
    }

}