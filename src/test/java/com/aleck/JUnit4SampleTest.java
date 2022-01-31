package com.aleck;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class JUnit4SampleTest {

    @Autowired
    private TestComponent testComponent;

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void test1() {

    }

    @Test
    public void test2() {
      org.junit.jupiter.api.Assertions.assertThrows(ArithmeticException.class, () -> {
        int i = 1 / 0;
      });
    }

    @Test
    @Disabled
    public void test3() {

    }

    @Test
    public void test4() {
      org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> {
        String s = null;
        s.substring(0, 5);
      });
    }

}

@Component
class TestComponent {
    
}
