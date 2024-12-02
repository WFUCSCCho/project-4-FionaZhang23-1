import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.io.*;

public class Proj4 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java TestAvl <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        // For file input
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // Open the input file
        inputFileNameStream = new FileInputStream(inputFileName);
        inputFileNameScanner = new Scanner(inputFileNameStream);

        // ignore first line
        inputFileNameScanner.nextLine();

        ArrayList<Song> dataList = new ArrayList<>();

        int count = 0;
        // Read data from the file
        for (int i = 0; i < numLines && inputFileNameScanner.hasNextLine(); i++) {
            String line = inputFileNameScanner.nextLine();
            String[] parts = line.split(",");

            // Trim whitespace from each part
            for (int j = 0; j < parts.length; j++) {
                parts[j] = parts[j].trim();
            }

            try {
                String trackName = parts[0];
                String artistName = parts[1];
                int releaseYear = Integer.parseInt(parts[2]);
                long streams = Long.parseLong(parts[3]);
                int bpm = Integer.parseInt(parts[4]);
                int danceability = Integer.parseInt(parts[5]);
                dataList.add(new Song(trackName, artistName, releaseYear, streams, bpm, danceability));
            } catch (NumberFormatException e) {
            }
        }

        inputFileNameScanner.close();

        // Sort, shuffle, and reverse the data
        ArrayList<Song> sortedList = new ArrayList<>(dataList);
        Collections.sort(sortedList);

        ArrayList<Song> shuffledList = new ArrayList<>(dataList);
        Collections.shuffle(shuffledList);

        ArrayList<Song> reversedList = new ArrayList<>(dataList);
        Collections.sort(reversedList, Collections.reverseOrder());

        SeparateChainingHashTable<Song> table;
        long start, end;

        // Test performance for sorted list
        table = new SeparateChainingHashTable<>();
        start = System.nanoTime();
        for (Song song : sortedList) {
            table.insert(song);
        }
        end = System.nanoTime();
        long sortedInsertTime = end - start;

        start = System.nanoTime();
        for (Song song : sortedList) {
            table.contains(song);
        }
        end = System.nanoTime();
        long sortedSearchTime = end - start;

        start = System.nanoTime();
        for (Song song : sortedList) {
            table.remove(song);
        }
        end = System.nanoTime();
        long sortedDeleteTime = end - start;

        System.out.printf("Sorted: Insert = %d ns, Search = %d ns, Delete = %d ns%n", sortedInsertTime, sortedSearchTime, sortedDeleteTime);

        // Test performance for shuffled list
        table = new SeparateChainingHashTable<>();
        start = System.nanoTime();
        for (Song song : shuffledList) {
            table.insert(song);
        }
        end = System.nanoTime();
        long shuffledInsertTime = end - start;

        start = System.nanoTime();
        for (Song song : shuffledList) {
            table.contains(song);
        }
        end = System.nanoTime();
        long shuffledSearchTime = end - start;

        start = System.nanoTime();
        for (Song song : shuffledList) {
            table.remove(song);
        }
        end = System.nanoTime();
        long shuffledDeleteTime = end - start;

        System.out.printf("Shuffled: Insert = %d ns, Search = %d ns, Delete = %d ns%n", shuffledInsertTime, shuffledSearchTime, shuffledDeleteTime);

        // Test performance for reversed list
        table = new SeparateChainingHashTable<>();
        start = System.nanoTime();
        for (Song song : reversedList) {
            table.insert(song);
        }
        end = System.nanoTime();
        long reversedInsertTime = end - start;

        start = System.nanoTime();
        for (Song song : reversedList) {
            table.contains(song);
        }
        end = System.nanoTime();
        long reversedSearchTime = end - start;

        start = System.nanoTime();
        for (Song song : reversedList) {
            table.remove(song);
        }
        end = System.nanoTime();
        long reversedDeleteTime = end - start;

        System.out.printf("Reversed: Insert = %d ns, Search = %d ns, Delete = %d ns%n", reversedInsertTime, reversedSearchTime, reversedDeleteTime);

        // Write results to analysis.txt
        try (PrintWriter pw = new PrintWriter(new FileOutputStream("analysis.txt", true))) {
            pw.printf("Number of Lines: %d%n", numLines);
            pw.printf("Sorted List:%n");
            pw.printf("Insertion Time: %d ns%n", sortedInsertTime);
            pw.printf("Search Time: %d ns%n", sortedSearchTime);
            pw.printf("Deletion Time: %d ns%n%n", sortedDeleteTime);

            pw.printf("Shuffled List:%n");
            pw.printf("Insertion Time: %d ns%n", shuffledInsertTime);
            pw.printf("Search Time: %d ns%n", shuffledSearchTime);
            pw.printf("Deletion Time: %d ns%n%n", shuffledDeleteTime);

            pw.printf("Reversed List:%n");
            pw.printf("Insertion Time: %d ns%n", reversedInsertTime);
            pw.printf("Search Time: %d ns%n", reversedSearchTime);
            pw.printf("Deletion Time: %d ns%n%n", reversedDeleteTime);
            pw.println();
        }
    }
}
