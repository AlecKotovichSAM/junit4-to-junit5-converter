/**
 * 
 */
package com.aleck;

import java.io.File;
import java.io.IOException;

import com.aleck.converter.Converter;

/**
 * @author Alec K
 *
 */
public class Main {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1 && args.length != 2) {
            System.out.println(
                    "Usage: 1st argument must be file or directory.\n2nd can be 'r' to enable scanning a directory recursively (optional).\nQuitting.");
            return;
        }

        String path = args[0];

        boolean recursive = false;

        if (args.length == 2) {
            if ("r".equals(args[1])) {
                recursive = true;
            }
        }

        if (new File(path).isDirectory()) {
            new Converter().convertDirectory(path, recursive);
        } else {
            new Converter().convertFile(path);
        }
    }

}
