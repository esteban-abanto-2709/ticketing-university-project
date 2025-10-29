package ticketing;

import ticketing.utils.ConsoleFormatter;
import ticketing.view.LocalView;
import ticketing.artist.ArtistView;
import ticketing.view.EventView;

import ticketing.utils.InputValidator;

public class Ticketing {

    public static void main(String[] args) {
        int option;

        do {
            ConsoleFormatter.printCentered("Sistema de Venta de Entradas", "=");
            ConsoleFormatter.printLineBreak();

            ConsoleFormatter.printTabbed("[1] Gestión de Locales");
            ConsoleFormatter.printTabbed("[2] Gestión de Artistas");
            ConsoleFormatter.printTabbed("[3] Gestión de Eventos");
            ConsoleFormatter.printTabbed("[3] Gestión de Entradas (pendiente)");
            ConsoleFormatter.printTabbed("[4] Gestión de Ventas (pendiente)");
            ConsoleFormatter.printTabbed("[5] Reportes (pendiente)");

            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Salir");

            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printLine("-");
            System.out.print("Seleccione una opcion: ");
            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> LocalView.mostrarMenu();
                case 2 -> ArtistView.showMenu();
                case 3 -> EventView.mostrarMenu();
                case 4 -> System.out.println("Módulo de ventas aún no implementado.");
                case 5 -> System.out.println("Módulo de reportes aún no implementado.");
                case 9 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción no válida.");
            }

        } while (option != 9);
    }

}
