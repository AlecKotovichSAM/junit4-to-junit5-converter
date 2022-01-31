/**
 * 
 */
package com.aleck.converter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Alec K
 *
 */
@Slf4j
public class Converter implements Consumer<String> {

    private static final Replacer REPLACER = new Replacer();

    private int occurences = 0;

    public String convertFileContent(String sourceFilePath) throws IOException {
        convertFile(sourceFilePath);
        Path path = Paths.get(sourceFilePath);
        return Files.readString(path);
    }

    public void convertFile(String sourceFilePath) {
        Path path = Paths.get(sourceFilePath);

        // https://stackoverflow.com/questions/23466179/java-replace-specific-string-in-text-file
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {

            // Do the replace operation
            List<String> list = stream.map(REPLACER).collect(Collectors.toList());

            if (REPLACER.getCounter().get() > 0) {
                log.info(path.getFileName() + ": Found " + REPLACER.getCounter().get() + " occurences of JUnit 4, converted.");

                occurences += REPLACER.getCounter().get();

                // Write the content back
                Files.write(path, list, StandardCharsets.UTF_8);

                REPLACER.getCounter().set(0);

            } else {
                log.info(path.getFileName() + ": Found no occurences.");
            }

        } catch (IOException e) {
            log.error("IOException for : " + path, e);
            e.printStackTrace();
        }

    }

    public void convertDirectory(String sourceDirPath, boolean recursive) throws IOException {
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

    }

    @Override
    public void accept(String file) {
        convertFile(file);
    }
}
