import java.util.*;

class BankCard {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        String numbers = scn.nextLine();
        String card = numbers.replaceAll("\\s", "");

        String visaRegex = "4[0-9]{15}"; // put your code here

        String masterCardRegex = "((5[1-5])[0-9]{14})|(" +
                "(222[1-9][0-9]{12})" +
                "|" +
                "(22[3-9][0-9][0-9]{12})" +
                "|" +
                "(2[3-6][0-9][0-9][0-9]{12})" +
                "|" +
                "(27[0-1][0-9][0-9]{12})" +
                "|" +
                "(2720[0-9]{12})" +
                ")"; // put your code here

        String americanExpressRegex = "(34|37)[0-9]{13}"; // put your code here

        if (card.matches(visaRegex)) {
            System.out.println("Visa");
        } else if (card.matches(masterCardRegex)) {
            System.out.println("MasterCard");
        } else if (card.matches(americanExpressRegex)) {
            System.out.println("AmericanExpress");
        } else {
            System.out.println("Card number does not exist");
        }
    }
}