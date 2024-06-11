package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.cleaning.data.RoomRecord;
import edu.ncsu.csc316.cleaning.factory.DSAFactory;
import edu.ncsu.csc316.cleaning.io.InputReader;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.sorter.Sorter;

/**
 * CleaningManager provides behaviors for retrieving information about cleaning
 * history, including lists of cleaning events for each room and the number of
 * square feet cleaned since a provided date.
 * 
 * @author Dr. King
 * @author Ethan Treece
 *
 */
public class CleaningManager {

    
    /** List of cleaning log */
    private List<CleaningLogEntry> cleaningLogs;
    
    /** List of room records */
    private List<RoomRecord> roomRecords;

    /**
     * Constructs a new CleaningManager for processing cleaning history information
     * from the provided file with room information and the provided file with
     * cleaning log event information.
     * 
     * @param pathToRoomFile the path to the file that contains room information
     * @param pathToLogFile  the path to the file that contains cleaning event log
     *                       information
     * @throws FileNotFoundException if either the room file or the cleaning event
     *                               log file cannot be read
     */
    public CleaningManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
        List<RoomRecord> rooms = InputReader.readRoomFile(pathToRoomFile);
        List<CleaningLogEntry> cleanings = InputReader.readLogFile(pathToLogFile);
        
        CleaningLogComparator logComparator = new CleaningLogComparator();
        RoomRecordComparator roomComparator = new RoomRecordComparator();
        Sorter<RoomRecord> roomSorter = DSAFactory.getComparisonSorter(roomComparator);
        Sorter<CleaningLogEntry> cleaningSorter = DSAFactory.getComparisonSorter(logComparator);
        
        RoomRecord[] roomsArray = new RoomRecord[rooms.size()];
        CleaningLogEntry[] cleaningsArray = new CleaningLogEntry[cleanings.size()];
        
        int roomCounter = 0;
        for (RoomRecord r : rooms) {
            roomsArray[roomCounter++] = r;
        }
        int logCounter = 0;
        for (CleaningLogEntry c : cleanings) {
            cleaningsArray[logCounter++] = c;
        }
        
        roomSorter.sort(roomsArray);
        cleaningSorter.sort(cleaningsArray);
        
        roomRecords = DSAFactory.getIndexedList();
        for (RoomRecord r : roomsArray) {
            roomRecords.addFirst(r);
        }
        cleaningLogs = DSAFactory.getIndexedList();
        for (CleaningLogEntry c : cleaningsArray) {
            cleaningLogs.addFirst(c);
        }
    }

    /**
     * Returns a map of cleaning event logs for each room. In the returned map, the
     * key of each entry is represented by the room ID. The value of each entry is
     * represented by a list of cleaning event log entries.
     * 
     * If there is no room information or there are no cleaning log events, returns
     * null.
     * 
     * @return a map of cleaning event logs for each room
     */
    public Map<String, List<CleaningLogEntry>> getEventsByRoom() {
        // Declare an empty map that will hold an entry for each room
        Map<String, List<CleaningLogEntry>> m = DSAFactory.getMap();
        // Iterate through the rooms, adding each one to the map along with an empty
        // list which will contain all the relevant cleaning log events
        for (RoomRecord r : roomRecords) {
            List<CleaningLogEntry> log = DSAFactory.getIndexedList();
            m.put(r.getRoomID(), log);
        }
        for (CleaningLogEntry c : cleaningLogs) {
            String id = c.getRoomID();
            m.get(id).addFirst(c);
        }
        return m;
    }

    /**
     * Returns the square footage (as a whole number) cleaned since a provided date
     * and time. Partial square feet are rounded down to the nearest whole number.
     * 
     * @param time the time since which to calculate square footage cleaned
     * @return the square footage cleaned since the provided date and time
     */
    public int getCoverageSince(LocalDateTime time) {
        // Get number of cleaning event log entries
        // Get map of rooms by roomID
        Map<String, RoomRecord> r = getRoomsByID();
        // Initialize total square footage as 0
        int total = 0;
        // Iterate through each cleaning log entry
        for (CleaningLogEntry c : cleaningLogs) {
            // Check that the cleaning entry occurred after the last time
            // the vacuum bag was replaced
            if (c.getTimestamp().isAfter(time)) {
                RoomRecord room = r.get(c.getRoomID());
                int length = room.getLength();
                int width = room.getWidth();
                double percent = c.getPercentCompleted() / 100.0;
                total = (int) (total + length * width * percent);
            }
        }
        // Return the total cast to an int as to round down
        return (int) total;
    }
    
    /**
     * Creates a map of room records by room id
     * @return map of room records by room id
     */
    private Map<String, RoomRecord> getRoomsByID() {
        Map<String, RoomRecord> m = DSAFactory.getMap();
        for (RoomRecord r : roomRecords) {
            m.put(r.getRoomID(), r);
        }
        return m;
    }
    
    /**
     * Comparator for comparing Entry objects of Strings as keys and Lists of ClenaingLogEnties as values
     * Sorted by size of list descending, then by name ascsending
     * @author Ethan Treece 
     *
     */
    public class CleaningLogComparator implements Comparator<CleaningLogEntry> {
        
        @Override
        public int compare(CleaningLogEntry o1, CleaningLogEntry o2) {
            if (o1.getRoomID().equals(o2.getRoomID())) {
                if (o1.getTimestamp().isAfter(o2.getTimestamp())) {
                    return -1;
                } else if (o1.getTimestamp().isBefore(o2.getTimestamp())) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return o1.getRoomID().compareTo(o2.getRoomID());
            }
        }

    }
    
    /**
     * Comparator for comparing Entry objects of Strings as keys and Lists of ClenaingLogEnties as values
     * Sorted by name ascsending
     * @author Ethan Treece 
     *
     */
    public class RoomRecordComparator implements Comparator<RoomRecord> {

        @Override
        public int compare(RoomRecord o1, RoomRecord o2) {
            return o1.getRoomID().compareTo(o2.getRoomID());
        }

    }
}