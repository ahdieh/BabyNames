
/**
 * Write a description of BabyBirths here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;
import java.text.DecimalFormat;
import java.math.RoundingMode;

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
    
    public void countNames(int year, String gender){
        String path = "data/us_babynames_by_year/yob" + year + ".csv";
        FileResource fr = new FileResource(path);
        int count = 0;
        for (CSVRecord rec: fr.getCSVParser(false)){
            if (rec.get(1).equals(gender)) count += 1;
        }
        System.out.println(count);
    }
    
    // print the number of girls names , the number of boys names and the 
    // total names in the file.   
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
        FileResource fr = new FileResource("data/us_babynames_by_year/yob1900.csv");
        totalBirths(fr);
        fr = new FileResource("data/us_babynames_by_year/yob1905.csv");
        totalBirths(fr);
    }
    
    // This method returns the rank of the name in the file for the given 
    // gender, where rank 1 is the name with the largest number of births. 
    // If the name is not in the file, then -1 is returned.
    public int getRank (int year, String name, String gender) {
        String path = "data/yob" + Integer.toString(year) + ".csv";
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
        
    public void testGetRank(){
        int rank = getRank(1960, "Emily", "F");
        System.out.println("the rank of Emily in females is " + rank);
        rank = getRank(1971, "Frank", "M");
        System.out.println("the rank of Frank in males is " + rank);        
    }
    
    
    // This method returns the name of the person in the file at this rank, 
    // for the given gender, where rank 1 is the name with the largest number
    // of births. If the rank does not exist in the file, then “NO NAME” is 
    // returned.
    public String getName(int year, int rank, String gender){
        String path = "data/yob" + year + ".csv";
        FileResource fr = new FileResource(path);
        int count = 0;
        for (CSVRecord rec : fr.getCSVParser(false)){
            if (rec.get(1).equals(gender)) count += 1;
            if (count == rank) return rec.get(0);
        }
        return "NO NAME";
    }
    
    public void testGetName(){
        String name = getName(1982,450, "M");
        System.out.println("the name with the rank of 450 in males is " + name);
        name = getName(1980,350, "F");
        System.out.println("the name with the rank of 350 in females is " + name);
    }
    
    // This method determines what name would have been named if they were 
    // born in a different year, based on the same popularity. That is, you 
    // should determine the rank of name in the year they were born, and 
    // then print the name born in newYear that is at the same rank and same 
    // gender.
    public void whatIsNameInYear (String name, int year, int newYear, String gender){
        int rank = getRank (year, name, gender);
        String newName = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + newName + 
                            " if she was born in " + newYear + ".");
    }
    
    public void testWhatIsNameInYear(){
        whatIsNameInYear("Owen", 1974, 2014, "M");
    }
    
    // This method selects a range of files to process and returns an 
    // integer, the year with the highest rank for the name and gender. If 
    // the name and gender are not in any of the selected files, it should 
    // return -1.
    public int yearOfHighestRank(String name, String gender){
        int rank = 0;
        int yearOfHighest = 0;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            String fileName = f.getName();
            int year = Integer.parseInt(fileName.replaceAll("[\\D]", ""));
            int currentRank = getRank(year, name, gender);
            if ((currentRank != -1) && (currentRank < rank || rank == 0)){
                rank = currentRank;
                yearOfHighest = year;
            }
        }
        if (yearOfHighest == 0) return -1;
        else return yearOfHighest;
    }
    
    public void testYearOfHighestRank(){
        int year = yearOfHighestRank("Mich", "M");
        System.out.println(year);
    }
    
    // This method selects a range of files to process and returns a double 
    // representing the average rank of the name and gender over the 
    // selected files. It should return -1.0 if the name is not ranked in 
    // any of the selected files.
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
        DecimalFormat df2 = new DecimalFormat(".##");
        df2.setRoundingMode(RoundingMode.DOWN);
        double averageRank = getAverageRank("Susan", "F");
        System.out.println(df2.format(averageRank));        
        averageRank = getAverageRank("Robert", "M");
        System.out.println(df2.format(averageRank));  
        //averageRank = getAverageRank("Mason", "F");
        //System.out.println(df2.format(averageRank));
    }
    
    // This method returns an integer, the total number of births of those 
    // names with the same gender and same year who are ranked higher than 
    // name.
    public int getTotalBirthsRankedHigher(int year, String name, String gender){
        String fileName = "yob" + year + ".csv";
        String path = "data/us_babynames_by_year/" + fileName;
        FileResource fr = new FileResource(path);
        int number = 0;
        for(CSVRecord rec : fr.getCSVParser()){
            if (rec.get(0).equals(name) && rec.get(1).equals(gender)) 
                number = Integer.parseInt(rec.get(2));
        }
        int rankedHigher = 0;
        if (number == 0) return rankedHigher = -1;        
        for(CSVRecord rec : fr.getCSVParser(false)){            
           if (rec.get(1).equals(gender)){
               
               int rank = Integer.parseInt(rec.get(2)); 
               if (rank > number) {
                   rankedHigher += rank;
                }
            }
        }
        return rankedHigher;
    }
    
    public void testGetTotalBirthsRankedHigher(){
        int rankedHigher = getTotalBirthsRankedHigher(1990, "Drew", "M");
        System.out.println(rankedHigher);
        rankedHigher = getTotalBirthsRankedHigher(1990, "Emily", "F");
        System.out.println(rankedHigher);
    }
}
