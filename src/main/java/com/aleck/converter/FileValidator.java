/**
 * 
 */
package com.aleck.converter;

import java.io.File;
import java.io.FileNotFoundException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

/**
 * @author okoto
 *
 */
public class FileValidator {

	public static ParseResult<CompilationUnit> validateFile(JavaParser javaParser, String sourceFilePath) throws FileNotFoundException {
		ParseResult<CompilationUnit> parseResult = javaParser.parse(new File(sourceFilePath));
		return parseResult;

	}
}
