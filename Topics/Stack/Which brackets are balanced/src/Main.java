import java.util.*;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        String openingBrackets = "[({";
        Map<Character, Character> brackets = Map.of(']', '[', '}', '{', ')', '(');

        Deque<Character> deque = new ArrayDeque<>();

        for (int i = 0; i < input.length(); i++) {
            char curChar = input.charAt(i);

            if (openingBrackets.indexOf(curChar) >= 0) {
                deque.push(curChar);
            } else if (!deque.isEmpty()) {
                if (deque.peek() == brackets.get(curChar)) {
                    deque.pop();
                } else {
                    System.out.println("false");
                    return;
                }
            } else {
                System.out.println("false");
                return;
            }
        }

        if (deque.isEmpty()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}