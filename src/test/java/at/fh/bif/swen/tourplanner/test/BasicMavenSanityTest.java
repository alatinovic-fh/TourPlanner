package at.fh.bif.swen.tourplanner.test;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
public class BasicMavenSanityTest {

    @Test
    void testBasicMavenSanity() {
        log.debug("test Basic Maven Sanity");
        assertTrue(1 + 1 == 2, "Shows that basic Math should work here");
    }

    @Test
    void basicTest(){
        assertTrue(true);
    }
}
