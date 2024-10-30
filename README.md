## Huffman Encoding with Java

 This program encodes the contents in the 'message.txt' file with an encoding map and writes the encoded file to the file 'encdFile.bin'. This binary file is read by the program, decoded using the same encoding map, then printed out to show the original contents of the file.

Inspired by my original project written in ARM Assembler on a Raspberry Pi, as well as wanting to further my experience with Java, I sought to implement the Huffman encoding algorithm in the Java language. The original project did not write to a binary file, but instead stored the encoded binary file as a linked list during the runtime of the program, unable to see how much space was reduced. Wanting to see the effect of the algorithm in its entirety, I rewrote the program in Java, introducing the aspect of writing to a binary file, to compare the size of the original 'txt' file and the newly encoded 'bin' file. Surely enough, the encoded binary file shortend the contents by almost 50%.
