package com.nwabear.converter;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class ConvertFile {
    public static void main(String[] args) throws Exception {
        File toConv = new File("src/main/java/com/nwabear/converter/file.rle");
        File output = new File("src/main/java/com/nwabear/converter/start.txt");

        if(output.createNewFile()) {
            System.out.println("File Created");
        }

        Scanner scan = new Scanner(toConv);
        FileWriter fw = new FileWriter(output);

        String line = scan.nextLine();
        while(line.charAt(0) == '#') {
            line = scan.nextLine();
        }

        String x = line.substring(4, line.indexOf(','));
        String y = "";
        if(line.contains("rule")) {
            y = line.substring(line.indexOf(',') + 6, line.lastIndexOf(','));
        } else {
            y = line.substring(line.indexOf(',') + 6);
        }

        fw.write(x + '\n');
        fw.write(y + '\n');

        String full = "";

        while(scan.hasNextLine()) {
            line = scan.nextLine();
            full += line;
        }

        int index = 0;
        int curNum = 0;
        boolean clear = true;

        while(full.charAt(index) != '!') {
            char character = full.charAt(index);
            if(clear) {
                curNum = 0;
                clear = false;
            }

            if(Character.isDigit(character)) {
                curNum = (curNum * 10) + Character.getNumericValue(character);
            } else {
                clear = true;
                for(int i = 0; i < curNum || i < 1; i++) {
                    if(character == 'b') {
                        fw.append('.');
                    } else if(character == 'o') {
                        fw.append('O');
                    } else if(character == '$') {
                        fw.append('\n');
                    }
                }
            }
            fw.flush();
            index++;
        }
        fw.close();
    }
}
