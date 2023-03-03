import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime dt = LocalDateTime.parse(scanner.next());
        System.out.println(dt.plusHours(11).toLocalDate());
    }
}