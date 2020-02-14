package bg.uni.sofia.fmi.vss.client.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlatformTest {

    private static final String TEST_KEY = "test";

    private Platform classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new Platform(TEST_KEY);
    }

    @Test
    public void whenGetKeyIsCalled_thenCorrectValueIsReturned() {
        assertEquals(TEST_KEY, classUnderTest.getKey());
    }
}
