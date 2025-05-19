package at.fh.bif.swen.tourplanner.util;

/**
 * Temporal IDGenarator for demo (Add and Update)
 * TODO future usage ID will be given by database
 *
 */

public class IDGenerator {
    private static long counter = 1;

    public static synchronized long nextId() {
        return counter++;
    }
}
