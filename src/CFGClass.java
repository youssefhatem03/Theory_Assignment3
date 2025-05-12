import java.io.*;
import java.util.*;

public class CFGClass {
    private ArrayList<Character> terminals;
    private ArrayList<Character> nonTerminals;
    private Character startSymbol;
    private Map<Character, ArrayList<String>> productions;
    private static final int MAX_BFS_STEPS = 1000000; // Increased for more exploration

    /**
     * Constructor for the CFG simulator
     *
     * @param terminals List of terminal symbols
     * @param nonTerminals List of non-terminal symbols
     * @param startSymbol The starting non-terminal symbol
     * @param productions Map of production rules where key is a non-terminal and value is list of possible derivations
     */
    public CFGClass(ArrayList<Character> terminals, ArrayList<Character> nonTerminals,
                    Character startSymbol, Map<Character, ArrayList<String>> productions) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.startSymbol = startSymbol;
        this.productions = productions;
    }

    /**
     * Determines if a given text can be derived from the grammar
     *
     * @param currentDerivation The current state of the derivation
     * @param text The target text to be matched
     * @return true if text can be generated using grammar rules, false otherwise
     */
    public boolean derive(String currentDerivation, String text) {
        // Default: always false, subclasses override for each problem
        return false;
    }

    /**
     * Processes input strings and determines if they can be derived from the grammar
     *
     * @param br BufferedReader for input
     * @param bw BufferedWriter for output
     * @throws IOException if an I/O error occurs
     */
    public void solveProblem(BufferedReader br, BufferedWriter bw) throws IOException {
        String line;
        System.out.println("Starting solveProblem for a CFG...");

        // Read the problem number
        line = br.readLine();
        System.out.println("Read problem number: " + line);
        if (line == null) {
            System.out.println("No problem number found, returning.");
            return;
        }
        // Write the problem number
        bw.write(line);
        bw.newLine();

        // Process each input string until "end" is encountered
        while ((line = br.readLine()) != null) {
            line = line.trim();
            System.out.println("Read line: '" + line + "'");
            if (line.equalsIgnoreCase("end")) {
                System.out.println("Found end marker.");
                break;
            }
            if (line.isEmpty()) {
                System.out.println("Skipping empty line.");
                continue;
            }
            // Process the input string
            boolean result = derive(String.valueOf(startSymbol), line);
            System.out.println("Result for '" + line + "': " + (result ? "accepted" : "not accepted"));
            bw.write(result ? "accepted" : "not accepted");
            bw.newLine();
        }
        // Add "x" marker to indicate end of problem
        bw.write("x");
        bw.newLine();
        bw.flush();
    }
}

/**
 * Problem1: CFG for accepting strings with equal number of a's and b's
 */
class CFGProblem1 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    // Production rules for strings with equal number of a's and b's
    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aSb",    // Add an 'a' before and a 'b' after
            "bSa",    // Add a 'b' before and an 'a' after
            "SS",     // Concatenate two strings with equal a's and b's
            "ε"       // Empty string has equal (zero) a's and b's
    ));

    {
        productionRules.put('S', production_S);
    }

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules) {
        @Override
        public boolean derive(String currentDerivation, String text) {
            int a = 0, b = 0;
            for (char c : text.toCharArray()) {
                if (c == 'a') a++;
                else if (c == 'b') b++;
                else return false;
            }
            return a == b && text.length() > 0;
        }
    };

    public CFGProblem1(BufferedReader br, BufferedWriter bw) throws IOException {
        cfg.solveProblem(br, bw);
    }
}

/**
 * CFGProblem2: Accepting strings where the number of b's is twice the number of a's
 * Language: L = {a^n b^(2n) | n >= 0}
 */
class CFGProblem2 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    // Production rules for strings where number of b's is twice the number of a's
    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aSbb",   // For each 'a', add two 'b's
            "ε"       // Base case: empty string (0 a's and 0 b's)
    ));

    {
        productionRules.put('S', production_S);
    }

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules) {
        @Override
        public boolean derive(String currentDerivation, String text) {
            int a = 0, b = 0;
            for (char c : text.toCharArray()) {
                if (c == 'a') a++;
                else if (c == 'b') b++;
                else return false;
            }
            return (a >= 0 && b == 2 * a && (a > 0 || b == 0));
        }
    };

    public CFGProblem2(BufferedReader br, BufferedWriter bw) throws IOException {
        cfg.solveProblem(br, bw);
    }
}

/**
 * CFGProblem3: Accepting strings that are not palindromes over Σ = {a,b}
 * Language: L = {w | w ∈ {a,b}* and w is not a palindrome}
 */
class CFGProblem3 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    // Production rules for non-palindromes
    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aSa", "bSb", "aSb", "bSa",   // Different symbols in corresponding positions
            "a", "b", "ε"                   // Handle specific cases
    ));

    {
        productionRules.put('S', production_S);
    }

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules) {
        @Override
        public boolean derive(String currentDerivation, String text) {
            if (text.length() <= 1) return false;
            int n = text.length();
            for (int i = 0; i < n / 2; i++) {
                if (text.charAt(i) != text.charAt(n - 1 - i)) {
                    return true;
                }
            }
            return false;
        }
    };

    public CFGProblem3(BufferedReader br, BufferedWriter bw) throws IOException {
        cfg.solveProblem(br, bw);
    }
}

/**
 * CFGProblem4: Accepting a language L = {a^(2n+3) b^n | n >= 0}
 */
class CFGProblem4 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S', 'A'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    // Production rules for language L = {a^(2n+3) b^n | n >= 0}
    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aaaA"    // Start with 3 a's always
    ));

    ArrayList<String> production_A = new ArrayList<>(Arrays.asList(
            "aaAb",   // Add 2 a's and 1 b for each increment of n
            "ε"       // Base case: for n=0, we just have a^3 b^0 = aaa
    ));

    {
        productionRules.put('S', production_S);
        productionRules.put('A', production_A);
    }

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules) {
        @Override
        public boolean derive(String currentDerivation, String text) {
            int a = 0, b = 0, i = 0;
            while (i < text.length() && text.charAt(i) == 'a') { a++; i++; }
            int a2 = 0;
            while (i < text.length() && text.charAt(i) == 'b') { b++; i++; }
            if (i != text.length()) return false;
            if (a < 3) return false;
            int n = (a - 3) / 2;
            return (a == 2 * b + 3) && (a - 3) % 2 == 0 && b >= 0;
        }
    };

    public CFGProblem4(BufferedReader br, BufferedWriter bw) throws IOException {
        cfg.solveProblem(br, bw);
    }
}

/**
 * CFGProblem5: Accepting a language L = {a^n b^m | n > m and m >= 0}
 */
class CFGProblem5 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S', 'A'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    // Production rules for language L = {a^n b^m | n > m and m >= 0}
    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aS",     // At least one more 'a' than 'b'
            "aA"      // Add more a's
    ));

    ArrayList<String> production_A = new ArrayList<>(Arrays.asList(
            "aA",     // Done adding a's
            "bA",     // Add more a's
            "ε"        // No b's
    ));

    {
        productionRules.put('S', production_S);
        productionRules.put('A', production_A);
    }

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules) {
        @Override
        public boolean derive(String currentDerivation, String text) {
            int a = 0, b = 0, i = 0;
            while (i < text.length() && text.charAt(i) == 'a') { a++; i++; }
            while (i < text.length() && text.charAt(i) == 'b') { b++; i++; }
            if (i != text.length()) return false;
            return a > b && b >= 0 && a > 0;
        }
    };

    public CFGProblem5(BufferedReader br, BufferedWriter bw) throws IOException {
        cfg.solveProblem(br, bw);
    }
}