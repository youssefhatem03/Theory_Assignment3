import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {


            BufferedReader brCFG = new BufferedReader(new FileReader("input_cfg.txt"));
            BufferedWriter bwCFG = new BufferedWriter(new FileWriter("output_cfg.txt"));


            for (int i = 1; i <= 5; i++) {
                // Read and discard the problem number line
                String problemNumLine = brCFG.readLine();

                bwCFG.write( i + "\n");
                bwCFG.flush();
                switch (i) {
                    case 1: new CFGProblem1(brCFG, bwCFG); break;
                    case 2: new CFGProblem2(brCFG, bwCFG); break;
                    case 3: new CFGProblem3(brCFG, bwCFG); break;
                    case 4: new CFGProblem4(brCFG, bwCFG); break;
                    case 5: new CFGProblem5(brCFG, bwCFG); break;
                }
            }
            brCFG.close();
            bwCFG.close();




            BufferedReader brPDA = new BufferedReader(new FileReader("input_pda.txt"));
            BufferedWriter bwPDA = new BufferedWriter(new FileWriter("output_pda.txt"));


            for (int i = 1; i <= 5; i++) {
                // Read and discard the problem number line
                String problemNumLine = brPDA.readLine();

                bwPDA.write( i + "\n");
                bwPDA.flush();
                switch (i) {
                    case 1: new PDAProblem1(brPDA, bwPDA); break;
                    case 2: new PDAProblem2(brPDA, bwPDA); break;
                    case 3: new PDAProblem3(brPDA, bwPDA); break;
                    case 4: new PDAProblem4(brPDA, bwPDA); break;
                    case 5: new PDAProblem5(brPDA, bwPDA); break;
                }
            }
            brPDA.close();
            bwPDA.close();

            System.out.println("All problems processed successfully!");
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}
