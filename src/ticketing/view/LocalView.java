package ticketing.view;

import java.util.List;

import ticketing.entities.Local;
import ticketing.entities.Zone;
import ticketing.controller.LocalController;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

public class LocalView {

    private static final LocalController localService = new LocalController();

    public static void mostrarMenu() {
        int opcion;
        do {
            ConsoleFormatter.printCentered(" GESTIÓN DE LOCALES ", "=");
            System.out.println();
            ConsoleFormatter.printTabbed("[1] Registrar Local");
            ConsoleFormatter.printTabbed("[2] Mostrar Locales");
            ConsoleFormatter.printTabbed("[3] Buscar Local");
            ConsoleFormatter.printTabbed("[4] Ver Detalle de Local");
            ConsoleFormatter.printTabbed("[5] Eliminar Local (pendiente)");
            System.out.println();
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            opcion = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (opcion) {
                case 1 ->
                    registrarLocal();
                case 2 ->
                    mostrarLocales();
                case 3 ->
                    buscarLocales();
                case 4 ->
                    verDetalleLocal();
                case 5 ->
                    System.out.println("Función eliminar (a implementar)");
                case 9 ->
                    System.out.println("Regresando al menú principal...");
                default ->
                    System.out.println("Opción no válida.");

            }

            if (opcion != 9) {
                InputValidator.pressEnterToContinue();
            }

        } while (opcion != 9);
    }

    private static void registrarLocal() {
        ConsoleFormatter.printCentered(" REGISTRAR NUEVO LOCAL ", "=");
        System.out.println();

        // Pedir datos básicos del local
        String codigo = InputValidator.getCode("Ingrese código del local: ");

        // Verificar que el código no exista
        if (localService.isCodeAlreadyExists(codigo)) {
            ConsoleFormatter.printError("Ya existe un local con el código: " + codigo);
            return;
        }

        String nombre = InputValidator.getNonEmptyString("Ingrese nombre del local: ");
        String direccion = InputValidator.getNonEmptyString("Ingrese dirección del local: ");

        Local local = new Local(codigo, nombre, direccion);

        // Registrar zonas
        int numZonas = InputValidator.getIntInRange("¿Cuántas zonas desea registrar? ", 1, 20);

        for (int i = 0; i < numZonas; i++) {
            System.out.println("Zona " + (i + 1));
            String nombreZona = InputValidator.getNonEmptyString("   Nombre de la zona: ");
            int capacidad = InputValidator.getIntInRange("   Capacidad de la zona: ", 1, 50000);

            local.agregarZona(new Zone(nombreZona, capacidad));
        }

        // Panel de confirmación
        mostrarResumenLocal(local);

        if (InputValidator.getConfirmation("¿Confirma el registro del local?")) {
            if (localService.registerLocal(local)) {
                ConsoleFormatter.printSuccess("Local registrado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo registrar el local.");
            }
        } else {
            ConsoleFormatter.printInfo("Registro cancelado.");
        }
    }

    private static void mostrarLocales() {
        ConsoleFormatter.printCentered(" LISTA DE LOCALES ", "=");
        System.out.println();

        if (!localService.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        List<Local> locales = localService.getAllLocals();
        ConsoleFormatter.printList("Total de locales", String.valueOf(locales.size()), ".");
        ConsoleFormatter.printLine("-");

        for (int i = 0; i < locales.size(); i++) {
            Local local = locales.get(i);
            System.out.println();
            ConsoleFormatter.printTabbed((i + 1) + ". " + local.getNombre(), 1);
            ConsoleFormatter.printTabbed("Código: " + local.getCodigo(), 2);
            ConsoleFormatter.printTabbed("Dirección: " + local.getDireccion(), 2);
            ConsoleFormatter.printTabbed("Zonas: " + local.getZonas().size(), 2);
        }
    }

    private static void buscarLocales() {
        ConsoleFormatter.printCentered(" BUSCAR LOCALES ", "=");
        System.out.println();

        if (!localService.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados para buscar.");
            return;
        }

        String termino = InputValidator.getNonEmptyString("Ingrese nombre del local a buscar: ");
        List<Local> resultados = localService.searchLocalsByName(termino);

        if (resultados.isEmpty()) {
            ConsoleFormatter.printInfo("No se encontraron locales con el término: " + termino);
        } else {
            ConsoleFormatter.printSuccess("Se encontraron " + resultados.size() + " resultado(s):");
            ConsoleFormatter.printLine("-");

            for (Local local : resultados) {
                System.out.println();
                ConsoleFormatter.printTabbed("• " + local.getNombre(), 1);
                ConsoleFormatter.printTabbed("Código: " + local.getCodigo(), 2);
                ConsoleFormatter.printTabbed("Dirección: " + local.getDireccion(), 2);
            }
        }
    }

    private static void verDetalleLocal() {
        ConsoleFormatter.printCentered(" DETALLE DE LOCAL ", "=");
        System.out.println();

        if (!localService.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del local: ");
        Local local = localService.getLocalByCode(codigo);

        if (local == null) {
            ConsoleFormatter.printError("No se encontró un local con el código: " + codigo);
        } else {
            mostrarResumenLocal(local);
        }
    }

    private static void mostrarResumenLocal(Local local) {
        ConsoleFormatter.printLine("=");
        ConsoleFormatter.printList("Código", local.getCodigo(), " ");
        ConsoleFormatter.printList("Nombre", local.getNombre(), " ");
        ConsoleFormatter.printList("Dirección", local.getDireccion(), " ");
        ConsoleFormatter.printLine("-");

        ConsoleFormatter.printTabbed("ZONAS:", 1);
        for (Zone zona : local.getZonas()) {
            ConsoleFormatter.printList("  " + zona.getNombre(), zona.getCapacidad() + " personas", ".", 1);
        }
        ConsoleFormatter.printLine("=");
    }
}
