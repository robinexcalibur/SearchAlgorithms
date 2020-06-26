import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class part2 {

    public static void main (String[] args) {

        Scanner scanner = new Scanner(System.in);
        int sizeOfData = 0;
        while (sizeOfData <= 0 || sizeOfData > 500000) {
            System.out.print("Please enter the number of random numbers you'd like to generate (< 500,000): ");
            sizeOfData = scanner.nextInt();
        }

        createData(sizeOfData);

        String command = "";

        while (!command.toLowerCase().equals("q")) {
            System.out.println("Please pick one of the following options: \n" +
                    "Print data (p) || Compare sorting algorithms (sort) || Compare searching algorithms (search) || Quit (q)");
            command = scanner.next();

            if (command.toLowerCase().equals("p")) { //print case
                printData();

            } else if (command.toLowerCase().equals("sort")) { //Sort case
                System.out.println("Loading data....");
                int[] data = loadData(sizeOfData);

                System.out.println("\nInsertion Sort:");
                long start = System.nanoTime();
                int[] results = insertionSort(data);
                long end = System.nanoTime();
                System.out.println("#compares: " + results[0] + " #swaps: " + results[1] + " Time: " + (end-start));

                System.out.println("\nSelection Sort:");
                data = loadData(sizeOfData);
                start = System.nanoTime();
                results = selectionSort(data);
                end = System.nanoTime();
                System.out.println("#compares: " + results[0] + " #swaps: " + results[1] + " Time: " + (end-start));


                System.out.println("\nMerge Sort:");
                data = loadData(sizeOfData);
                results[0] = 0;
                results[1] = 0;
                start = System.nanoTime();
                mergeSort(data, results);
                end = System.nanoTime();
                System.out.println("#compares: " + results[0] + " #placements: " + results[1] + " Time: " + (end-start));

                System.out.println("\nQuick Sort:");
                data = loadData(sizeOfData);
                results[0] = 0;
                results[1] = 0;
                start = System.nanoTime();
                quickSort(data, sizeOfData-1, 0, results);
                end = System.nanoTime();
                System.out.println("#compares: " + results[0] + " #swaps: " + results[1] + " Time: " + (end-start));


            } else if (command.toLowerCase().equals("search")) {
                System.out.println("Loading data....");
                int[] data = loadData(sizeOfData);

                System.out.println("\nSequential search on unsorted array:");
                long start = System.nanoTime();
                int results = sequentialSearch(data, 5);
                long end = System.nanoTime();
                System.out.println("#compares: " + results + " Time: " + (end-start));

                System.out.println("\nBinary search with sorted array::");
                selectionSort(data); //sorts data as a side effect.
                start = System.nanoTime();
                results = binarySearch(data, 5);
                end = System.nanoTime();
                System.out.println("#compares: " + results + " Time: " + (end-start));

                System.out.println("\nBinary search with BST:");
                BST tree = new BST();
                for (int i = 0; i < sizeOfData; i++) {
                    tree.put(data[i], data[i]);
                }
                start = System.nanoTime();
                results = BSTsearch(tree, 5);
                end = System.nanoTime();
                System.out.println("#compares: " + results + " Time: " + (end-start));


            }
        }





    }



    //----------for Main Method------------
    private static void createData (int sizeOfData) {
        Random rand = new Random();

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("unsorted500000.txt");
            for (int i = 0; i < sizeOfData; i++) {
                fileWriter.write(rand.nextInt() + " ");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printData () {
        try {
            File data = new File("unsorted500000.txt");
            Scanner fileScanner = new Scanner(data);

            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int[] loadData(int sizeOfData) {
        File data = new File("unsorted500000.txt");
        int[] array = new int[sizeOfData];
        try {
            Scanner fileScanner = new Scanner(data);
            for (int i = 0; i < sizeOfData; i++) {
                array[i] = fileScanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return array;
    }

    //helper method that prints array. I used this to make sure the sorting methods were sorting correctly.
    private static void printArray (int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + ", ");
        }
        System.out.println();
    }


    //-----------Sorting Alg.---------------
    //Each returns an int[2] were index 0 = #compares and 1 = #swaps

    //Insertion sort:
    private static int[] insertionSort(int[] array) {
        int[] results = {0, 0};
        //0 = compares, 1 = swaps

        for (int i = 1; i < array.length; i++) {
            //movingValue is the index we're moving. Tester is the index we're currently testing it against.
            int movingValue = i;
            int testingValue = i-1;
            results[0]++; //for array[i] < array[tester]
            while (testingValue >= 0 && array[movingValue] < array[testingValue]) {
                int holder = array[testingValue];
                array[testingValue] = array[movingValue];
                array[movingValue] = holder;

                testingValue--;
                movingValue--;
                results[1]++; //for the above swap
                results[0]++; //for the following while loop test
            }
        }

        //printArray(array);
        return results;
    }


    //Selection sort:
    private static int[] selectionSort(int[] array) {
        int[] results = {0, 0};
        //0 = compares, 1 = swaps


        for (int i = 0; i < array.length; i++) {
            int min = i; //holds index, not value

            //search the array
            for (int j = i+1; j < array.length; j++) {
                if (array[min] > array[j]) {
                    min = j;
                }
                results[0]++;
            }

            //swap if needed
            if (min != i) {
                int holder = array[i];
                array[i] = array[min];
                array[min] = holder;
                results[1]++;
            }
        }
        //printArray(array);
        return results;
    }


    //Merge sort:
    //please send it an int[2] array to count in.
    private static int[] mergeSort(int[] array, int[] counter) {
        if (array.length <= 1 ) {
            return array;
        }
        int half = array.length / 2;
        int[] b = mergeSort(Arrays.copyOfRange(array, 0, half), counter);
        int[] c = mergeSort(Arrays.copyOfRange(array, half, array.length), counter);
        return merge(b, c, half, counter);
    }

    //Merge sort helper method
    private static int[] merge (int[] a, int[] b, int left, int[] counter) {
        int[] sorted = new int[a.length + b.length];
        int ai = 0;
        int bi = 0;

        for (int i = 0; i < sorted.length; i++) {
            if (ai >= a.length) {
                counter[0]++;
                sorted[i] = b[bi];
                bi++;
            } else if (bi >= b.length) {
                counter[0] = counter[0] + 2;
                sorted[i] = a[ai];
                ai++;
            } else if (b[bi] < a[ai]) {
                counter[0] = counter[0] + 3;
                sorted[i] = b[bi];
                bi++;
            } else { //a's is smaller
                counter[0] = counter[0] + 3;
                sorted[i] = a[ai];
                ai++;
            }
            counter[1]++; //one item placed
        }
        return sorted;
    }


    //Quick sort:
    //please sent it an int[2] array to count in.
    private static void quickSort(int[] array, int top, int bottom, int[] counter) {
        if (bottom < top) {
            int p = quickSortSwapping(array, top, bottom, counter);

            quickSort(array, p-1, bottom, counter); //bottom half
            quickSort(array, top, p+1, counter); //top half
        }

    }

    //helper method for quick sort. We're going to always use the last element as a partition.
    private static int quickSortSwapping (int[] array, int top, int bottom, int[] counter) {
        //0 = compares, 1 = swaps
        int first = bottom;
        int last = top - 1;

        int partition = array[top];

        while (first < last) {
            counter[0]++;
            while (first < top && array[first] < partition) {
                first++;
                counter[0]++;
            }

            counter[0]++;
            while (last >= 0 && array[last] > partition) { //move this to right
                last--;
                counter[0]++;
            }

            if (first < last) {
                //swap indexes
                int holder = array[first];
                array[first] = array[last];
                array[last] = holder;
                counter[1]++;
                first++;
                last--;
            }

        }
        //put partition in place
        array[top] = array[first];
        array[first] = partition;
        counter[1]++;
        return first; //return location of partition, it's now correctly placed
    }



    //----------Searching Alg.--------------
    //each returns the #compares

    //Sequential Search on unsorted Array
    private static int sequentialSearch(int[] array, int target) {
        int compares = 0;

        for (int i = 0; i < array.length; i++) {
            compares++;
            if (array[i] == target) {
                break;
            }
        }
        return compares;
    }

    //Binary Search on sorted Array. This method takes care of sorting the array.
    private static int binarySearch(int[] array, int target) {
        int compares = 0;

        //Sort array
        //int[] merge = {0,0}; //just so I can use my merge sort
        //array = mergeSort(array, merge);

        //Now search
        int l = 0;
        int r = array.length-1;
        while (l < r) {
            int middle = ((r-l)/2) + l;
            compares++; // for the array[middle] == target
            if (array[middle] == target) {
                break;
            } else if (array[middle] < target) {
                l = middle + 1;
            } else {
                r = middle - 1;
            }
            compares++; //for the array[middle] < target
        }

        return compares;
    }


    //Binary Search on BST
    private static int BSTsearch (BST tree, int target) {
        return tree.containsWithCompares(target);
    }

}
