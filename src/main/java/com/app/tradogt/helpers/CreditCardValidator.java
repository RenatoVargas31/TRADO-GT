package com.app.tradogt.helpers;

public class CreditCardValidator {
    // Método para validar el número de tarjeta
    public static boolean isValidCardNumber(String cardNumber, String cardType) {

        System.out.println("Holaa :'v");

        switch (cardType) {
            case "Visa":

                System.out.println("Holaa :'v visa");
                return cardNumber.startsWith("4") && cardNumber.length() == 16 && luhnCheck(cardNumber);
            case "MasterCard":

                System.out.println("Holaa :'v MasterCard" );
                return (cardNumber.startsWith("51") || cardNumber.startsWith("52") || cardNumber.startsWith("53") || cardNumber.startsWith("54") || cardNumber.startsWith("55") ||
                        (Integer.parseInt(cardNumber.substring(0, 4)) >= 2221 && Integer.parseInt(cardNumber.substring(0, 4)) <= 2720))
                        && cardNumber.length() == 16 && luhnCheck(cardNumber);
            case "American Express":

                System.out.println("Holaa :'v America");
                return (cardNumber.startsWith("34") || cardNumber.startsWith("37")) && cardNumber.length() == 15 && luhnCheck(cardNumber);
            default:
                return false;
        }
    }

    public static boolean isValidExpiryDate(String expiryDate) {
        return expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}"); // Formato MM/YY
    }


    // Método para validar el CVV
    public static boolean isValidCVV(String cvv, String cardType) {
        switch (cardType) {
            case "Visa":
            case "MasterCard":
                return cvv.matches("\\d{3}"); // 3 dígitos
            case "American Express":
                return cvv.matches("\\d{4}"); // 4 dígitos
            default:
                return false;
        }
    }


    // Implementación del Algoritmo de Luhn
    public static boolean luhnCheck(String cardNumber) {
        int nDigits = cardNumber.length();
        int nSum = 0;
        boolean isSecond = false;

        System.out.println("Luhm no me agradas :'v");
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = cardNumber.charAt(i) - '0';
            if (isSecond)
                d *= 2;
            nSum += d / 10;
            nSum += d % 10;
            isSecond = !isSecond;
            System.out.println("Luhm no me agradas :'v " + nSum % 10 );
        }
        return (nSum % 10 == 0);
    }

}
