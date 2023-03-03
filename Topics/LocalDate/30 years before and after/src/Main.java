import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String dateString = scanner.next();

        LocalDate date = LocalDate.parse(dateString);

        System.out.println(date.withYear(date.getYear() - 30));
        System.out.println(date.withYear(date.getYear() + 30));
    }
}