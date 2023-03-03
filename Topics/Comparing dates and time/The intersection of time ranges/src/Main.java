import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalTime lt1Start = LocalTime.parse(scanner.next());
        LocalTime lt1End = LocalTime.parse(scanner.next());
        LocalTime lt2Start = LocalTime.parse(scanner.next());
        LocalTime lt2End = LocalTime.parse(scanner.next());

        if (lt1Start.equals(lt2Start)) {
            System.out.println("true");
        } else if (lt1Start.isBefore(lt2Start)) {
            System.out.println(lt1End.isAfter(lt2Start) || lt1End.equals(lt2Start));
        } else {
            System.out.println(lt2End.isAfter(lt1Start) || lt2End.equals(lt1Start));
        }
    }
}