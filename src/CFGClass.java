import java.io.*;
import java.util.*;

public class CFGClass {
    private ArrayList<Character> terminals;
    private ArrayList<Character> nonTerminals;
    private Character startSymbol;
    private Map<Character, ArrayList<String>> productions;
    private static final int MAX_BFS_STEPS = 1000000;

    public CFGClass(ArrayList<Character> terminals, ArrayList<Character> nonTerminals,
                    Character startSymbol, Map<Character, ArrayList<String>> productions) {
        this.terminals = terminals;
        this.nonTerminals = nonTerminals;
        this.startSymbol = startSymbol;
        this.productions = productions;
    }


    public boolean derive(String currentDerivation, String text) {
        return false;
    }


    public void solveProblem(BufferedReader br, BufferedWriter bw) throws IOException {
        String line;

        line = br.readLine();
        if (line == null) {
            return;
        }

        bw.write(line);
        bw.newLine();

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equalsIgnoreCase("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            boolean result = derive(String.valueOf(startSymbol), line);
            bw.write(result ? "accepted" : "not accepted");
            bw.newLine();
        }

        bw.write("x");
        bw.newLine();
        bw.flush();
    }
}


class CFGProblem1 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();


    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aSb",
            "bSa",
            "SS",
            "ε"
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


class CFGProblem2 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aSbb",
            "ε"
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


class CFGProblem3 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aSa",
            "bSb",
            "aSb",
            "bSa",
            "a",
            "b",
            "ε"
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


class CFGProblem4 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S', 'A'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aaaA"
    ));

    ArrayList<String> production_A = new ArrayList<>(Arrays.asList(
            "aaAb",
            "ε"
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


class CFGProblem5 {
    ArrayList<Character> terminals = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> nonTerminals = new ArrayList<>(Arrays.asList('S', 'A'));
    Character startSymbol = 'S';
    Map<Character, ArrayList<String>> productionRules = new HashMap<>();

    ArrayList<String> production_S = new ArrayList<>(Arrays.asList(
            "aS",
            "aA"
    ));

    ArrayList<String> production_A = new ArrayList<>(Arrays.asList(
            "aA",
            "bA",
            "ε"
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