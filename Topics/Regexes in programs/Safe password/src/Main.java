import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        String date = scn.nextLine();
        String pattern = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{12,}";

        if (date.matches(pattern)) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}