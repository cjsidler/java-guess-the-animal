import java.util.Scanner;

public class Main {

    /* Fix this method */
    public static void printReverseCharByChar(String s) {
        if (s.length() > 0) {
            printReverseCharByChar(s.substring(1));
            System.out.print(s.charAt(0));
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        printReverseCharByChar(scanner.nextLine());
    }
}