package at.fh.bif.swen.tourplanner.test;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class BasicMavenSanityTest {

    @Test
    void testBasicMavenSanity() {
        System.out.println("test Basic Maven Sanity");
        assertTrue(1 + 1 == 2, "Shows that basic Math should work here");
    }

    @Test
    void basicTest(){
        assertTrue(true);
    }
}
