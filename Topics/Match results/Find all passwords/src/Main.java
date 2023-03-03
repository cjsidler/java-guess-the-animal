import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        Pattern pattern = Pattern.compile("(password:?\\s*)([a-zA-Z0-9]+)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            System.out.println("No passwords found.");
        } else {
            matcher.reset();
            while (matcher.find()) {
                System.out.println(matcher.group(2));
            }
        }
    }
}