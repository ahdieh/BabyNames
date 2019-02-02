
/**
 * Write a description of BabyBirths here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;

public class BabyBirths {
    public void printNames(){
        FileResource fr = new FileResource();
        for(CSVRecord rec : fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100){
                System.out.println("Name " + rec.get(0) + 
                                " Gender " + rec.get(1) +
                                " Num Born " + rec.get(2));
            }
            
        }
    }
    
    public void totalBirths (FileResource fr) {
        int totalBirt = 0;
        int totalGirls = 0;
        int totalBoys = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirt += numBorn;
            if (rec.get(1).equals("M")) totalBoys += numBorn;
            else totalGirls += numBorn;    
        }
        System.out.println("total Birt = " + totalBirt);
        System.out.println("total girls = " + totalGirls);  
        System.out.println("total boys = " + totalBoys);
    }
    
    public void testTotalBirth () {
        FileResource fr = new FileResource("data/yob2014.csv");
        totalBirths(fr);
    }
}
