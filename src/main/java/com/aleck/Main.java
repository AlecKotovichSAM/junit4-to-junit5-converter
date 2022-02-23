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
	 * Main entry point
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String quit = "\nQuitting.";
		if (args.length != 1 && args.length != 2) {
			System.out.println("Usage: 1st argument MUST be file or directory.\n"
					+ "Optionally supply --recursive to enable scanning a directory recursively." + quit);
			return;
		}

		String path = args[0];

		boolean recursive = false;

		if (args.length == 2) {
			if ("--recursive".equals(args[1])) {
				recursive = true;
			} else {
				System.err.println("Unknown option: " + args[1] + quit);
				return;
			}
		}

		if (new File(path).isDirectory()) {
			new Converter().convertDirectory(path, recursive);
		} else {
			new Converter().convertFile(path);
		}
	}

}
