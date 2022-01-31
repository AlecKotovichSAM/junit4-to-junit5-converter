/**
 * 
 */
package com.aleck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.aleck.converter.Converter;

/**
 * @author Alec K
 *
 */
public class ConverterTest {

    @Test
    public void testFileConvert() throws IOException, URISyntaxException {
        Converter converter = new Converter();
        String junit4ConvertedContent = converter.convertFileContent(
                new File(getClass().getClassLoader().getResource("com/aleck/JUnit4Sample.java").toURI()).getAbsolutePath());
        
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("com/aleck/JUnit4Sample.java").getFile());
        int pos = file.getAbsolutePath().indexOf("target");
        assertTrue(pos != -1);
        
        String samplePath = new StringBuilder(file.getAbsolutePath().substring(0, pos))
                .append("src/test/java/com/aleck/JUnit4Sample.java")
                .toString();
                

        String jUnit5SampleContent = Files.readString(Paths.get(samplePath), StandardCharsets.UTF_8);

        assertEquals(junit4ConvertedContent, jUnit5SampleContent);
    }
}
