package ticketing;

import ticketing.database.DatabaseManager;
import ticketing.database.DatabaseSetup;
import ticketing.database.SampleDataLoader;

import ticketing.local.LocalView;
import ticketing.artist.ArtistView;
import ticketing.view.EventView;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

public class Ticketing {

    public static void main(String[] args) {

        DatabaseSetup.initializeDatabase();
        SampleDataLoader.insertSampleData();

        int option;

        do {
            ConsoleFormatter.printCentered("Sistema de Venta de Entradas", "=");
            ConsoleFormatter.printLineBreak();

            ConsoleFormatter.printTabbed("[1] Gestión de Locales");
            ConsoleFormatter.printTabbed("[2] Gestión de Artistas");
            ConsoleFormatter.printTabbed("[3] Gestión de Eventos");
            ConsoleFormatter.printTabbed("[4] Gestión de Entradas (pendiente)");
            ConsoleFormatter.printTabbed("[5] Gestión de Ventas (pendiente)");
            ConsoleFormatter.printTabbed("[6] Reportes (pendiente)");

            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Salir");

            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printLine("-");
            System.out.print("Seleccione una opcion: ");
            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> LocalView.showMenu();
                case 2 -> ArtistView.showMenu();
                case 3 -> EventView.mostrarMenu();
                case 4 -> System.out.println("Módulo de Entradas aún no implementado.");
                case 5 -> System.out.println("Módulo de Ventas aún no implementado.");
                case 6 -> System.out.println("Módulo de Reportes aún no implementado.");
                case 9 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción no válida.");
            }

        } while (option != 9);

        DatabaseManager.closeConnection();
    }
}
