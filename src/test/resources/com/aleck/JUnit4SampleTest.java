package com.aleck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RunWith(MockitoJUnitRunner.class)
public class JUnit4SampleTest {

    @Autowired
    private TestComponent testComponent;

    @Before
    public void setUp() {
        assertEquals("A", "A");
    }

    @After
    public void tearDown() {
        assertNotNull(Integer.MAX_VALUE);
    }

    @Test
    public void test1() {
        assertNull(null);
    }

    @Test(expected = ArithmeticException.class)
    public void test2() {
        int i = 1 / 0;
    }

    @Test
    @Ignore
    public void test3() {
        assertTrue(Boolean.TRUE);
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
