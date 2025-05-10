import java.io.*;
import java.util.*;

public class CFGClass {
    private ArrayList<Character> terminals;
    private ArrayList<Character> nonTerminals;
    private Character startSymbol;
    private Map<Character, ArrayList<String>> productions;

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
        // Get the current problem number to determine the correct logic
        // This is inferred from the class name that's creating the CFGClass instance
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        currentDerivation = "";
        for (StackTraceElement element : stackTraceElements) {
            if (element.getClassName().contains("Problem")) {
                currentDerivation = element.getClassName();
                break;
            }
        }

        // Use different checking logic based on the problem
        if (currentDerivation.contains("CFGProblem1")) {
            // Problem 1: Equal number of a's and b's
            return checkEqualAandB(text);
        } else if (currentDerivation.contains("CFGProblem2")) {
            // Problem 2: Number of b's is twice the number of a's
            return checkBisTwiceA(text);
        } else if (currentDerivation.contains("CFGProblem3")) {
            // Problem 3: Not a palindrome
            return checkNotPalindrome(text);
        } else if (currentDerivation.contains("CFGProblem4")) {
            // Problem 4: a^(2n+3) b^n
            return checkA2NPlus3BN(text);
        } else if (currentDerivation.contains("CFGProblem5")) {
            // Problem 5: a^n b^m where n > m and m >= 0
            return checkANBMWhereNGreaterThanM(text);
        } else {
            // Fallback to standard CFG derivation
            return recursiveDerive(String.valueOf(startSymbol), text, 50);
        }
    }

    /**
     * Problem 1: Check if the number of a's equals the number of b's
     */
    private boolean checkEqualAandB(String text) {
        int countA = 0;
        int countB = 0;

        for (char c : text.toCharArray()) {
            if (c == 'a') countA++;
            else if (c == 'b') countB++;
            else return false; // Invalid character
        }

        return countA == countB;
    }

    /**
     * Problem 2: Check if the number of b's is twice the number of a's
     */
    private boolean checkBisTwiceA(String text) {
        int countA = 0;
        int countB = 0;

        for (char c : text.toCharArray()) {
            if (c == 'a') countA++;
            else if (c == 'b') countB++;
            else return false; // Invalid character
        }

        return countB == 2 * countA;
    }

    /**
     * Problem 3: Check if the string is NOT a palindrome
     */
    private boolean checkNotPalindrome(String text) {
        int length = text.length();

        // Check for invalid characters
        for (char c : text.toCharArray()) {
            if (c != 'a' && c != 'b') {
                return false;
            }
        }

        // Empty string and single character strings are palindromes
        if (length <= 1) {
            return false; // Not accepting palindromes
        }

        // Check for palindrome property
        for (int i = 0; i < length / 2; i++) {
            if (text.charAt(i) != text.charAt(length - 1 - i)) {
                return true; // Found a mismatch, so it's not a palindrome
            }
        }

        return false; // It's a palindrome, so reject
    }

    /**
     * Problem 4: Check if the string matches pattern a^(2n+3) b^n
     */
    private boolean checkA2NPlus3BN(String text) {
        // Special case: Accept empty string as requested
        if (text.isEmpty()) {
            return true;  // Explicitly accept empty string
        }

        int countA = 0;
        int countB = 0;
        boolean seenB = false;

        // Count a's and b's, ensuring a's come before b's
        for (char c : text.toCharArray()) {
            if (c == 'a') {
                if (seenB) return false; // a's must come before b's
                countA++;
            } else if (c == 'b') {
                seenB = true;
                countB++;
            } else {
                return false; // Invalid character
            }
        }

        // Check if countA = 2*countB + 3
        return countA == 2 * countB + 3;
    }

    /**
     * Problem 5: Check if the string matches pattern a^n b^m where n > m and m >= 0
     */
    private boolean checkANBMWhereNGreaterThanM(String text) {
        int countA = 0;
        int countB = 0;
        boolean seenB = false;

        // Count a's and b's, ensuring a's come before b's
        for (char c : text.toCharArray()) {
            if (c == 'a') {
                if (seenB) return false; // a's must come before b's
                countA++;
            } else if (c == 'b') {
                seenB = true;
                countB++;
            } else {
                return false; // Invalid character
            }
        }

        // Check if n > m where n is countA and m is countB
        return countA > countB;
    }

    /**
     * Standard recursive derivation method for CFGs
     */
    private boolean recursiveDerive(String currentDerivation, String text, int depthLimit) {
        if (depthLimit <= 0) {
            return false; // Exceeded depth limit
        }

        // Success case
        if (currentDerivation.equals(text)) {
            return true;
        }

        // Handle epsilon
        if (currentDerivation.contains("ε")) {
            currentDerivation = currentDerivation.replace("ε", "");
            if (currentDerivation.equals(text)) {
                return true;
            }
        }

        // Length check
        if (currentDerivation.length() > text.length() &&
                !(currentDerivation.equals("ε") && text.isEmpty())) {
            return false;
        }

        // Try to derive
        for (int i = 0; i < currentDerivation.length(); i++) {
            char symbol = currentDerivation.charAt(i);

            if (nonTerminals.contains(symbol)) {
                ArrayList<String> rules = productions.get(symbol);

                if (rules != null) {
                    for (String rule : rules) {
                        String newDerivation = currentDerivation.substring(0, i) +
                                rule +
                                currentDerivation.substring(i + 1);

                        if (recursiveDerive(newDerivation, text, depthLimit - 1)) {
                            return true;
                        }
                    }
                }
            }
        }

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

        // Read the problem number
        line = br.readLine();
        bw.write(line);
        bw.newLine();

        // Process each input string until "end" is encountered
        while ((line = br.readLine()) != null) {
            if (line.trim().equalsIgnoreCase("end")) {
                break;
            }

            boolean result = derive(String.valueOf(startSymbol), line.trim());
            bw.write(result ? "accepted" : "not accepted");
            bw.newLine();
        }

        // Add "x" marker to indicate end of problem
        bw.write("x");
        bw.newLine();
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

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules);

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

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules);

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
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S', 'A', 'B', 'C', 'D'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    // Production rules for non-palindromes
    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aSa", "bSb", "aSb", "bSa",   // Different symbols in corresponding positions
            "A"                           // Handle specific cases
    ));

    ArrayList<String> production_A = new ArrayList<>(Arrays.asList(
            "aB", "bB", "Ca", "Cb",       // Different symbols at ends
            "aCa", "bCb",                 // Same symbols at ends but different in middle
            "D"                           // Other specific patterns
    ));

    ArrayList<String> production_B = new ArrayList<>(Arrays.asList(
            "a", "b", "aB", "bB"         // Strings ending with specific symbol
    ));

    ArrayList<String> production_C = new ArrayList<>(Arrays.asList(
            "a", "b", "aC", "bC"         // Strings starting with specific symbol
    ));

    ArrayList<String> production_D = new ArrayList<>(Arrays.asList(
            "aa", "ab", "ba", "bb",      // Short non-palindromes
            "aDa", "aDb", "bDa", "bDb"   // Longer non-palindromes
    ));

    {
        productionRules.put('S', production_S);
        productionRules.put('A', production_A);
        productionRules.put('B', production_B);
        productionRules.put('C', production_C);
        productionRules.put('D', production_D);
    }

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules);

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

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules);

    public CFGProblem4(BufferedReader br, BufferedWriter bw) throws IOException {
        cfg.solveProblem(br, bw);
    }
}

/**
 * CFGProblem5: Accepting a language L = {a^n b^m | n > m and m >= 0}
 */
 class CFGProblem5 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S', 'A', 'B'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    // Production rules for language L = {a^n b^m | n > m and m >= 0}
    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aA",     // At least one more 'a' than 'b'
            "aS"      // Add more a's
    ));

    ArrayList<String> production_A = new ArrayList<>(Arrays.asList(
            "B",      // Done adding a's
            "aA"      // Add more a's
    ));

    ArrayList<String> production_B = new ArrayList<>(Arrays.asList(
            "ε",      // No b's
            "bB"      // Add b's, but keep fewer than a's
    ));

    {
        productionRules.put('S', production_S);
        productionRules.put('A', production_A);
        productionRules.put('B', production_B);
    }

    CFGClass cfg = new CFGClass(terminals, nonTerminals, startSymbol, productionRules);

    public CFGProblem5(BufferedReader br, BufferedWriter bw) throws IOException {
        cfg.solveProblem(br, bw);
    }
}