# CSC400MOD4 – Stacks: Infix Expression Calculator

A stack-based calculator for infix integer expressions. It converts the expression to postfix with the shunting-yard method, then evaluates the postfix. Stack ADT examples (array stack, linked stack, delimiter matching) are included alongside it.

## Overview

This is **CSU Global CSC400, Module 4**. It is an Eclipse Java project with two packages under `src/`:
- `module4`: the calculator
- `examples`: stack examples in the style of Goodrich, Tamassia and Goldwasser, *Data Structures and Algorithms in Java* (the `Stack` interface carries their `@author` tags)

## `module4/InfixCalculator.java`

The calculator handles `+ - * / %`, parentheses and **multi-digit non-negative integers**. All arithmetic is integer arithmetic, and parentheses don't imply multiplication. It uses `java.util.Stack`.

| Method | What it does |
|---|---|
| `precedence(char)` | `+ -` → 1; `* / %` → 2 |
| `isOperator(char)` | Recognizes the five operators |
| `infixToPostfix(String)` | **Shunting-yard conversion.** It scans character by character, skipping whitespace and collecting runs of digits into numbers. `(` is pushed. `)` pops operators until the matching `(`, and throws "Unbalanced parentheses" if there isn't one. An operator first pops any operators of higher or equal precedence (so operators are left-associative), then is pushed. Any other character throws "Invalid character". At the end it pops the remaining operators, and a leftover `(` means the parentheses are unbalanced. |
| `evaluatePostfix(String)` | Tokenizes with `StringTokenizer`. It pushes operands, and for each operator pops `b` then `a` and pushes `a op b`. It checks for too few operands, division or modulo by zero, and leftover operands. |
| `evaluateInfix(String)` | The public entry point. It prints the postfix form and returns the result. **Any exception prints "Error: Invalid infix expression" and returns 0.** |

`main` runs seven test expressions (verified output):

| # | Infix | Postfix | Result |
|---|---|---|---|
| 1 | `(4+2)*3` | `4 2 + 3 *` | 18 |
| 2 | `5+(3*7)` | `5 3 7 * +` | 26 |
| 3 | `4+2*3` | `4 2 3 * +` | 10 |
| 4 | `17%5+10*2` | `17 5 % 10 2 * +` | 22 |
| 5 | `(4+2*3` | – (unbalanced) | 0, with an error message |
| 6 | `4+2$3` | – (invalid character) | 0, with an error message |
| 7 | `(4+200+5)*3/(25-3)+(8+(43/(2+5)))` | `4 200 + 5 + 3 * 25 3 - / 8 43 2 5 + / + +` | 42 |

## `examples` – stack ADT

| File | Description |
|---|---|
| `Stack<E>` | The LIFO interface: `size`, `isEmpty`, `push`, `top`, `pop` |
| `SinglyLinkedList<E>` | A singly linked list with a nested `Node` class, head and tail references, `addFirst`, `addLast` and `removeFirst` |
| `LinkedStack<E>` | A stack built on `SinglyLinkedList` (adapter pattern): push = `addFirst` and pop = `removeFirst`, both O(1) |
| `ArrayStack<E>` | An array-based stack with a top index `t` and a default capacity of 1000 |
| `Analyzer` | `isMatched(expr)` checks `() {} []` delimiter matching with a stack. `isHTMLMatched(html)` checks opening and closing HTML tags the same way. Both use `LinkedStack`. |

### Known issue
In `ArrayStack(int capacity)`, the constructor declares a **local** `E[] data` that hides the field, so the field `data` stays `null`. As a result, `push()` throws a `NullPointerException`. Writing `data = (E[]) new Object[capacity];` would assign the field instead. `LinkedStack` and `Analyzer` aren't affected.

## Build and run

JDK 8 or newer:

```bash
mkdir -p bin
javac -d bin src/module4/*.java src/examples/*.java
java -cp bin module4.InfixCalculator
```

## Concepts demonstrated
- The stack ADT, implemented with an array and with a linked list
- Converting infix to postfix (shunting-yard) and evaluating postfix with a stack
- Operator precedence and left associativity
- Delimiter and HTML-tag matching with a stack
- Validating input and reporting errors through exceptions
