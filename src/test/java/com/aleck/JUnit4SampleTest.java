package com.aleck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class JUnit4SampleTest {

    @Autowired
    private TestComponent testComponent;

    @BeforeEach
    public void setUp() {
        assertEquals("A", "A");
    }

    @AfterEach
    public void tearDown() {
        assertNotNull(Integer.MAX_VALUE);
    }

    @Test
    public void test1() {
        assertNull(null);
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
        assertTrue(Boolean.TRUE);
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
