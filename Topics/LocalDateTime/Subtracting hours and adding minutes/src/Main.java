import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        LocalDateTime ldt = LocalDateTime.parse(scanner.next());
        int hoursToSubtract = scanner.nextInt();
        int minutesToAdd = scanner.nextInt();

        System.out.println(ldt.minusHours(hoursToSubtract).plusMinutes(minutesToAdd));
    }
}