package module4;

import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Infix expression evaluator using Java Stack ADT.
 * Converts infix to postfix then evaluates the postfix.
 * Supports +, -, *, /, %, (, ), and integers. (integer division only)
 * Parentheses do not imply multiplication.
 * Detects unbalanced parentheses and invalid tokens.
 */
public class InfixCalculator {

    // Operator precedence (higher number = higher precedence)
    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            default:
                return -1;
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || 
        		c == '-' || 
        		c == '*' || 
        		c == '/' || 
        		c == '%';
    }

    /**
     * Convert infix expression to postfix using a stack for operators.
     */
    private String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> opStack = new Stack<>();

        // Tokenize to support multi-digit numbers and no spaces
        int i = 0;
        while (i < infix.length()) {
            char c = infix.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Multi-digit number
            if (Character.isDigit(c)) {
                StringBuilder num = new StringBuilder();
                while (i < infix.length() && Character.isDigit(infix.charAt(i))) {
                    num.append(infix.charAt(i));
                    i++;
                }
                postfix.append(num).append(' ');
                continue;
            }

            // Left parenthesis
            if (c == '(') {
                opStack.push(c);
                i++;
                continue;
            }

            // Right parenthesis
            if (c == ')') {
                while (!opStack.isEmpty() && opStack.peek() != '(') {
                    postfix.append(opStack.pop()).append(' ');
                }
                if (opStack.isEmpty() || opStack.pop() != '(') {
                    throw new IllegalArgumentException("Unbalanced parentheses");
                }
                i++;
                continue;
            }

            // Operator
            if (isOperator(c)) {
                while (!opStack.isEmpty() && opStack.peek() != '('
                        && precedence(opStack.peek()) >= precedence(c)) {
                    postfix.append(opStack.pop()).append(' ');
                }
                opStack.push(c);
                i++;
                continue;
            }

            // Invalid character
            throw new IllegalArgumentException("Invalid character: " + c);
        }

        // Pop remaining operators
        while (!opStack.isEmpty()) {
            char op = opStack.pop();
            if (op == '(') {
                throw new IllegalArgumentException("Unbalanced parentheses");
            }
            postfix.append(op).append(' ');
        }

        return postfix.toString().trim();
    }

    /**
     * Evaluate a postfix expression using a stack of operands.
     */
    private int evaluatePostfix(String postfix) {
        Stack<Integer> stack = new Stack<>();
        StringTokenizer st = new StringTokenizer(postfix);

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (token.matches("\\d+")) {          // operand
                stack.push(Integer.parseInt(token));
            } else if (token.length() == 1 && isOperator(token.charAt(0))) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression: insufficient operands");
                }
                int b = stack.pop();
                int a = stack.pop();
                char op = token.charAt(0);

                switch (op) {
                    case '+':
                        stack.push(a + b);
                        break;
                    case '-':
                        stack.push(a - b);
                        break;
                    case '*':
                        stack.push(a * b);
                        break;
                    case '/':
                        if (b == 0) throw new ArithmeticException("Division by zero");
                        stack.push(a / b);
                        break;
                    case '%':
                        if (b == 0) throw new ArithmeticException("Modulo by zero");
                        stack.push(a % b);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operator: " + op);
                }
            } else {
                throw new IllegalArgumentException("Invalid token in postfix: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: leftover operands");
        }
        return stack.pop();
    }

    /**
     * Returns the integer result or zero and prints an error message for invalid input.
     */
    public int evaluateInfix(String infixExpression) {
        try {
            if (infixExpression == null || infixExpression.trim().isEmpty()) {
                System.out.println("Error: Invalid infix expression");
                return 0;
            }
            String postfix = infixToPostfix(infixExpression);
            System.out.println("Postfix Expression: " + postfix);
            return evaluatePostfix(postfix);
        } catch (Exception e) {
            System.out.println("Error: Invalid infix expression");
            return 0;
        }
    }

    public static void main(String[] args) {
        InfixCalculator calculator = new InfixCalculator();

        // Test 1: multi-digit + parentheses (solution: 18)
        String expression1 = "(4+2)*3";
        System.out.println("Expression 1: " + expression1);
        System.out.println("Result 1: " + calculator.evaluateInfix(expression1));
        System.out.println();

        // Test 2: multi-digit + nested (solution: 26)
        String expression2 = "5+(3*7)";
        System.out.println("Expression 2: " + expression2);
        System.out.println("Result 2: " + calculator.evaluateInfix(expression2));
        System.out.println();

        // Test 3: precedence without full parentheses (solution: 10)
        String expression3 = "4+2*3"; 
        System.out.println("Expression 3: " + expression3);
        System.out.println("Result 3: " + calculator.evaluateInfix(expression3));
        System.out.println();

        // Test 4: modulo and multi-digit (solution: 22)
        String expression4 = "17%5+10*2";
        System.out.println("Expression 4: " + expression4);
        System.out.println("Result 4: " + calculator.evaluateInfix(expression4));
        System.out.println();

        // Test 5: invalid - unbalanced parentheses (solution: 0)
        String expression5 = "(4+2*3";
        System.out.println("Expression 5: " + expression5);
        System.out.println("Result 5: " + calculator.evaluateInfix(expression5));
        System.out.println();

        // Test 6: invalid character (solution: 0)
        String expression6 = "4+2$3";
        System.out.println("Expression 6: " + expression6);
        System.out.println("Result 6: " + calculator.evaluateInfix(expression6));
        System.out.println();
        
        // Test 7: long infix equation with nested parenthesis (solution: 42)
        String expression7 = "(4+200+5)*3/(25-3)+(8+(43/(2+5)))";
        System.out.println("Expression 7: " + expression7);
        System.out.println("Result 7: " + calculator.evaluateInfix(expression7));
        System.out.println();
    }
}