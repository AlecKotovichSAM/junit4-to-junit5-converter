/**
 * 
 */
package com.aleck.converter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Alec K
 *
 */
@Slf4j
public class Converter implements Consumer<String> {

    private static final JavaParser JAVA_PARSER = new JavaParser();

    private static final Replacer REPLACER = new Replacer();

    private int occurences = 0;

    public String convertFileContent(String sourceFilePath) throws IOException {
        convertFile(sourceFilePath);
        Path path = Paths.get(sourceFilePath);
        return Files.readString(path);
    }

    public void convertFile(String sourceFilePath) throws FileNotFoundException {

        ParseResult<CompilationUnit> parseResult = validateFile(sourceFilePath);
        if (!parseResult.isSuccessful()) {
            log.warn("File " + sourceFilePath + " cannot be parsed, maybe it is not a valid Java file...");
            return;
        }

        Path path = Paths.get(sourceFilePath);

        // https://stackoverflow.com/questions/23466179/java-replace-specific-string-in-text-file
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {

            // Do the replace operation
            List<String> lines = stream
                    .map(REPLACER)
                    .flatMap(Pattern.compile(System.lineSeparator())::splitAsStream)
                    .collect(Collectors.toList());

            // Process @Test(expected = ..) blocks
            String output = processTestExpectedExceptionsWithJavaParser(lines);

            if (REPLACER.getCounter().get() > 0) {
                log.info(path.getFileName() + ": Found " + REPLACER.getCounter().get() + " occurences of JUnit 4, converted.");

                occurences += REPLACER.getCounter().get();

                // Write the content back
                Files.writeString(path, output, StandardCharsets.UTF_8);

                REPLACER.getCounter().set(0);

            } else {
                log.info(path.getFileName() + ": Found no occurences.");
            }

        } catch (IOException e) {
            log.error("IOException for : " + path, e);
            e.printStackTrace();
        }

    }

    private ParseResult<CompilationUnit> validateFile(String sourceFilePath) throws FileNotFoundException {
        ParseResult<CompilationUnit> parseResult = JAVA_PARSER.parse(new File(sourceFilePath));
        return parseResult;
    }

    public void convertDirectory(String sourceDirPath, boolean recursive) throws IOException {
    	LocalTime startTime = LocalTime.now();
        Path dirPath = Paths.get(sourceDirPath);
        List<String> files = null;

        if (recursive) {
            try (Stream<Path> stream = Files.walk(dirPath, Integer.MAX_VALUE)) {
                files = stream.filter(Files::isRegularFile).map(String::valueOf).sorted().collect(Collectors.toList());
            }
        } else {

            files = Files.list(dirPath).parallel().filter(Files::isRegularFile).map(String::valueOf).collect(Collectors.toList());
        }

        files.forEach(s -> log.info(s.toString().replace(sourceDirPath, "")));

        // Boom!
        files.forEach(this);

        log.info("Total occurences: " + occurences);
        log.info("Total files analyzed:" + files.size());
        Duration between = Duration.between(startTime, LocalTime.now());
		log.info("Finished in " + DateTimeFormatter.ISO_LOCAL_TIME.format(LocalTime.ofSecondOfDay(between.toSeconds())));

    }

    @Override
    public void accept(String file) {
        try {
            convertFile(file);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private String processTestExpectedExceptionsWithJavaParser(List<String> lines) {
        String output = mergeLines(lines);

        Map<BlockStmt, String> method2ExpectedException = new LinkedHashMap<>();
        List<Position> positionsOfTestExpected = new ArrayList<>();

        ParseResult<CompilationUnit> parseResult = JAVA_PARSER.parse(new ByteArrayInputStream(output.getBytes()));
        if (parseResult.isSuccessful()) {
            Optional<CompilationUnit> result = parseResult.getResult();
            result.get().accept(new VoidVisitorAdapter() {

                @Override
                public void visit(MethodDeclaration methodDeclr, Object arg) {
                    super.visit(methodDeclr, arg);
                    Optional<AnnotationExpr> testAnnotation = methodDeclr.getAnnotationByName("Test");
                    if (testAnnotation.isPresent()) {
                        List<Node> childNodes = testAnnotation.get().getChildNodes();

                        List<Node> expectedTest = childNodes.stream().filter(node -> node.toString().startsWith("expected"))
                                .collect(Collectors.toList());

                        if (expectedTest != null && !expectedTest.isEmpty() && expectedTest.size() == 1) {
                            MemberValuePair expectedPair = (MemberValuePair) expectedTest.get(0);
                            String expectedExceptionClass = ((ClassExpr) expectedPair.getValue()).toString();

                            positionsOfTestExpected.add(methodDeclr.getRange().get().begin);
                            method2ExpectedException.put(methodDeclr.getBody().get(), expectedExceptionClass);
                        }
                    }
                }
            }, null);
        } else {
            log.warn("Error during processing content, skipping");
        }
        
        positionsOfTestExpected.forEach(pos -> lines.set(pos.line - 1, allocateSpaces(pos.column -1) + "@Test"));

        int offset = 0;
        String template = "org.junit.jupiter.api.Assertions.assertThrows(%s, () -> {";

        List<BlockStmt> keys = method2ExpectedException
                .keySet()
                .stream()
                .sorted(new Comparator<BlockStmt>() {
                    @Override
                    public int compare(BlockStmt bst1, BlockStmt bst2) {
                        return bst1.getBegin().get().line - bst2.getBegin().get().line;
                    }
                }).collect(Collectors.toList());
        
        for (BlockStmt blockStmt : keys) {
            String newLine = String.format(template, method2ExpectedException.get(blockStmt));
            log.debug("begin, end: " + blockStmt.getBegin().get().column + ", " + blockStmt.getEnd().get().column);
            Node child1 = blockStmt.getChildNodes().get(0);
            lines.add(blockStmt.getBegin().get().line + offset, allocateSpaces(child1.getBegin().get().column - 3) + newLine);
            lines.add(blockStmt.getEnd().get().line + offset, allocateSpaces(blockStmt.getEnd().get().column + 1) + "});");
            offset += 2;
        }

        REPLACER.getCounter().set(REPLACER.getCounter().get() + method2ExpectedException.size());

        output = mergeLines(lines);

        return output;
    }

    private String mergeLines(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        lines.stream().map(line -> line + System.lineSeparator()).forEach(sb::append);
        return sb.toString();
    }
    
    private static String allocateSpaces(int spacesNumber) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < spacesNumber; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
