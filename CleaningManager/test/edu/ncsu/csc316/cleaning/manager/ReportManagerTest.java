package edu.ncsu.csc316.cleaning.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

/**
 * Tests the ReportManager class and its methods
 * @author Ethan Treece
 *
 */
public class ReportManagerTest {
    
    /** Report manager */
    private ReportManager manager;

    @Test
    public void testReportManager() {
        try {
            manager = new ReportManager("input/sample-rooms.csv", "input/sample-logs.csv");
        } catch (FileNotFoundException e) {
            fail("Files not found");
        }
        
        assertEquals("Date & time must be in the format: MM/DD/YYYY HH:MM:SS", manager.getVacuumBagReport("string"));
        assertEquals("Vacuum Bag Report (last replaced 05/28/2021 14:15:02) [\n   Bag is due for replacement in 3742 SQ FT\n]", 
                manager.getVacuumBagReport("05/28/2021 14:15:02"));
        assertEquals("Vacuum Bag Report (last replaced 05/01/2021 00:00:00) [\n   Bag is overdue for replacement!\n]", 
                manager.getVacuumBagReport("05/01/2021 00:00:00"));
        
        
        StringBuffer s1 = new StringBuffer();
        s1.append("Room Report [\n"
                + "   Dining Room was cleaned on [\n"
                + "      05/31/2021 09:27:45\n"
                + "      05/23/2021 18:22:11\n"
                + "      05/21/2021 09:16:33\n"
                + "   ]\n"
                + "   Foyer was cleaned on [\n"
                + "      05/01/2021 10:03:11\n"
                + "   ]\n"
                + "   Guest Bathroom was cleaned on [\n"
                + "      05/17/2021 04:37:31\n"
                + "      05/08/2021 07:01:51\n"
                + "   ]\n"
                + "   Guest Bedroom was cleaned on [\n"
                + "      05/23/2021 11:51:19\n"
                + "      05/13/2021 22:20:34\n"
                + "   ]\n"
                + "   Kitchen was cleaned on [\n"
                + "      (never cleaned)\n"
                + "   ]\n"
                + "   Living Room was cleaned on [\n"
                + "      05/30/2021 10:14:41\n"
                + "      05/28/2021 17:22:52\n"
                + "      05/12/2021 18:59:12\n"
                + "      05/11/2021 19:00:12\n"
                + "      05/09/2021 18:44:23\n"
                + "      05/03/2021 17:22:52\n"
                + "   ]\n"
                + "   Office was cleaned on [\n"
                + "      06/01/2021 13:39:01\n"
                + "   ]\n"
                + "]\n");
        assertEquals(s1.toString(), manager.getRoomReport());
        
        StringBuffer s2 = new StringBuffer();
        s2.append("Frequency of Cleanings [\n"
                + "   Living Room has been cleaned 6 times\n"
                + "   Dining Room has been cleaned 3 times\n"
                + "   Guest Bathroom has been cleaned 2 times\n"
                + "   Guest Bedroom has been cleaned 2 times\n"
                + "   Foyer has been cleaned 1 times\n"
                + "   Office has been cleaned 1 times\n"
                + "   Kitchen has been cleaned 0 times\n"
                + "]\n");
        assertEquals(s2.toString(), manager.getFrequencyReport(7));
        
        StringBuffer s3 = new StringBuffer();
        s3.append("Frequency of Cleanings [\n"
                + "   Living Room has been cleaned 6 times\n"
                + "   Dining Room has been cleaned 3 times\n"
                + "   Guest Bathroom has been cleaned 2 times\n"
                + "]\n");
        assertEquals(s3.toString(), manager.getFrequencyReport(3));

        assertEquals("Number of rooms must be greater than 0.", manager.getFrequencyReport(-2));
        

    }

}
