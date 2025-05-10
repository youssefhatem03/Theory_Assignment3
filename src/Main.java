import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Create file readers and writers
            BufferedReader brCFG = new BufferedReader(new FileReader("input_cfg.txt"));
            BufferedWriter bwCFG = new BufferedWriter(new FileWriter("output_cfg.txt"));

            // Process CFG problems
            System.out.println("Processing CFG problems...");
            new CFGProblem1(brCFG, bwCFG);
            new CFGProblem2(brCFG, bwCFG);
            new CFGProblem3(brCFG, bwCFG);
            new CFGProblem4(brCFG, bwCFG);
            new CFGProblem5(brCFG, bwCFG);

            // Close CFG files
            brCFG.close();
            bwCFG.close();

            // Process PDA problems
            System.out.println("Processing PDA problems...");
            BufferedReader brPDA = new BufferedReader(new FileReader("input_pda.txt"));
            BufferedWriter bwPDA = new BufferedWriter(new FileWriter("output_pda.txt"));

            new PDAProblem1(brPDA, bwPDA);
            new PDAProblem2(brPDA, bwPDA);
            new PDAProblem2(brPDA, bwPDA);
            new PDAProblem2(brPDA, bwPDA);
            new PDAProblem2(brPDA, bwPDA);
//            new PDAProblem3(brPDA, bwPDA);
//            new PDAProblem4(brPDA, bwPDA);
//            new PDAProblem5(brPDA, bwPDA);

            // Close PDA files
            brPDA.close();
            bwPDA.close();

            System.out.println("All problems processed successfully!");
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}