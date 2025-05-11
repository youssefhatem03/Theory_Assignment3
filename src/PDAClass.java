import java.io.*;
import java.util.*;

class TransitionKey {
    private int currentState;
    private char input;
    private char stackTop;

    public TransitionKey(int currentState, char input, char stackTop) {
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

    public TransitionValue(int nextState, String stackPush) {
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
    private Map<TransitionKey, List<TransitionValue>> transitions = new HashMap<>();

    public void addTransition(int currentState, char input, char stackTop,
                              int nextState, String stackPush) {
        TransitionKey key = new TransitionKey(currentState, input, stackTop);
        transitions.computeIfAbsent(key, k -> new ArrayList<>()).add(new TransitionValue(nextState, stackPush));
    }

    public List<TransitionValue> getTransitions(int currentState, char input, char stackTop) {
        return transitions.getOrDefault(new TransitionKey(currentState, input, stackTop), new ArrayList<>());
    }
}

class PDAState {
    private int state;
    private Stack<Character> stack;
    private int position;

    public PDAState(int state, Stack<Character> stack, int position) {
        this.state = state;
        this.stack = new Stack<>();
        this.stack.addAll(stack);
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PDAState pdaState = (PDAState) o;
        return state == pdaState.state &&
                position == pdaState.position &&
                stack.equals(pdaState.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, stack, position);
    }

    public int getState() { return state; }
    public Stack<Character> getStack() { return stack; }
    public int getPosition() { return position; }
}

public class PDAClass {
    private ArrayList<Integer> states;
    private ArrayList<Character> inputAlphabet;
    private ArrayList<Character> stackAlphabet;
    private TransitionFunction transitionFunction;
    private int startState;
    private ArrayList<Integer> finalStates;
    private char stackInitial;

    public PDAClass(ArrayList<Integer> states, ArrayList<Character> inputAlphabet,
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

    public boolean isAccepted(String input) {
        Stack<Character> stack = new Stack<>();
        stack.push(stackInitial);

        Queue<PDAState> queue = new LinkedList<>();
        Set<PDAState> visited = new HashSet<>();
        queue.add(new PDAState(startState, stack, 0));

        while (!queue.isEmpty()) {
            PDAState current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            int currentState = current.getState();
            Stack<Character> currentStack = current.getStack();
            int pos = current.getPosition();

            // Check acceptance by final state
            if (pos == input.length() && finalStates.contains(currentState)) {
                return true;
            }

            // Process transitions
            processTransitions(currentState, currentStack, pos, input, queue, visited);
        }
        return false;
    }

    private void processTransitions(int currentState, Stack<Character> stack, int pos,
                                    String input, Queue<PDAState> queue, Set<PDAState> visited) {
        char stackTop = stack.isEmpty() ? 'e' : stack.peek();
        char inputSymbol = (pos < input.length()) ? input.charAt(pos) : 'e';

        // Process input transitions
        if (pos < input.length()) {
            processTransition(currentState, inputSymbol, stack, pos + 1, queue, visited);
        }

        // Process epsilon transitions
        processTransition(currentState, 'e', stack, pos, queue, visited);
    }

    private void processTransition(int currentState, char inputSymbol, Stack<Character> stack,
                                   int newPos, Queue<PDAState> queue, Set<PDAState> visited) {
        char stackTop = stack.isEmpty() ? 'e' : stack.peek();
        List<TransitionValue> transitions = transitionFunction.getTransitions(currentState, inputSymbol, stackTop);

        for (TransitionValue transition : transitions) {
            Stack<Character> newStack = new Stack<>();
            newStack.addAll(stack);

            if (stackTop != 'e' && !newStack.isEmpty()) {
                newStack.pop();
            }

            String pushStr = transition.getStackPush();
            for (int i = pushStr.length() - 1; i >= 0; i--) {
                char c = pushStr.charAt(i);
                if (c != 'e') newStack.push(c);
            }

            PDAState nextState = new PDAState(transition.getNextState(), newStack, newPos);
            if (!visited.contains(nextState)) {
                queue.add(nextState);
            }
        }
    }

    public void solveProblem(BufferedReader br, BufferedWriter bw) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.equals("end")) {
            boolean result = isAccepted(line.trim());
            bw.write(result ? "accepted\n" : "not accepted\n");
        }
        bw.write("x\n");
    }
}



// Problem 1: {a^n b^m c^n | n,m >= 0}
class PDAProblem1 {
    public PDAProblem1(BufferedReader br, BufferedWriter bw) throws IOException {
        ArrayList<Integer> states = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
        ArrayList<Character> inputAlpha = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
        ArrayList<Character> stackAlpha = new ArrayList<>(Arrays.asList('$', 'A'));
        ArrayList<Integer> finalStates = new ArrayList<>(Arrays.asList(3));
        TransitionFunction tf = new TransitionFunction();

        tf.addTransition(0, 'a', '$', 0, "A$");
        tf.addTransition(0, 'a', 'A', 0, "AA");
        tf.addTransition(0, 'e', '$', 1, "$");
        tf.addTransition(0, 'e', 'A', 1, "A");
        tf.addTransition(1, 'b', '$', 1, "$");
        tf.addTransition(1, 'b', 'A', 1, "A");
        tf.addTransition(1, 'c', 'A', 2, "");
        tf.addTransition(2, 'c', 'A', 2, "");
        tf.addTransition(2, 'e', '$', 3, "$");

        PDAClass pda = new PDAClass(states, inputAlpha, stackAlpha, tf, 0, finalStates, '$');
        pda.solveProblem(br, bw);
    }
}

// Problem 2: {a^{3n} b^{2n} | n >= 1}
class PDAProblem2 {
    public PDAProblem2(BufferedReader br, BufferedWriter bw) throws IOException {
        ArrayList<Integer> states = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        ArrayList<Character> inputAlpha = new ArrayList<>(Arrays.asList('a', 'b'));
        ArrayList<Character> stackAlpha = new ArrayList<>(Arrays.asList('$', 'X'));
        ArrayList<Integer> finalStates = new ArrayList<>(Arrays.asList(4));
        TransitionFunction tf = new TransitionFunction();

        // State 0: Count a's in groups of three, pushing one X for each group
        // First a in a group
        tf.addTransition(0, 'a', '$', 1, "$");   // First a with empty stack
        tf.addTransition(0, 'a', 'X', 1, "X");   // First a with X on stack

        // Second a in a group
        tf.addTransition(1, 'a', '$', 2, "$");   // Second a with empty stack
        tf.addTransition(1, 'a', 'X', 2, "X");   // Second a with X on stack

        // Third a in a group - push X onto stack to mark completion of a group of 3 a's
        tf.addTransition(2, 'a', '$', 0, "X$");  // Third a with empty stack, push X
        tf.addTransition(2, 'a', 'X', 0, "XX");  // Third a with X on stack, push X

        // Transition to state 3 to process b's if there's at least one X on stack
        tf.addTransition(0, 'b', 'X', 3, "X");   // Start processing b's

        // State 3: Process b's, popping X for every two b's
        tf.addTransition(3, 'b', 'X', 0, "");    // Second b, pop X

        // Accept when the stack is back to just $ (meaning we've matched all a's with b's)
        tf.addTransition(0, 'e', '$', 4, "$");   // Accept if stack has only $

        PDAClass pda = new PDAClass(states, inputAlpha, stackAlpha, tf, 0, finalStates, '$');
        pda.solveProblem(br, bw);
    }
}

// Problem 3: Balanced brackets
class PDAProblem3 {
    public PDAProblem3(BufferedReader br, BufferedWriter bw) throws IOException {
        ArrayList<Integer> states = new ArrayList<>(Arrays.asList(0, 1));
        ArrayList<Character> inputAlpha = new ArrayList<>(Arrays.asList('{', '}', ' '));
        ArrayList<Character> stackAlpha = new ArrayList<>(Arrays.asList('$', '{'));
        ArrayList<Integer> finalStates = new ArrayList<>(Arrays.asList(1));
        TransitionFunction tf = new TransitionFunction();

        // Push { onto stack when encountered
        tf.addTransition(0, '{', '$', 0, "{$");  // First open bracket with empty stack
        tf.addTransition(0, '{', '{', 0, "{{");  // Push { onto stack

        // Pop from stack when matching } is encountered
        tf.addTransition(0, '}', '{', 0, "");    // Pop { when matching } is found

        // Handle spaces - just remain in the same state
        tf.addTransition(0, ' ', '$', 0, "$");   // Space with empty stack
        tf.addTransition(0, ' ', '{', 0, "{");   // Space with { on stack

        // Accept when stack has only $ (all brackets matched) and all input is consumed
        tf.addTransition(0, 'e', '$', 1, "$");   // Accept when stack is empty except for $

        PDAClass pda = new PDAClass(states, inputAlpha, stackAlpha, tf, 0, finalStates, '$');
        pda.solveProblem(br, bw);
    }
}

// Problem 4: {a^n b^{n+m} c^m | n,m >= 1}
class PDAProblem4 {
    public PDAProblem4(BufferedReader br, BufferedWriter bw) throws IOException {
        ArrayList<Integer> states = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
        ArrayList<Character> inputAlpha = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
        ArrayList<Character> stackAlpha = new ArrayList<>(Arrays.asList('$', 'A', 'B'));
        ArrayList<Integer> finalStates = new ArrayList<>(Arrays.asList(4));
        TransitionFunction tf = new TransitionFunction();

        tf.addTransition(0, 'a', '$', 0, "A$");
        tf.addTransition(0, 'a', 'A', 0, "AA");
        tf.addTransition(0, 'e', 'A', 1, "A");
        tf.addTransition(1, 'b', 'A', 1, "");
        tf.addTransition(1, 'e', '$', 2, "$");
        tf.addTransition(2, 'b', '$', 2, "B$");
        tf.addTransition(2, 'b', 'B', 2, "BB");
        tf.addTransition(2, 'e', 'B', 3, "B");
        tf.addTransition(3, 'c', 'B', 3, "");
        tf.addTransition(3, 'e', '$', 4, "$");

        PDAClass pda = new PDAClass(states, inputAlpha, stackAlpha, tf, 0, finalStates, '$');
        pda.solveProblem(br, bw);
    }
}

// Problem 5: {Wc^k | W ∈ {a,b}*, k = number of b's in W}
class PDAProblem5 {
    public PDAProblem5(BufferedReader br, BufferedWriter bw) throws IOException {
        ArrayList<Integer> states = new ArrayList<>(Arrays.asList(0, 1, 2));
        ArrayList<Character> inputAlpha = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
        ArrayList<Character> stackAlpha = new ArrayList<>(Arrays.asList('$', 'B'));
        ArrayList<Integer> finalStates = new ArrayList<>(Arrays.asList(2));
        TransitionFunction tf = new TransitionFunction();

        tf.addTransition(0, 'a', '$', 0, "$");
        tf.addTransition(0, 'a', 'B', 0, "B");
        tf.addTransition(0, 'b', '$', 0, "B$");
        tf.addTransition(0, 'b', 'B', 0, "BB");
        tf.addTransition(0, 'c', 'B', 1, "");
        tf.addTransition(1, 'c', 'B', 1, "");
        tf.addTransition(0, 'e', '$', 2, "$");
        tf.addTransition(1, 'e', '$', 2, "$");

        PDAClass pda = new PDAClass(states, inputAlpha, stackAlpha, tf, 0, finalStates, '$');
        pda.solveProblem(br, bw);
    }
}
