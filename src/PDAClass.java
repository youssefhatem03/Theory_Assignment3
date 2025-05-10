import java.io.*;
import java.util.*;

class TransitionKey {
    private int currentState;
    private char input;
    private char stackTop;

    TransitionKey(int currentState, char input, char stackTop) {
        this.currentState = currentState;
        this.input = input;
        this.stackTop = stackTop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransitionKey that = (TransitionKey) o;
        return currentState == that.currentState &&
                input == that.input &&
                stackTop == that.stackTop;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentState, input, stackTop);
    }
}

class TransitionValue {
    private int nextState;
    private String stackPush;

    TransitionValue(int nextState, String stackPush) {
        this.nextState = nextState;
        this.stackPush = stackPush;
    }

    public int getNextState() {
        return nextState;
    }

    public String getStackPush() {
        return stackPush;
    }
}

class TransitionFunction {
    private Map<TransitionKey, List<TransitionValue>> transitions;

    public TransitionFunction() {
        this.transitions = new HashMap<>();
    }

    public void addTransition(int currentState, char input, char stackTop, int nextState, String stackPush) {
        TransitionKey key = new TransitionKey(currentState, input, stackTop);
        TransitionValue value = new TransitionValue(nextState, stackPush);

        if (!transitions.containsKey(key)) {
            transitions.put(key, new ArrayList<>());
        }
        transitions.get(key).add(value);
    }

    public List<TransitionValue> getTransitions(int currentState, char input, char stackTop) {
        TransitionKey key = new TransitionKey(currentState, input, stackTop);
        List<TransitionValue> result = transitions.getOrDefault(key, new ArrayList<>());
        return result;
    }
}

class PDAState {
    private int state;
    private Stack<Character> stack;
    private int position;

    public PDAState(int state, Stack<Character> stack, int position) {
        this.state = state;
        this.stack = stack;
        this.position = position;
    }

    public int getState() {
        return state;
    }

    public Stack<Character> getStack() {
        return stack;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PDAState pdaState = (PDAState) o;
        return state == pdaState.state &&
                position == pdaState.position &&
                Objects.equals(stack, pdaState.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, stack, position);
    }
}

public class PDAClass {
    private ArrayList<Integer> states;
    private ArrayList<Character> inputAlphabet;
    private ArrayList<Character> stackAlphabet;
    private TransitionFunction transitionFunction;
    private int startState;
    private ArrayList<Integer> finalStates;
    private char stackInitial;

    PDAClass(ArrayList<Integer> states, ArrayList<Character> inputAlphabet,
             ArrayList<Character> stackAlphabet, TransitionFunction transitionFunction,
             int startState, ArrayList<Integer> finalStates, char stackInitial) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.stackAlphabet = stackAlphabet;
        this.transitionFunction = transitionFunction;
        this.startState = startState;
        this.finalStates = finalStates;
        this.stackInitial = stackInitial;
    }

    public boolean isAccepted(String s) {
        // Start with initial state and stack
        Stack<Character> initialStack = new Stack<>();
        initialStack.push(stackInitial);

        // Use BFS to explore all possible execution paths
        Queue<PDAState> queue = new LinkedList<>();
        Set<PDAState> visited = new HashSet<>();

        queue.add(new PDAState(startState, initialStack, 0));

        while (!queue.isEmpty()) {
            PDAState current = queue.poll();

            int currentState = current.getState();
            Stack<Character> currentStack = current.getStack();
            int position = current.getPosition();

            // If we've reached the end of the string
            if (position == s.length()) {
                // Check if current state is in finalStates (acceptance by final state)
                if (finalStates.contains(currentState)) {
                    return true;
                }

                // Try epsilon transitions at the end
                processEpsilonTransitions(currentState, currentStack, position, queue, visited, s);
            } else {
                // Process the current input symbol
                char currentInput = s.charAt(position);
                processSymbolTransitions(currentState, currentInput, currentStack, position, queue, visited, s);

                // Also try epsilon transitions
                processEpsilonTransitions(currentState, currentStack, position, queue, visited, s);
            }
        }

        return false;
    }

    private void processSymbolTransitions(int currentState, char currentInput, Stack<Character> currentStack,
                                          int position, Queue<PDAState> queue, Set<PDAState> visited, String s) {
        // Check transitions with current input
        if (!currentStack.isEmpty()) {
            char stackTop = currentStack.peek();
            List<TransitionValue> transitions = transitionFunction.getTransitions(currentState, currentInput, stackTop);

            for (TransitionValue transition : transitions) {
                int nextState = transition.getNextState();
                String stackPush = transition.getStackPush();

                // Create a copy of the current stack
                Stack<Character> newStack = cloneStack(currentStack);

                // Pop the top element
                newStack.pop();

                // Push new elements to stack in reverse order
                for (int i = stackPush.length() - 1; i >= 0; i--) {
                    char c = stackPush.charAt(i);
                    newStack.push(c);
                }

                PDAState nextPDAState = new PDAState(nextState, newStack, position + 1);
                if (!visited.contains(nextPDAState)) {
                    visited.add(nextPDAState);
                    queue.add(nextPDAState);
                }
            }
        }
    }

    private void processEpsilonTransitions(int currentState, Stack<Character> currentStack,
                                           int position, Queue<PDAState> queue, Set<PDAState> visited, String s) {
        // Check for epsilon transitions
        if (!currentStack.isEmpty()) {
            char stackTop = currentStack.peek();
            List<TransitionValue> epsilonTransitions = transitionFunction.getTransitions(currentState, 'ε', stackTop);

            for (TransitionValue transition : epsilonTransitions) {
                int nextState = transition.getNextState();
                String stackPush = transition.getStackPush();

                // Create a copy of the current stack
                Stack<Character> newStack = cloneStack(currentStack);

                // Pop the top element
                newStack.pop();

                // Push new elements to stack in reverse order
                for (int i = stackPush.length() - 1; i >= 0; i--) {
                    char c = stackPush.charAt(i);
                    newStack.push(c);
                }

                PDAState nextPDAState = new PDAState(nextState, newStack, position);
                if (!visited.contains(nextPDAState)) {
                    visited.add(nextPDAState);
                    queue.add(nextPDAState);
                }
            }

            // Also check for epsilon input, epsilon stack transitions
            List<TransitionValue> epsilonEpsilonTransitions = transitionFunction.getTransitions(currentState, 'ε', 'ε');

            for (TransitionValue transition : epsilonEpsilonTransitions) {
                int nextState = transition.getNextState();
                String stackPush = transition.getStackPush();

                // Create a copy of the current stack
                Stack<Character> newStack = cloneStack(currentStack);

                // Push new elements to stack in reverse order
                for (int i = stackPush.length() - 1; i >= 0; i--) {
                    char c = stackPush.charAt(i);
                    newStack.push(c);
                }

                PDAState nextPDAState = new PDAState(nextState, newStack, position);
                if (!visited.contains(nextPDAState)) {
                    visited.add(nextPDAState);
                    queue.add(nextPDAState);
                }
            }
        }
    }

    private Stack<Character> cloneStack(Stack<Character> stack) {
        Stack<Character> temp = new Stack<>();
        Stack<Character> result = new Stack<>();

        // Pop all elements from the original stack and push to temp
        for (char c : stack) {
            temp.push(c);
        }

        // Pop all elements from temp and push to result
        while (!temp.isEmpty()) {
            result.push(temp.pop());
        }

        return result;
    }

    public void solveProblem(BufferedReader br, BufferedWriter bw) throws IOException {
        String line;
        // Read problem number
        String problemNum = br.readLine();

        bw.write(problemNum);
        bw.newLine();

        // Process each input string until "end" is encountered
        while (!(line = br.readLine()).equals("end")) {
            boolean result = isAccepted(line);
            bw.write(result ? "accepted" : "not accepted");
            bw.newLine();
        }

        // Write the problem separator
        bw.write("x");
        bw.newLine();
    }
}



// Problem 1: Language { a^n b^m c^n | n,m>=0 }
class PDAProblem1 {
    ArrayList<Integer> states = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
    ArrayList<Integer> finalStates = new ArrayList<>(Arrays.asList(3));
    ArrayList<Character> inputAlphabet = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
    ArrayList<Character> stackAlphabet = new ArrayList<>(Arrays.asList('$', 'a'));
    int startState = 0;
    Character stackInitial = '$';
    TransitionFunction transitionFunction = new TransitionFunction();
    PDAClass pda;

    public PDAProblem1(BufferedReader br, BufferedWriter bw) throws IOException {
        // State 0: Push 'a' onto stack for each 'a' read
        transitionFunction.addTransition(0, 'a', '$', 0, "a$");
        transitionFunction.addTransition(0, 'a', 'a', 0, "aa");

        // Transition to state 1 on reading 'b'
        transitionFunction.addTransition(0, 'b', '$', 1, "$");
        transitionFunction.addTransition(0, 'b', 'a', 1, "a");

        // State 1: Skip 'b's
        transitionFunction.addTransition(1, 'b', '$', 1, "$");
        transitionFunction.addTransition(1, 'b', 'a', 1, "a");

        // Transition to state 2 on reading 'c'
        transitionFunction.addTransition(1, 'c', '$', 2, "$");
        transitionFunction.addTransition(1, 'c', 'a', 2, "");

        // State 2: Pop 'a' for each 'c' read
        transitionFunction.addTransition(2, 'c', 'a', 2, "");

        // Transition to state 3 when stack is empty
        transitionFunction.addTransition(2, 'ε', '$', 3, "$");

        // Special case for empty string
        transitionFunction.addTransition(0, 'ε', '$', 3, "$");

        pda = new PDAClass(states, inputAlphabet, stackAlphabet, transitionFunction, startState, finalStates, stackInitial);
        pda.solveProblem(br, bw);
    }
}

// Problem 2: Language {a^(3n) b^(2n) | n>=1}
class PDAProblem2 {
    ArrayList<Integer> states = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
    ArrayList<Integer> finalStates = new ArrayList<>(Arrays.asList(4));
    ArrayList<Character> inputAlphabet = new ArrayList<>(Arrays.asList('a', 'b'));
    ArrayList<Character> stackAlphabet = new ArrayList<>(Arrays.asList('$', 'X'));
    int startState = 0;
    Character stackInitial = '$';
    TransitionFunction transitionFunction = new TransitionFunction();
    PDAClass pda;

    public PDAProblem2(BufferedReader br, BufferedWriter bw) throws IOException {
        // State 0: Push 'X' onto stack for every 3 'a's read
        transitionFunction.addTransition(0, 'a', '$', 1, "$");
        transitionFunction.addTransition(1, 'a', '$', 2, "$");
        transitionFunction.addTransition(2, 'a', '$', 0, "X$");

        transitionFunction.addTransition(0, 'a', 'X', 1, "X");
        transitionFunction.addTransition(1, 'a', 'X', 2, "X");
        transitionFunction.addTransition(2, 'a', 'X', 0, "XX");

        // State 0: Transition to state 3 on reading 'b'
        transitionFunction.addTransition(0, 'b', 'X', 3, "");

        // State 3: Pop 'X' for every 2 'b's read
        transitionFunction.addTransition(3, 'b', 'X', 3, "X");
        transitionFunction.addTransition(3, 'b', 'X', 3, "");

        // Transition to state 4 when stack is empty (except for '$')
        transitionFunction.addTransition(3, 'ε', '$', 4, "$");

        pda = new PDAClass(states, inputAlphabet, stackAlphabet, transitionFunction, startState, finalStates, stackInitial);
        pda.solveProblem(br, bw);
    }
}

