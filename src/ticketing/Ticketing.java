package ticketing;

import ticketing.database.DatabaseManager;
import ticketing.database.DatabaseSetup;
import ticketing.database.SampleDataLoader;

import ticketing.local.LocalView;
import ticketing.artist.ArtistView;
import ticketing.event.EventView;
import ticketing.sponsor.SponsorView;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

public class Ticketing {

    public static void main(String[] args) {

        if (Config.AUTO_CREATE_DB_AND_TABLES) {
            DatabaseSetup.initializeDatabase();
        }

        if (Config.AUTO_LOAD_SAMPLE_DATA) {
            SampleDataLoader.insertSampleData();
        }

        int option;

        do {
            ConsoleFormatter.printCentered(" SISTEMA DE VENTA DE ENTRADAS ", "=");
            ConsoleFormatter.printLineBreak();

            ConsoleFormatter.printTabbed("[1] Gestión de Locales");
            ConsoleFormatter.printTabbed("[2] Gestión de Artistas");
            ConsoleFormatter.printTabbed("[3] Gestión de Sponsors");
            ConsoleFormatter.printTabbed("[4] Gestión de Eventos");
            ConsoleFormatter.printTabbed("[5] Gestión de Ventas (pendiente)");

            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Salir");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printLine("-");

            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> LocalView.showMenu();
                case 2 -> ArtistView.showMenu();
                case 3 -> SponsorView.showMenu();
                case 4 -> EventView.showMenu();
                case 5 -> ConsoleFormatter.printLeft("Módulo de Ventas aún no implementado.");
                case 9 -> ConsoleFormatter.printLeft("Saliendo del sistema...");
                default -> ConsoleFormatter.printLeft("Opción no válida.");
            }

            if (option != 9) {
                InputValidator.pressEnterToContinue();
            }

        } while (option != 9);

        DatabaseManager.closeConnection();
    }
}
