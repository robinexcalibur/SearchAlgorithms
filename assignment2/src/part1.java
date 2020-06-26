import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class part1 {

    public static void main (String[] args) {
        File testFile = new File("samples/samples/sample1.txt");
        System.out.println("This program will test if the following file can drain water: " + testFile.getName());
        try {
            Scanner scan = new Scanner(testFile);
            ArrayList<String> fileLines = new ArrayList<String>();

            //read raw string lines into array list
            while (scan.hasNextLine()) {
                fileLines.add(scan.nextLine().trim());
            }

            //figure out how big an int[][] we're making.
            //# objects in ArrayList = #rows || length of object + 1, /2 = # columns (because white spaces)
            int rows = fileLines.size();
            int cols = (fileLines.get(0).length()+1)/2;
            String[][] soil = new String[rows][cols];

            for (int i = 0; i < rows; i++) {
                soil[i] = fileLines.get(i).split(" ", rows);
            }

            //for indexes, I'm going to consider each element in the String[][] to be of index:
            //(row # * col.length) + col
            WeightedQuickUnionPathCompressionUF unionizer = new WeightedQuickUnionPathCompressionUF(rows*cols);

            //tests array by looking up and to the left. Also prints it.
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    System.out.print(soil[i][j] + " ");
                    if (soil[i][j].equals("1")) {
                        if (i > 0 && soil[i-1][j].equals("1")) { //not the first row
                            unionizer.union((i*cols) + j, ((i-1) * cols) + j);
                        }
                        if (j > 0 && soil[i][j-1].equals("1")) { //not the first column
                            unionizer.union((i*cols) + j, ((i*cols) + (j-1)));
                        }
                    }
                }
                System.out.println();
            }

            //find all the 1s in the top and bottom
            ArrayList<Integer> topOnes = new ArrayList<Integer>();
            ArrayList<Integer> bottomOnes = new ArrayList<Integer>();
            for (int i = 0; i < cols; i++) {
                if (soil[0][i].equals("1")) {
                    topOnes.add(i);
                }
                if (soil[rows-1][i].equals("1")) {
                    bottomOnes.add(i);
                }
            }

            //now check if they connect
            boolean drains = false;
            for (int i = 0; i < topOnes.size(); i++) {
                for (int j = 0; j < bottomOnes.size(); j++) {
                    if (unionizer.find(topOnes.get(i)) == unionizer.find(bottomOnes.get(j) + (cols * (rows-1)))) {
                        drains = true;
                        break;
                    }
                }
                if (drains) { break; }
            }

            if (drains) {
                System.out.println("Allows water to drain.");
            } else {
                System.out.println("Does not allow water to drain.");
            }


        } catch (FileNotFoundException e) {
            System.out.println("File could not be read. Program terminated.");
            e.printStackTrace();
        }

    }
}
