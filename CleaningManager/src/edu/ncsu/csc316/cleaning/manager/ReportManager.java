package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Iterator;

import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.cleaning.factory.DSAFactory;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.sorter.Sorter;


/**
 * ReportManager handles behaviors associated with generating String reports for
 * the user interface, including (1) vacuum bag reports, (2) frequency reports,
 * and (3) room reports.
 * 
 * @author Dr. King
 * @author Ethan Treece
 *
 */
public class ReportManager {

    /** Date format */
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    
    /** Manager */
    private CleaningManager manager;

    /**
     * Constructs a new ReportManager for building reports of cleaning history
     * information from the provided file with room information and the provided
     * file with cleaning log event information.
     * 
     * @param pathToRoomFile the path to the file that contains room information
     * @param pathToLogFile  the path to the file that contains cleaning event log
     *                       information
     * @throws FileNotFoundException if either the room file or the cleaning event
     *                               log file cannot be read
     */
    public ReportManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
        manager = new CleaningManager(pathToRoomFile, pathToLogFile);
    }

    /**
     * Returns a report that indicates how many square feet remain to be cleaned
     * until a vacuum bag change is recommended.
     * 
     * @param timestamp the date and time of the previous vacuum bag change
     * @return a report that indicates how many more square feet can be cleaned
     *         until a vacuum bag change is recommended
     */
    public String getVacuumBagReport(String timestamp) {
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(timestamp, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            return "Date & time must be in the format: MM/DD/YYYY HH:MM:SS";
        }
        StringBuffer buf = new StringBuffer();
        buf.append("Vacuum Bag Report (last replaced ");
        buf.append(timestamp);
        buf.append(") [\n");
        
        int coverage = manager.getCoverageSince(dateTime);
        
        if (coverage >= 5280) {
            buf.append("   Bag is overdue for replacement!\n]");
        } else {
            buf.append("   Bag is due for replacement in ");
            buf.append(5280 - coverage);
            buf.append(" SQ FT\n]");
        }
        
        return buf.toString();
    }

    /**
     * Returns a report of the top X rooms cleaned, sorted from most frequent to
     * least frequent.
     * 
     * @param number the number of rooms to include in the report
     * @return a report of the top X rooms cleaned
     */
    public String getFrequencyReport(int number) {
        if (number <= 0) {
            return "Number of rooms must be greater than 0.";
        }

        Map<String, List<CleaningLogEntry>> events = manager.getEventsByRoom();
        
        if (events.isEmpty()) {
            return "No rooms have been cleaned.";
        }

        Entry<String, List<CleaningLogEntry>>[] eventsArray = mapToArrayOfEntries(events);
        RoomFrequencyComparator comparator = new RoomFrequencyComparator();
        Sorter<Entry<String, List<CleaningLogEntry>>> sorter = DSAFactory.getComparisonSorter(comparator);
        sorter.sort(eventsArray);
        
        StringBuffer buf = new StringBuffer();
        buf.append("Frequency of Cleanings [\n");
        
        for (int i = 0; i < number; i++) {
            if (i >= eventsArray.length) {
                break;
            }
            String roomName = eventsArray[i].getKey();
            int times = eventsArray[i].getValue().size();
            buf.append("   ");
            buf.append(roomName);
            buf.append(" has been cleaned ");
            buf.append(times);
            buf.append(" times\n");
        }
        sorter.sort(eventsArray);
        buf.append("]\n");
        return buf.toString();
    }

    /**
     * Returns a report that includes a list of cleaning log events associated with
     * each room. The report lists rooms in alphabetical (ascending) order, and
     * cleaning log entries for each room as sorted in descending chronological
     * order (most recent to oldest).
     * 
     * @return a report of cleaning log events for each room
     */
    public String getRoomReport() {
        Map<String, List<CleaningLogEntry>> events = manager.getEventsByRoom();
        
        if (events.isEmpty()) {
            return "No rooms have been cleaned.";
        }
        Iterator<List<CleaningLogEntry>> it = events.values().iterator();
        boolean cleaned = false;
        while (it.hasNext()) {
            if (it.next().size() != 0) {
                cleaned = true;
                break;
            }
        }
        if (!cleaned) {
            return "No rooms have been cleaned.";
        }
        
        StringBuffer buf = new StringBuffer();
        buf.append("Room Report [\n");
        
        Iterator<List<CleaningLogEntry>> logs = events.values().iterator();
        Iterator<String> rooms = events.iterator();
        while (logs.hasNext()) {
            List<CleaningLogEntry> list = logs.next();
            buf.append("   ");
            buf.append(rooms.next());
            buf.append(" was cleaned on [\n");
            if (list.size() == 0) {
                buf.append("      (never cleaned)\n");
            } else {
                for (CleaningLogEntry c : list) {
                    buf.append("      ");
                    buf.append(c.getTimestamp().format(DATE_TIME_FORMAT));
                    buf.append("\n");
                }
            }
            buf.append("   ]\n");
        }
        buf.append("]\n");
        return buf.toString();
    }
    
    
    /**
     * Takes the map of Strings as keys and Lists of ClenaingLogEnties as values, and returns
     * an array of entries of that map
     * @param m map
     * @return array of entries in given map
     */
    private Entry<String, List<CleaningLogEntry>>[] mapToArrayOfEntries(Map<String, List<CleaningLogEntry>> m) {
        @SuppressWarnings("unchecked")
        Entry<String, List<CleaningLogEntry>>[] sorted = (Entry<String, List<CleaningLogEntry>>[]) new Entry[m.size()];
        Iterator<Entry<String, List<CleaningLogEntry>>> it = m.entrySet().iterator();
        for (int i = 0; i < m.size(); i++) {
            sorted[i] = it.next();
        }
        return sorted;
    }
    
    /**
     * Comparator for comparing Entry objects of Strings as keys and Lists of ClenaingLogEnties as values
     * Sorted by size of list descending, then by name ascsending
     * @author Ethan Treece 
     *
     */
    public class RoomFrequencyComparator implements Comparator<Entry<String, List<CleaningLogEntry>>> {

        @Override
        public int compare(Entry<String, List<CleaningLogEntry>> o1, Entry<String, List<CleaningLogEntry>> o2) {
            if (o1.getValue().size() > o2.getValue().size()) {
                return -1;
            } else if (o1.getValue().size() < o2.getValue().size()) {
                return 1;
            } else {
                return o1.getKey().compareTo(o2.getKey());
            }
            
        }

    }
    
}