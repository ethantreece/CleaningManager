package edu.ncsu.csc316.cleaning.ui;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.cleaning.manager.ReportManager;

/**
 * CleaningManagerUI provides a simple command-line user interface through
 * which the user will interact with the program.
 * 
 * @author Ethan Treece
 *
 */
public class CleaningManagerUI {
    
    /** Manager */
    private static ReportManager manager;
    
    /**
     * Reads user input, where user interacts with the
     * program.
     * @param args arguments
     * @throws FileNotFoundException file not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        Scanner in = new Scanner(System.in);
        
        System.out.print("Room information: ");
        String roomInfo = in.nextLine();
        
        System.out.print("Cleaning events: ");
        String cleaningEvents = in.nextLine();
        
        manager = new ReportManager(roomInfo, cleaningEvents);
        
        System.out.print("1Report (F,C,V,Q): ");
        String userInput = in.nextLine();
        
        do {
            if ("F".equals(userInput)) {
                System.out.print("Number of rooms: ");
                int rooms = in.nextInt();
                in.nextLine();
                System.out.println(manager.getFrequencyReport(rooms));
            } else if ("C".equals(userInput)) {
                System.out.println(manager.getRoomReport());
            } else if ("V".equals(userInput)) {
                System.out.print("Enter the date the vacuum bag was last replaced (MM/DD/YYYY HH:MM:SS): ");
                String date = in.nextLine();
                System.out.println(manager.getVacuumBagReport(date));
            }
            System.out.print("Report (F,C,V,Q): ");
            userInput = in.next();
            in.nextLine();
        } while (!"Q".equals(userInput));
        
        in.close();
    }

}
