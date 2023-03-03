import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime dateTime = LocalDateTime.parse(scanner.next());
        int daysToAdd = scanner.nextInt();
        int hoursToSubtract = scanner.nextInt();
        System.out.println(dateTime.plusDays(daysToAdd).minusHours(hoursToSubtract));
    }
}