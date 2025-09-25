package ticketing.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ticketing.entities.Local;
import ticketing.entities.Zone;
import ticketing.utils.ConsoleFormatter;

public class LocalManagementMenu {

    private static List<Local> locales = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void mostrarMenu() {
        int opcion;
        do {
            ConsoleFormatter.cleanConsole();
            ConsoleFormatter.printCentered(" Gestión de Locales ", "=");

            ConsoleFormatter.printTabbed("[1] Registrar Local");
            ConsoleFormatter.printTabbed("[2] Mostrar Locales");
            ConsoleFormatter.printTabbed("[3] Eliminar Local (pendiente futuro)");
            ConsoleFormatter.printTabbed("[4] Buscar Local (pendiente futuro)");

            System.out.println("");
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");

            System.out.println("");
            ConsoleFormatter.printLine("-");
            System.out.print("Seleccione una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 ->
                    registrarLocal();
                case 2 ->
                    mostrarLocales();
                case 3 ->
                    System.out.println("Función eliminar (a implementar)");
                case 4 ->
                    System.out.println("Función eliminar (a implementar)");
                case 9 ->
                    System.out.println("Regresando al menú principal...");
                default ->
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 9);
    }

    private static void registrarLocal() {
        System.out.print("Ingrese código del local: ");
        String codigo = scanner.nextLine();

        System.out.print("Ingrese nombre del local: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese dirección del local: ");
        String direccion = scanner.nextLine();

        Local local = new Local(codigo, nombre, direccion);

        System.out.print("¿Cuántas zonas desea registrar para este local? ");
        int numZonas = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numZonas; i++) {
            System.out.println("Zona " + (i + 1) + ":");
            System.out.print("   Nombre de la zona: ");
            String nombreZona = scanner.nextLine();

            System.out.print("   Capacidad de la zona: ");
            int capacidad = scanner.nextInt();
            scanner.nextLine();

            local.agregarZona(new Zone(nombreZona, capacidad));
        }

        locales.add(local);
        System.out.println(">>> Local registrado exitosamente.");
    }

    private static void mostrarLocales() {
        if (locales.isEmpty()) {
            System.out.println("No hay locales registrados.");
        } else {
            System.out.println("\n--- Lista de Locales ---");
            for (Local l : locales) {
                System.out.println(l.toString());
            }
        }
    }
}
