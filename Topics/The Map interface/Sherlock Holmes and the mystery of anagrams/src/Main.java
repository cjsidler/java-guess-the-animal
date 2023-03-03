import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[] word1 = scanner.nextLine().toLowerCase().toCharArray();
        char[] word2 = scanner.nextLine().toLowerCase().toCharArray();

        Map<Character, Integer> counts = new HashMap<>();

        for (char letter : word1) {
            Integer count = counts.getOrDefault(letter, 0);
            counts.put(letter, count + 1);
        }

        for (char letter : word2) {
            Integer count = counts.getOrDefault(letter, 0);
            if (count == 0) {
                System.out.println("no");
                return;
            }
            counts.put(letter, count - 1);
        }

        for (var entry : counts.entrySet()) {
            if (entry.getValue() != 0) {
                System.out.println("no");
                return;
            }
        }

        System.out.println("yes");
    }
}