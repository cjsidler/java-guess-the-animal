import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime ldt = LocalDateTime.parse(scanner.next());
        LocalDateTime beginningOfYear = LocalDateTime.of(ldt.getYear(), 1, 1, 0, 0);
        Duration duration = Duration.between(beginningOfYear, ldt);
        System.out.println(duration.toHours());
    }
}