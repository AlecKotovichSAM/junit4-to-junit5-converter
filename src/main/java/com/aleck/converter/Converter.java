/**
 * 
 */
package com.aleck.converter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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

    private AtomicInteger occurences = new AtomicInteger(0);

    public String convertFileContent(String sourceFilePath) throws IOException {
        convertFile(sourceFilePath);
        Path path = Paths.get(sourceFilePath);
        return Files.readString(path);
    }

    /**
     * Main conversion method
     * 
     * @param sourceFilePath
     * @throws FileNotFoundException
     */
    public void convertFile(String sourceFilePath) throws FileNotFoundException {
    	new FileConverter(occurences).convert(sourceFilePath);
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


}
