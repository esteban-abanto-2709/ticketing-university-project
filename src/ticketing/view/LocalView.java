package ticketing.view;

import java.util.List;

import ticketing.entities.Local;
import ticketing.entities.Zone;
import ticketing.controller.LocalController;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

public class LocalView {

    private static final LocalController localController = new LocalController();

    public static void mostrarMenu() {
        int opcion;
        do {
            ConsoleFormatter.printCentered(" GESTIÓN DE LOCALES ", "=");
            System.out.println();
            ConsoleFormatter.printTabbed("[1] Registrar Local");
            ConsoleFormatter.printTabbed("[2] Mostrar Locales");
            ConsoleFormatter.printTabbed("[3] Editar Local");
            ConsoleFormatter.printTabbed("[4] Ver Detalle de Local");
            ConsoleFormatter.printTabbed("[5] Eliminar Local");
            System.out.println();
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            opcion = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (opcion) {
                case 1 -> registrarLocal();
                case 2 -> mostrarLocales();
                case 3 -> editarLocal();
                case 4 -> verDetalleLocal();
                case 5 -> eliminarLocal();
                case 9 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción no válida.");

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
        if (localController.isCodeAlreadyExists(codigo)) {
            ConsoleFormatter.printError("Ya existe un local con el código: " + codigo);
            return;
        }

        String nombre = InputValidator.getNonEmptyString("Ingrese nombre del local: ");
        String direccion = InputValidator.getNonEmptyString("Ingrese dirección del local: ");

        // Registrar zonas
        Zone[] zones = solicitarZonas();
        Local local = new Local(codigo, nombre, direccion, zones);

        // Panel de confirmación
        mostrarResumenLocal(local);

        if (InputValidator.getConfirmation("¿Confirma el registro del local?")) {
            if (localController.registerLocal(local)) {
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

        if (!localController.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        List<Local> locales = localController.getAllLocals();
        ConsoleFormatter.printList("Total de locales", String.valueOf(locales.size()), " ");
        ConsoleFormatter.printLine("-");

        for (int i = 0; i < locales.size(); i++) {
            Local local = locales.get(i);
            System.out.println();
            ConsoleFormatter.printTabbed((i + 1) + ". " + local.getCodigo());
            ConsoleFormatter.printList(local.getNombre(), String.valueOf(local.getCapacidadTotal()), " ");

        }
    }

    private static void editarLocal() {
        ConsoleFormatter.printCentered(" EDITAR LOCAL ", "=");
        System.out.println();

        if (!localController.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del local a editar: ");
        Local local = localController.getLocalByCode(code);

        if (local == null) {
            ConsoleFormatter.printError("No se encontró un local con el código: " + code);
            return;
        }

        // Mostrar datos actuales
        System.out.println("\nDatos actuales:");
        mostrarResumenLocal(local);
        System.out.println();

        // Pedir nuevos datos
        ConsoleFormatter.printInfo("Ingrese los nuevos datos (o presione Enter para mantener el actual):");
        System.out.println();

        String newName = InputValidator.getOptionalString("Nuevo nombre [" + local.getNombre() + "]: ");
        if (!newName.isEmpty()) {
            local.setNombre(newName);
        }

        String newAddress = InputValidator.getOptionalString("Nueva dirección [" + local.getDireccion() + "]: ");
        if (!newAddress.isEmpty()) {
            local.setDireccion(newAddress);
        }

        if (InputValidator.getConfirmation("¿Desea modificar las zonas?")) {
            Zone[] zones = solicitarZonas();
            local.setZonas(zones);
        }

        // Mostrar resumen de cambios
        System.out.println("\nNuevos datos:");
        mostrarResumenLocal(local);

        if (InputValidator.getConfirmation("¿Confirma la edición del local?")) {
            if (localController.updateLocal(local)) {
                ConsoleFormatter.printSuccess("Local actualizado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo actualizar el local.");
            }
        } else {
            ConsoleFormatter.printInfo("Edición cancelada.");
        }
    }

    private static Zone[] solicitarZonas() {
        int numZonas = InputValidator.getIntInRange("¿Cuántas zonas desea registrar? ", 1, 20);

        Zone[] zonas = new Zone[numZonas];

        for (int i = 0; i < zonas.length; i++) {
            System.out.println("Zona " + (i + 1));
            String nombreZona = InputValidator.getNonEmptyString("   Nombre de la zona: ");
            int capacidad = InputValidator.getIntInRange("   Capacidad de la zona: ", 1, 50000);

            zonas[i] = new Zone(nombreZona, capacidad);
        }

        return zonas;
    }

    private static void eliminarLocal() {
        ConsoleFormatter.printCentered(" ELIMINAR LOCAL ", "=");
        System.out.println();

        if (!localController.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del local a eliminar: ");
        Local local = localController.getLocalByCode(codigo);

        if (local == null) {
            ConsoleFormatter.printError("No se encontró un local con el código: " + codigo);
            return;
        }

        // Mostrar datos del local a eliminar
        System.out.println("\nLocal a eliminar:");
        mostrarResumenLocal(local);

        ConsoleFormatter.printWaring("Esta acción no se puede deshacer.");

        if (InputValidator.getConfirmation("¿Está seguro de eliminar este local?")) {
            if (localController.deleteLocal(codigo)) {
                ConsoleFormatter.printSuccess("Local eliminado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo eliminar el local.");
            }
        } else {
            ConsoleFormatter.printInfo("Eliminación cancelada.");
        }
    }

    private static void verDetalleLocal() {
        ConsoleFormatter.printCentered(" DETALLE DE LOCAL ", "=");
        System.out.println();

        if (!localController.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del local: ");
        Local local = localController.getLocalByCode(codigo);

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
