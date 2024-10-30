import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Tester {
    public static final int BYTE_LENGTH = 8;

    public static void main(String[] args) throws Exception {
        try (FileReader reader1 = new FileReader("/java programming/huffman_encoding/src/message.txt")) {
            // ENCODING PROCESS

            // Count the frequencies of each unique character in the file
            System.out.print("Counting frequencies... ");
            HuffList freqList = countFreq(reader1);
            System.out.print("Complete\n");

            // Create the encoding tree
            System.out.print("Building encoding tree... ");
            BinaryTree encdTree = buildEncdTree(freqList);
            System.out.print("Complete\n");

            // Create the encoding map
            System.out.print("Building encoding map... ");
            HuffList encdMap = new HuffList();
            encdMap.buildEncdMap(encdTree.getRoot(), 0, 0);
            System.out.print("Complete\n\n");

            // Print divider
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            // Print the encoding map
            System.out.println("Here is the encoding map for each character.");
            System.out.println("|Character|   |Encoding|");
            MapNode n = (MapNode) encdMap.getList();
            while (n != null) {
                char charToPrint = n.getData();
                String binToPrint = Integer.toBinaryString(n.getEncd());
                int numOfAddedZeros = n.getLength() - binToPrint.length();
                // Add zeros according to the encoding length
                for (int i = 0; i < numOfAddedZeros; i++) {
                    binToPrint = "0" + binToPrint;
                }

                // Specify the EOF character
                if (charToPrint == '|') {
                    System.out.print(" EOF           ");
                } else {
                    System.out.printf(" \'%c\'           ", charToPrint);
                }
                System.out.printf("%s           \n", binToPrint);
                n = (MapNode) n.getNext();
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            System.out.println();

            // Open another reader for encoding process
            System.out.println("Encoding the file... ");
            FileReader reader2 = new FileReader("/java programming/huffman_encoding/src/message.txt");
            byte[] encdMsg = huffEncd(reader2, encdMap);

            // Write encdMsg to a binary file
            try (FileOutputStream encdWriter = new FileOutputStream("encdFile.bin")) {
                encdWriter.write(encdMsg);
            }
            System.out.println("File has been sucessfully encoded.\n");

            // Print divider
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            // Print out the encoded binary file
            int numOfBytes = 0;
            ArrayList<String> byteList = new ArrayList<>();
            try (FileInputStream encdFileStream = new FileInputStream("encdFile.bin")) {
                int readByte;
                while ((readByte = encdFileStream.read()) != -1) {
                    String byteString = Integer.toBinaryString(readByte);
                    int numOfAddedZeros = BYTE_LENGTH - byteString.length();
                    // Add zeros according to the encoding length
                    for (int i = 0; i < numOfAddedZeros; i++) {
                        byteString = "0" + byteString;
                    }
                    byteList.add(byteString + " ");
                    numOfBytes++;
                }
                String displayEncd = numOfBytes > 1 ? "Here are the %d bytes in BINARY:\n"
                        : "Here is the %d byte displayed in BINARY:\n";
                System.out.printf(displayEncd, numOfBytes);
                int i = 0;
                for (String b : byteList) {
                    if (++i % 4 == 0) {
                        System.out.println(b);
                    } else {
                        System.out.print(b);
                    }
                }
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

            }

            // DECODING PROCESS
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Decoding the file...\n");

            // Read in the encoded binary file and decode
            try (FileInputStream encdFileStream = new FileInputStream("encdFile.bin")) {
                decode(encdFileStream, encdTree);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Counts the frequencies of each unique character in a file
    public static HuffList countFreq(FileReader reader) {
        // Create linked list
        HuffList freqList = new HuffList();
        try {
            // Initialize first node
            int isChar = reader.read();
            freqList.insertFirst(new HuffNode((char) isChar, 1));

            // Read the rest of the file
            while ((isChar = reader.read()) != -1) {
                // Add to freqList if char is unique
                char readChar = (char) isChar;
                HuffNode foundNode = (HuffNode) freqList.find(readChar);
                if (foundNode != null) { // unique char is found; create new node
                    foundNode.incrementFreq();
                } else { // matching node is found, increment its freq
                    HuffNode unique = new HuffNode(readChar, 1);
                    freqList.insertFirst(unique);
                }
            }

            // Create new 'EOF' node
            HuffNode eofNode = new HuffNode('|', 1);
            freqList.insertFirst(eofNode);

            // Return sorted linked list
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return freqList.sort();
    }

    // Creates the encoding tree from the list of frequencies found in a file
    public static BinaryTree buildEncdTree(HuffList freqList) {
        while (freqList.length() > 1) {
            // Get first and second node, assigning direction
            HuffNode leftNode = (HuffNode) freqList.getList();
            leftNode.setDirection(false);
            HuffNode rightNode = (HuffNode) leftNode.getNext();
            rightNode.setDirection(true);

            // Save the third node
            HuffNode nextNode = (HuffNode) rightNode.getNext();

            // Detach left and right node from LinkedList, have nextNode be the head
            leftNode.setNext(null);
            rightNode.setNext(null);
            freqList.setList(nextNode);

            // Create parent node with combined frequencies
            HuffNode parentNode = new HuffNode(leftNode, rightNode);

            // Have LinkedList point to parent node and parent node point to the next node
            // in the LinkedList
            freqList.insertSorted(parentNode);
        }
        BinaryTree encdTree = new BinaryTree((HuffNode) freqList.getList());
        return encdTree;
    }

    // Encodes the file with respect to the encoding map
    public static byte[] huffEncd(FileReader reader, HuffList map) {
        ArrayList<Byte> byteList = new ArrayList<>();
        byte byteMsg = 0;
        final int MAX_SPACE = BYTE_LENGTH;
        int spaceRemaining = MAX_SPACE;

        // Read in char from reader and get the encoding and length
        try {
            int isChar;
            do {
                // Read the character (if at end of file, set character to '|')
                char charToEncd = ((isChar = reader.read()) != -1) ? (char) isChar : '|';
                MapNode n = (MapNode) map.getList();

                // Loop through list to find the corresponding map node
                while (n != null) {
                    char charFromNode = (char) n.getData();
                    if (charFromNode == charToEncd)
                        break;
                    n = (MapNode) n.getNext();
                }

                // n contains the corresponding mapping node, so put encoding into byteMsg
                if (spaceRemaining < n.getLength()) { // Not enough space, split encoding into separate bytes
                    // Get the left partial encd
                    int rightLength = n.getLength() - spaceRemaining;
                    int leftEncd = n.getEncd() >> rightLength;

                    // Store left partial encd in byteMsg then store msg in byteList
                    byteMsg <<= spaceRemaining;
                    byteMsg |= leftEncd;
                    byteList.add(byteMsg);

                    // Get the right partial encd and store as the new byteMsg
                    int mask = (int) Math.pow(2, rightLength) - 1;
                    byteMsg = (byte) (n.getEncd() & mask);

                    // Reset spaceRemaining
                    spaceRemaining = MAX_SPACE - rightLength;

                } else { // There is enough space for the encd to fit
                    spaceRemaining -= n.getLength();
                    byteMsg <<= n.getLength();
                    byteMsg |= n.getEncd();
                }

                // Check if the char was EOF
                if (charToEncd == '|') {
                    // add last byteMsg to byteList
                    byteMsg <<= spaceRemaining;
                    byteList.add(byteMsg);
                    break;
                }
            } while (isChar != -1);
            // Create last node from

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }

    // Decodes the encoded file with respect to the encoding map
    public static void decode(FileInputStream inputStream, BinaryTree encdTree) {
        // Print out decoding process
        System.out.print("The decoded message is:\n\"");

        byte readByte;
        int BYTE_LENGTH = 8;
        HuffNode nodePtr = (HuffNode) encdTree.getRoot();

        // Read in byte from inputStream
        try {
            endFunction:
            // Read all bytes in the encoded inputStream
            while ((readByte = (byte) inputStream.read()) != -1) {
                // Read byte bit by bit from left to right
                for (int i = BYTE_LENGTH - 1; i >= 0; i--) {
                    int bit = (readByte >> i) % 2;
                    // Traverse tree
                    if (bit == 0) {
                        nodePtr = (HuffNode) nodePtr.getLeft();
                    } else {
                        nodePtr = (HuffNode) nodePtr.getRight();
                    }

                    // Check if nodePtr is a parent node (char instance should be null)
                    Character currChar = nodePtr.getData();
                    if (currChar == null)
                        continue;
                    if (currChar == '|') {
                        break endFunction;
                    }

                    // Character has been found through traversal, print the char out
                    System.out.print(currChar);

                    // Return back to the beginning of the encdTree
                    nodePtr = encdTree.getRoot();
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        // Finish message and print new line
        System.out.println("\"\n\nThe file has been successfully decoded!");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

    }
}
