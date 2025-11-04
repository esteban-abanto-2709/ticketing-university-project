package ticketing.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputValidator {

    private static final Scanner scanner = new Scanner(System.in);

    // Pide un entero en un rango específico
    public static int getIntInRange(String message, int min, int max) {
        int value;
        while (true) {
            System.out.print(message);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    break;
                }
                System.out.println("El número debe estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
        return value;
    }

    public static int getIntMin(String message, int min) {
        int value;
        while (true) {
            System.out.print(message);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value >= min) {
                    break;
                }
                System.out.println("El número debe ser mayor o igual a " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
        return value;
    }

    // Pide un string no vacío
    public static String getNonEmptyString(String message) {
        String value;
        while (true) {
            System.out.print(message);
            value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                break;
            }
            System.out.println("Este campo no puede estar vacío.");
        }
        return value;
    }

    public static String getOptionalString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    // Pide confirmación S/N
    public static boolean getConfirmation(String message) {
        String response;
        while (true) {
            System.out.print(message + " (S/N): ");
            response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("S") || response.equals("SI")) {
                return true;
            } else if (response.equals("N") || response.equals("NO")) {
                return false;
            }
            System.out.println("Responda con S (Sí) o N (No).");
        }
    }

    // Pide un código con formato específico (letras y números)
    public static String getCode(String message) {
        String code;
        while (true) {
            code = getNonEmptyString(message);
            if (code.matches("[A-Za-z0-9]+")) {
                break;
            }
            System.out.println("El código solo debe contener letras y números.");
        }
        return code.toUpperCase();
    }

    // Pausa hasta que el usuario presione Enter
    public static void pressEnterToContinue() {
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    public static LocalDate getDate(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = null;

        while (fecha == null) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                fecha = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Use dd/MM/yyyy (ejemplo: 25/12/2024)");
            }
        }

        return fecha;
    }

    public static LocalTime getTime(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime hora = null;

        while (hora == null) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                hora = LocalTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de hora inválido. Use HH:mm (ejemplo: 20:30)");
            }
        }

        return hora;
    }
}
