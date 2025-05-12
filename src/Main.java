import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Process CFG problems
            BufferedReader brCFG = new BufferedReader(new FileReader("input_cfg.txt"));
            BufferedWriter bwCFG = new BufferedWriter(new FileWriter("output_cfg.txt"));

            // Process each CFG problem
            new CFGProblem1(brCFG, bwCFG);
            new CFGProblem2(brCFG, bwCFG);
            new CFGProblem3(brCFG, bwCFG);
            new CFGProblem4(brCFG, bwCFG);
            new CFGProblem5(brCFG, bwCFG);

            // Close CFG files
            brCFG.close();
            bwCFG.close();

            // Process PDA problems
            BufferedReader brPDA = new BufferedReader(new FileReader("input_pda.txt"));
            BufferedWriter bwPDA = new BufferedWriter(new FileWriter("output_pda.txt"));

            // Process each PDA problem
            new PDAProblem1(brPDA, bwPDA);
            new PDAProblem2(brPDA, bwPDA);
            new PDAProblem3(brPDA, bwPDA);
            new PDAProblem4(brPDA, bwPDA);
            new PDAProblem5(brPDA, bwPDA);

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