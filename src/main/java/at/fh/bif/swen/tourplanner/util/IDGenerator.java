package at.fh.bif.swen.tourplanner.util;

/**
 * Temporal IDGenarator for demo (Add and Update)
 * TODO future usage ID will be given by database
 *
 */

public class IDGenerator {
    private static long counter = 1;
    private static long tourLogCounter;


    public static long nextId() {
        return counter++;
    }

    public static long nextTourLogId() {
        return tourLogCounter++;
    }
}
