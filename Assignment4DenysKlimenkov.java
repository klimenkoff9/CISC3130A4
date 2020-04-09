// Class: CISC 3130
// Section: MY9
// EmplId: 23836677
// Name: Denys Klimenkov


import java.io.File;
import java.io.IOException;
import java.util.*;

public class Assignment4 {
    //Hashmap for storing read in data to use throughout program
    public static HashMap<String, int[][]> map = new HashMap<>();
    //String yearsplit to hold the split up line so i dont have to redeclare
    private static String yearsplit[];
    //String year to hold the year throughout program
    private static String year;

    public static void main(String args[]) throws IOException {
        //Method for reading in data
        readInData();
        //Method to print out how many times each genre appeared
        totalAppearances();
        //Method to print out amount of movies released in the past 5 years and movies released before that can change 5 to any year
        appearedInLast(5);
        //Method to print out amount of times each genre appeared for each year
        appearedPerYearPerGenre();
        //Method to print out number of movies released each year
        releasePerYear();
    }

    //Reads in data
    public static void readInData() throws IOException {
        //Opens file to read
        File records = new File("Data/movies.csv");
        Scanner file = new Scanner(records);
        file.nextLine();
        //Does this till end of file
        while (file.hasNextLine()) {
            //Goes to next line
            String line = file.nextLine();
            //Splits the line at , because of csv
            String[] splitline = line.split(",");
            //Grabs the genres from the splitline
            String genres = splitline[splitline.length - 1];
            //Separates the genres by splitting | because movies have multiple genres
            String[] listGenres = genres.split("\\|");

            //Gets the year the movie was released had to check if it has " because some lines had quotations in the movie title
            if (!line.contains("\"")) {
                //Splits at ),
                yearsplit = line.split("\\)+,");
                //Gets the year
                year = yearsplit[0].charAt(yearsplit[0].length() - 4) + String.valueOf(yearsplit[0].charAt(yearsplit[0].length() - 3)) + yearsplit[0].charAt(yearsplit[0].length() - 2) + yearsplit[0].charAt(yearsplit[0].length() - 1);
            } else {
                //Splits at )",
                yearsplit = line.split("\\)" + "\"" + "+,");
                //Grabs the year
                year = yearsplit[0].charAt(yearsplit[0].length() - 4) + String.valueOf(yearsplit[0].charAt(yearsplit[0].length() - 3)) + yearsplit[0].charAt(yearsplit[0].length() - 2) + yearsplit[0].charAt(yearsplit[0].length() - 1);

            }

            //Does this for the amount of genres in current line
            for (int i = 0; i < listGenres.length; i++) {
                //Only goes in if genre is specified, some movies had no genre listed
                if (!listGenres[i].contains("(no genres listed)")) {
                    //Checks if genre is already in hashmap goes to else if it is
                    if (!map.containsKey(listGenres[i])) {
                        //Puts year released and genre into hashmap
                        int[][] inFo = new int[500][2];
                        //Holds year
                        inFo[0][0] = Integer.valueOf(year);
                        //Holds 1 because its first appearance
                        inFo[0][1] = 1;
                        map.put(listGenres[i], inFo);
                    } else {
                        //Does this if genre is not already in hashmap
                        if (map.containsKey(listGenres[i])) {
                            //Used to hold the years of the genre and amount of times it appeared so far
                            int[][] yearCheck = map.get(listGenres[i]);
                            //Loops until the year for the specific genre is found
                            for (int j = 0; j < yearCheck.length; j++) {
                                //checks if the current year is equal to the current year of the line if not goes to else
                                if (yearCheck[j][0] == Integer.valueOf(year)) {
                                    //Adds 1 to the current appearance
                                    yearCheck[j][1] = yearCheck[j][1] + 1;
                                    //Changes the amount of times the genre appeared in the specific year
                                    map.put(listGenres[i], yearCheck);
                                    //Breaks out of loop because we added the new amount
                                    break;
                                } else {
                                    //Does this if loops reaches the end of yearCheck meaning the year hasnt appeared for the genre yet so we add it
                                    if (j == yearCheck.length - 1) {
                                        yearCheck = map.get(listGenres[i]);
                                        //Goes through yearCheck to find a empty slot
                                        for (int k = 0; k < yearCheck.length; k++) {
                                            //If empty slot is found we add new year to hashmap
                                            if (yearCheck[k][0] == 0) {
                                                //Holds year
                                                yearCheck[k][0] = Integer.valueOf(year);
                                                //Holds 1 because first appearance
                                                yearCheck[k][1] = 1;
                                                //Adds the year to hashmap
                                                map.put(listGenres[i], yearCheck);
                                                //Leaves loop after adding
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    //Prints total times genre appeared
    public static void totalAppearances() {
        //Gets all the keys from hashmap
        Set<String> allKeys = map.keySet();
        //Variable used to keep track of amount of times the genre appeared
        int count = 0;
        //Used to move the set of keys into array
        String[] arrayKeys = new String[allKeys.size()];
        //Moves keys to array
        allKeys.toArray(arrayKeys);
        //Array used to sort
        String[][] arrayToSort = new String[arrayKeys.length][3];
        //Total used to see average of all movies
        int totalMovies = 0;
        //Does this for every genre
        for (int j = 0; j < arrayKeys.length; j++) {
            //Gets the value from hashmap
            int[][] allData = map.get(arrayKeys[j]);
            //Does this for length of array and grabs the total appearance
            for (int i = 0; i < allData.length; i++) {
                //Adds new amount to count
                count = count + allData[i][1];
            }
            //Sets genre name
            arrayToSort[j][0] = arrayKeys[j];
            //Sets the amount of times it appeared
            arrayToSort[j][1] = String.valueOf(count);
            //Total times all movies appeared
            totalMovies = totalMovies + count;
            //resets count
            count = 0;
        }
        //Goes to method sortList to sort the array
        sortList(arrayToSort);
        //Calculates the average
        totalMovies = totalMovies / arrayKeys.length;
        //Does this for the length of genres
        for (int k = 0; k < arrayToSort.length; k++) {
            //Checks if its above average or less then average if less goes to else
            if (Integer.valueOf(arrayToSort[k][1]) > totalMovies) {
                //Prints amount of times the genre appeared more then average
                System.out.println(arrayToSort[k][0] + " appeared more then average with a total of " + arrayToSort[k][1] + " times");
            } else {
                //Prints the amount of times the genre appeared less then average
                System.out.println(arrayToSort[k][0] + " appeared less then average with a total of " + arrayToSort[k][1] + " times");

            }

        }
        //Prints extra line for neatness
        System.out.println();
    }

    //Method used to check how many times the genre appeared a certain of years back
    public static void appearedInLast(int yearsBack) {
        //Current year is 2020
        int currentYear = 2020;
        //Used to keep track of genres released before yearsback(5)
        int passYears = 0;
        //Used to keep track of genres released after yearsback(5)
        int afterYears = 0;
        //Gets all keys and moves to set
        Set<String> allKeys = map.keySet();
        //Used to move the keys into array
        String[] arrayKeys = new String[allKeys.size()];
        allKeys.toArray(arrayKeys);
        //Used to sort array
        String[][] arrayToSort = new String[arrayKeys.length][3];
        //Does this for every genre
        for (int j = 0; j < arrayKeys.length; j++) {
            //Gets the values from key
            int[][] allData = map.get(arrayKeys[j]);
            //Does this for each value
            for (int i = 0; i < allData.length; i++) {
                //Checks if its older then 5 years or not if not goes to else
                if (allData[i][0] < currentYear - yearsBack) {
                    //Adds new amount to passYears
                    passYears = passYears + allData[i][1];
                } else {
                    //Checks if its newer then 5 years
                    if (allData[i][0] >= currentYear - yearsBack) {
                        //Adds new amount to afterYears
                        afterYears = afterYears + allData[i][1];
                    }
                }

            }
            //Stores the genre
            arrayToSort[j][0] = arrayKeys[j];
            //Stores the appearances in recent years
            arrayToSort[j][1] = String.valueOf(afterYears);
            //Used to store appearances before specified year
            arrayToSort[j][2] = String.valueOf(passYears);
            //resets for next genre
            afterYears = 0;
            passYears = 0;
        }
        //Goes to sortList to sort the array
        sortList(arrayToSort);
        //Does this for the amount of genres
        for (int k = 0; k < arrayToSort.length; k++) {
            //Prints the number of appearances before 2015 and after 2015 in this case
            System.out.println(arrayToSort[k][0] + " appeared a total of " + arrayToSort[k][2] + " times before " + (currentYear - yearsBack) + " and " + arrayToSort[k][1] + " times after " + (currentYear - yearsBack));
        }
        //Prints extra line for neatness
        System.out.println();
    }

    //Prints the amount of times each genre showed up each year
    public static void appearedPerYearPerGenre() {
        //Set to hold keys
        Set<String> allKeys = map.keySet();
        //Used to move the keys to array
        String[] arrayKeys = new String[allKeys.size()];
        //Moves the keys to array
        allKeys.toArray(arrayKeys);

        //Does this for each genre
        for (int j = 0; j < arrayKeys.length; j++) {
            //Used to store the value from the key
            int[][] allData = map.get(arrayKeys[j]);
            //Used to sort array later
            String[][] arrayToSort = new String[500][3];
            //Does this for the length of the value
            for (int i = 0; i < allData.length; i++) {
                //Checks if value is present
                if (allData[i][0] != 0) {
                    //Sets slot equal to genre
                    arrayToSort[i][0] = arrayKeys[j];
                    //Sets slot equal to year
                    arrayToSort[i][1] = String.valueOf(allData[i][0]);
                    //Sets slot equal to times appeared
                    arrayToSort[i][2] = String.valueOf(allData[i][1]);
                }
            }
            //Goes to method sortList to sort the array
            sortList(arrayToSort);
            //Prints out genre name
            System.out.print(arrayToSort[j][0] + " appeared ");
            //Does this for each year
            for (int k = 0; k < arrayToSort.length; k++) {
                //Checks to see if year is present
                if (arrayToSort[k][2] != null) {
                    //Checks if it appeared more then once that time to print different strings if once prints time if else prints times
                    if (Integer.valueOf(arrayToSort[k][2]) == 1) {
                        //Prints times of appearance per year
                        System.out.print(arrayToSort[k][2] + " time in " + arrayToSort[k][1] + ", ");
                    } else {
                        //Prints times of appearance per year
                        System.out.print(arrayToSort[k][2] + " times in " + arrayToSort[k][1] + ", ");
                    }
                }
            }

                //Prints extra line for neatness
            System.out.println();
        }


    }

    //Method used to print amount of movies released each year
    public static void releasePerYear() throws IOException {
        //Opens file for reading
        File records = new File("Data/movies.csv");
        Scanner file = new Scanner(records);
        //Hash map used to store years and time appeared
        HashMap<String, Integer> yearTimes = new HashMap<>();
        //Skips first line
        file.nextLine();
        //Does this till the end of file
        while (file.hasNextLine()) {
            //Goes to next line
            String line = file.nextLine();
            //Grabs the year had to check if line contains " to grab year goes to else if it does
            if (!line.contains("\"")) {
                //Splits line by ),
                yearsplit = line.split("\\)+,");
                //Grabs year
                year = yearsplit[0].charAt(yearsplit[0].length() - 4) + String.valueOf(yearsplit[0].charAt(yearsplit[0].length() - 3)) + yearsplit[0].charAt(yearsplit[0].length() - 2) + yearsplit[0].charAt(yearsplit[0].length() - 1);
            } else {
                //Splits line by )",
                yearsplit = line.split("\\)" + "\"" + "+,");
                //Grabs year
                year = yearsplit[0].charAt(yearsplit[0].length() - 4) + String.valueOf(yearsplit[0].charAt(yearsplit[0].length() - 3)) + yearsplit[0].charAt(yearsplit[0].length() - 2) + yearsplit[0].charAt(yearsplit[0].length() - 1);
            }

            //Checks to see if hashmap contains year
            if (!yearTimes.containsKey(year)) {
                //if year is not already in hash map adds year and sets value to 1
                yearTimes.put(year, 1);
            } else {
                //Does this if year is already in hashmap
                if (yearTimes.containsKey(year)) {
                    //Gets the previous value and adds 1 to it
                    int currentTotal = yearTimes.get(year) + 1;
                    //Adds new value to hashmap
                    yearTimes.put(year, currentTotal);

                }
            }


        }

        //Goes to method sortbykey and prints out sorted hashmap using treemap
        sortbykey(yearTimes);
    }


    //Sorts hashmap with treemap and prints our release of movies per year
    public static void sortbykey(HashMap yearTimes) {
        //Treemap to sort
        TreeMap<String, Integer> tree = new TreeMap<>();

        //Moves data from hashmap to treemap
        tree.putAll(yearTimes);

        //Prints tree
        System.out.println();
        for (Map.Entry<String, Integer> entry : tree.entrySet()) {
            System.out.println(entry.getValue() + " movies released in " + entry.getKey());
        }
    }


    public static String[][] sortList(String[][] myList) {
        //Used to hold temp of first slot when swapping two slots
        String temp;
        //Used to hold temp year when swapping two slots to keep the year with the title
        String tempYear;
        //Used to hold amount of times it appeared
        String tempTimes;
        //Selection sort to sort the array
        for (int i = 0; i < myList.length; i++) {
            for (int j = i + 1; j < myList.length; j++) {
                if (myList[j][0] != null && myList[i][0] != null) {
                    if (Integer.valueOf(myList[i][1]) < Integer.valueOf(myList[j][1])) {
                        //sets value to swap reused in multiple methods
                        temp = myList[i][0];
                        myList[i][0] = myList[j][0];
                        myList[j][0] = temp;
                        //sets value to swap reused in multiple methods
                        tempYear = myList[i][1];
                        myList[i][1] = myList[j][1];
                        myList[j][1] = tempYear;
                        //sets value to swap reused in multiple methods
                        tempTimes = myList[i][2];
                        myList[i][2] = myList[j][2];
                        myList[j][2] = tempTimes;
                    }
                }
            }
        }
        //returns the sorted list
        return myList;
    }

}
