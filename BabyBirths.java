
/**
 * Write a description of BabyBirths here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;

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
    
    public int getRank (int year, String name, String gender) {
        String path = "data/testing/yob" + Integer.toString(year) + "short.csv";
        FileResource fr = new FileResource(path);
        int rank = 0;
        for (CSVRecord rec : fr.getCSVParser(false)){           
           if(rec.get(1).equals(gender)){
               rank += 1;
               if (rec.get(0).equals(name)) return rank;
           }           
        }
        return -1;
    }
    
    public void testTotalBirth () {
        FileResource fr = new FileResource("data/yob2014.csv");
        totalBirths(fr);
    }
    
    public void testGetRank(){
        int rank = getRank(2012, "Mason", "M");
        System.out.println("the rank of Mason in males is " + rank);
        rank = getRank(2012, "Mason", "F");
        System.out.println("the rank of Mason in females is " + rank);        
    }
    
    public String getName(int year, int rank, String gender){
        String path = "data/testing/yob" + year + "short.csv";
        FileResource fr = new FileResource(path);
        int count = 0;
        for (CSVRecord rec : fr.getCSVParser(false)){
            if (rec.get(1).equals(gender)) count += 1;
            if (count == rank) return rec.get(0);
        }
        return "NO NAME";
    }
    
    public void testGetName(){
        String name = getName(2012,2, "M");
        System.out.println("the name with the second rank in males is " + name);
        name = getName(2012,2, "F");
        System.out.println("the name with the second rank in females is " + name);
    }
    
    public void whatIsNameInYear (String name, int year, int newYear, String gender){
        int rank = getRank (year, name, gender);
        String newName = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + newName + 
                            " if she was born in " + newYear + ".");
    }
    
    public void testWhatIsNameInYear(){
        whatIsNameInYear("Isabella", 2012, 2014, "F");
    }
    
    public int yearOfHighestRank(String name, String gender){
        int rank = 0;
        int yearOfHighest = 0;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            String fileName = f.getName();
            int year = Integer.parseInt(fileName.replaceAll("[\\D]", ""));
            int currentRank = getRank(year, name, gender);
            if (currentRank < rank || rank == 0){
                rank = currentRank;
                yearOfHighest = year;
            }
        }
        if (yearOfHighest == 0) return -1;
        else return yearOfHighest;
    }
    
    public void testYearOfHighestRank(){
        int year = yearOfHighestRank("Mason", "M");
        System.out.println(year);
    }
    
    public double getAverageRank (String name, String gender){
        double averageRank = -1.0;
        int totalRank = 0;
        int count = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()){
            String fileName = f.getName();
            int  year = Integer.parseInt(fileName.replaceAll("[\\D]" , ""));
            int currentRank = getRank(year, name, gender);
            if (currentRank != -1) {
                totalRank += currentRank;
                count += 1;
            }
        }
        if (count != 0) averageRank = new Double(totalRank) / count;
        return averageRank;
    }
    
    public void testGetAverageRank(){
        double averageRank = getAverageRank("Mason", "M");
        System.out.println(String.format ("%.2f", averageRank));        
        averageRank = getAverageRank("Jacob", "M");
        System.out.println(String.format ("%.2f", averageRank));   
        averageRank = getAverageRank("Mason", "F");
        System.out.println(String.format ("%.2f", averageRank));
    }
}
