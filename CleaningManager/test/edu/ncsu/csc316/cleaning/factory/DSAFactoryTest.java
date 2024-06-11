package edu.ncsu.csc316.cleaning.factory;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc316.dsa.data.Identifiable;
import edu.ncsu.csc316.dsa.data.Student;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.list.positional.PositionalList;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.queue.Queue;
import edu.ncsu.csc316.dsa.sorter.Sorter;
import edu.ncsu.csc316.dsa.stack.Stack;

/**
 * Tests the DSAFactory class and its methods
 * @author Ethan Treece
 *
 */
public class DSAFactoryTest {

    @Test
    public void testDSAFactory() {
        Map<String, Integer> map = DSAFactory.getMap();
        assertEquals(0, map.size());
        
        List<String> list = DSAFactory.getIndexedList();
        assertEquals(0, list.size());
        
        PositionalList<String> posList = DSAFactory.getPositionalList();
        assertEquals(0, posList.size());
        
        Sorter<Integer> compSorter = DSAFactory.getComparisonSorter(null);
        
        Sorter<Identifiable> sorter = DSAFactory.getNonComparisonSorter();
        
        Stack<String> stack = DSAFactory.getStack();
        assertEquals(0, stack.size());
        
        Queue<String> queue = DSAFactory.getQueue();
        assertEquals(0, queue.size());
        
        Integer[] arr = {1, 4, 3, 5, 2};
        
        compSorter.sort(arr);
        
        assertEquals(1, (int) arr[0]);
        assertEquals(2, (int) arr[1]);
        assertEquals(3, (int) arr[2]);
        assertEquals(4, (int) arr[3]);
        assertEquals(5, (int) arr[4]);
        
        Student sOne = new Student("OneFirst", "OneLast", 1, 1, 1.0, "oneUnityID");
        Student sTwo = new Student("TwoFirst", "TwoLast", 2, 2, 2.0, "twoUnityID");
        Student sThree = new Student("ThreeFirst", "ThreeLast", 3, 3, 3.0, "threeUnityID");
        Student sFour = new Student("FourFirst", "FourLast", 4, 4, 4.0, "fourUnityID");
        Student sFive = new Student("FiveFirst", "FiveLast", 5, 5, 5.0, "fiveUnityID");
        Student[] original = { sTwo, sOne, sFour, sThree, sFive };
        sorter.sort(original);
        
        assertEquals(sOne, original[0]);
        assertEquals(sTwo, original[1]);
        assertEquals(sThree, original[2]);
        assertEquals(sFour, original[3]);
        assertEquals(sFive, original[4]);
        
    }

}
