package ticketing;

import java.util.Scanner;

import ticketing.view.LocalView;
import ticketing.utils.ConsoleFormatter;

public class Ticketing {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            ConsoleFormatter.printCentered(" Sistema de Venta de Entradas ", "=");
            System.out.println("");

            ConsoleFormatter.printTabbed("[1] Gestión de Locales");
            ConsoleFormatter.printTabbed("[2] Gestión de Eventos (pendiente)");
            ConsoleFormatter.printTabbed("[3] Gestión de Entradas (pendiente)");
            ConsoleFormatter.printTabbed("[4] Gestión de Ventas (pendiente)");
            ConsoleFormatter.printTabbed("[5] Reportes (pendiente)");

            System.out.println("");
            ConsoleFormatter.printTabbed("[9] Salir");

            System.out.println("");
            ConsoleFormatter.printLine("-");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 ->
                    LocalView.mostrarMenu();
                case 2 ->
                    System.out.println("Módulo de eventos aún no implementado.");
                case 3 ->
                    System.out.println("Módulo de entradas aún no implementado.");
                case 4 ->
                    System.out.println("Módulo de ventas aún no implementado.");
                case 5 ->
                    System.out.println("Módulo de reportes aún no implementado.");
                case 9 ->
                    System.out.println("Saliendo del sistema...");
                default ->
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 9);

        scanner.close();
    }

}
