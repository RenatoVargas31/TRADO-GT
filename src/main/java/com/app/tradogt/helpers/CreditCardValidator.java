package com.app.tradogt.helpers;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreditCardValidator {
    // Método para validar el número de tarjeta
    public static boolean isValidCardNumber(String cardNumber, String cardType) {

        switch (cardType) {
            case "Visa":
                // Eliminar espacios del número de la tarjeta
                String cleanedCardNumber = cardNumber.replaceAll("\\s", "");

                if (cleanedCardNumber.length() == 16) {
                    System.out.println("el numero tiene 16 digitos");
                    if (cleanedCardNumber.startsWith("4")) {
                        System.out.println("el numero inicia con 4");
                        if (luhnCheck(cleanedCardNumber)) {
                            System.out.println("el numero cumple con Luhm");
                            return true;
                        } else {
                            System.out.println("Error en Luhm");
                            return false;
                        }
                    } else {
                        System.out.println("Error en iniciar con 4");
                        return false;
                    }
                } else {
                    System.out.println("Error en longitud igual a 16");
                    return false;
                }
            case "MasterCard":
                // Eliminar espacios del número de la tarjeta
                cleanedCardNumber = cardNumber.replaceAll("\\s", "");

                if (cleanedCardNumber.length() == 16) {
                    System.out.println("el numero tiene 16 digitos");
                    if (cleanedCardNumber.startsWith("51") || cleanedCardNumber.startsWith("52") ||
                            cleanedCardNumber.startsWith("53") || cleanedCardNumber.startsWith("54") ||
                            cleanedCardNumber.startsWith("55") ||
                            (Integer.parseInt(cleanedCardNumber.substring(0, 4)) >= 2221 && Integer.parseInt(cleanedCardNumber.substring(0, 4)) <= 2720)) {
                        System.out.println("el numero inicia con 51-55 o en el rango 2221-2720");
                        if (luhnCheck(cleanedCardNumber)) {
                            System.out.println("el numero cumple con Luhm");
                            return true;
                        } else {
                            System.out.println("Error en Luhm");
                            return false;
                        }
                    } else {
                        System.out.println("Error en iniciar con 51-55 o fuera del rango 2221-2720");
                        return false;
                    }
                } else {
                    System.out.println("Error en longitud igual a 16");
                    return false;
                }
            case "American Express":
                // Eliminar espacios del número de la tarjeta
                cleanedCardNumber = cardNumber.replaceAll("\\s", "");

                if (cleanedCardNumber.length() == 15) {
                    System.out.println("el numero tiene 15 digitos");
                    if (cleanedCardNumber.startsWith("34") || cleanedCardNumber.startsWith("37")) {
                        System.out.println("el numero inicia con 34 o 37");
                        if (luhnCheck(cleanedCardNumber)) {
                            System.out.println("el numero cumple con Luhm");
                            return true;
                        } else {
                            System.out.println("Error en Luhm");
                            return false;
                        }
                    } else {
                        System.out.println("Error en iniciar con 34 o 37");
                        return false;
                    }
                } else {
                    System.out.println("Error en longitud igual a 15");
                    return false;
                }
            default:
                System.out.println("Tipo de tarjeta no reconocido");
                return false;
        }


    }

    public static boolean isValidExpiryDate(String expiryDate) {
        System.out.println("Verificando la fecha de vencimiento: " + expiryDate);

        // Verificar el formato de la fecha
        if (expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            System.out.println("El formato de la fecha de vencimiento es correcto (MM/YY).");

            // Obtener el año y mes actual
            YearMonth currentYearMonth = YearMonth.now();

            try {
                // Parsear la fecha de vencimiento ingresada
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
                YearMonth expiryYearMonth = YearMonth.parse(expiryDate, formatter);

                // Verificar si la fecha de vencimiento es mayor o igual a la fecha actual
                if (expiryYearMonth.isAfter(currentYearMonth) || expiryYearMonth.equals(currentYearMonth)) {
                    System.out.println("La fecha de vencimiento es válida y mayor o igual al mes y año actuales.");
                    return true;
                } else {
                    System.out.println("La fecha de vencimiento es menor al mes y año actuales.");
                    return false;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error al parsear la fecha de vencimiento: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("El formato de la fecha de vencimiento es incorrecto.");
            return false;
        }
    }


    // Implementación del Algoritmo de Luhn
    public static boolean luhnCheck(String cardNumber) {
        int nDigits = cardNumber.length();
        int nSum = 0;
        boolean isSecond = false;

        for (int i = nDigits - 1; i >= 0; i--) {
            int d = cardNumber.charAt(i) - '0';
            if (isSecond) {
                d *= 2;
                System.out.println("Multiplicado por 2: " + d);
            }
            nSum += d / 10;
            nSum += d % 10;
            System.out.println("Suma parcial: " + nSum);
            isSecond = !isSecond;
            System.out.println("Luhm no me agradas :'v " + (nSum % 10));
        }
        boolean result = (nSum % 10 == 0);
        return result;
    }


}
