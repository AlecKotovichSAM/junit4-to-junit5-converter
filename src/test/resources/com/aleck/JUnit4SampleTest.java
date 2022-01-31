package com.aleck;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class JUnit4SampleTest {

    @Autowired
    private TestComponent testComponent;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1() {

    }

    @Test(expected = ArithmeticException.class)
    public void test2() {
        int i = 1 / 0;
    }

    @Test
    @Ignore
    public void test3() {

    }

    @Test(expected = NullPointerException.class)
    public void test4() {
        String s = null;
        s.substring(0, 5);
    }

}

@Component
class TestComponent {
    
}
