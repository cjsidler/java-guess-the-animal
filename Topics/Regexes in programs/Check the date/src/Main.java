import java.util.*;

class Date {

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        String date = scn.nextLine();

        String dateRegex = "((([19][0-9][0-9][0-9])|([20][0-9][0-9][0-9]))" +
                "[-./]" +
                "[01][0-2]?" +
                "[-./]" +
                "[0-3][0-9]?)" +
                "|" +
                "([0-3][0-9]?" +
                "[-./]" +
                "[01][0-2]?" +
                "[-./]" +
                "(([19][0-9][0-9][0-9])|([20][0-9][0-9][0-9])))";

        if (date.matches(dateRegex)) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
    }
}