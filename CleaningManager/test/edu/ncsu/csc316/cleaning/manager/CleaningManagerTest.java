package edu.ncsu.csc316.cleaning.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

import org.junit.Test;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;

/**
 * Tests the CleaningManager class and its methods
 * @author Ethan Treece
 *
 */
public class CleaningManagerTest {
    
    /** Cleaning manager */
    private CleaningManager manager;

    @Test
    public void testCleaningManager() {
        try {
            manager = new CleaningManager("input/sample-rooms.csv", "input/sample-logs.csv");
        } catch (FileNotFoundException e) {
            fail("Files not found");
        }
        
        Map<String, List<CleaningLogEntry>> map = manager.getEventsByRoom();
        assertEquals(7, map.size());
        
        int c = manager.getCoverageSince(LocalDateTime.of(2021, 5, 28, 14, 15, 2));
        assertEquals(1538, c);
        
    }

}
