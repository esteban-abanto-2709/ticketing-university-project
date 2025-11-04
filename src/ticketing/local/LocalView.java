package ticketing.local;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

import java.util.List;

public class LocalView {

    private static final LocalController controller = new LocalController();

    public static void showMenu() {
        int option;

        do {
            ConsoleFormatter.printCentered(" GESTIÓN DE LOCALES ", "=");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[1] Registrar Local");
            ConsoleFormatter.printTabbed("[2] Mostrar Locales");
            ConsoleFormatter.printTabbed("[3] Editar Local");
            ConsoleFormatter.printTabbed("[4] Ver Detalle de Local");
            ConsoleFormatter.printTabbed("[5] Eliminar Local");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> registerLocal();
                case 2 -> showLocals();
                case 3 -> updateLocal();
                case 4 -> showDetail();
                case 5 -> deleteLocal();
                case 9 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

            if (option != 9) {
                InputValidator.pressEnterToContinue();
            }

        } while (option != 9);
    }

    private static void registerLocal() {
        ConsoleFormatter.printCentered(" REGISTRAR NUEVO LOCAL ", "=");
        ConsoleFormatter.printLineBreak();

        String code = InputValidator.getCode("Ingrese código del local: ");

        if (controller.exists(code)) {
            ConsoleFormatter.printError("Ya existe un local con el código: " + code);
            return;
        }

        String name = InputValidator.getNonEmptyString("Ingrese nombre del local: ");
        String address = InputValidator.getOptionalString("Ingrese dirección del local (opcional): ");
        int capacity = InputValidator.getIntMin("Ingrese capacidad del local: ", 1);

        Local local = new Local(code, name, address, capacity);

        showSummary(local);

        if (InputValidator.getConfirmation("¿Confirma el registro del local?")) {
            if (controller.register(local)) {
                ConsoleFormatter.printSuccess("Local registrado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo registrar el local.");
            }
        } else {
            ConsoleFormatter.printInfo("Registro cancelado.");
        }
    }

    private static void showLocals() {
        ConsoleFormatter.printCentered(" LISTA DE LOCALES ", "=");
        ConsoleFormatter.printLineBreak();

        if (!controller.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        List<Local> locales = controller.findAll();
        ConsoleFormatter.printList("Total de locales", String.valueOf(locales.size()), " ");
        ConsoleFormatter.printLine("-");

        for (int i = 0; i < locales.size(); i++) {
            Local local = locales.get(i);
            ConsoleFormatter.printTabbed((i + 1) + ". " + local.getName() + " [" + local.getCode() + "]");
        }
    }

    private static void updateLocal() {
        ConsoleFormatter.printCentered(" EDITAR LOCAL ", "=");
        ConsoleFormatter.printLineBreak();

        if (!controller.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del local a editar: ");
        Local local = controller.findByCode(code);

        if (local == null) {
            ConsoleFormatter.printError("No se encontró un local con el código: " + code);
            return;
        }

        System.out.println("\nDatos actuales:");
        showSummary(local);

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printInfo("Ingrese los nuevos datos (o presione Enter para mantener los actuales):");
        ConsoleFormatter.printLineBreak();

        String newName = InputValidator.getOptionalString("Nuevo nombre [" + local.getName() + "]: ");
        String newAddress = InputValidator.getOptionalString("Nueva dirección [" + local.getAddress() + "]: ");
        String newCapacityStr = InputValidator.getOptionalString("Nueva capacidad [" + local.getCapacity() + "]: ");

        if (!newName.isEmpty()) local.setName(newName);
        if (!newAddress.isEmpty()) local.setAddress(newAddress);
        if (!newCapacityStr.isEmpty()) {
            try {
                int newCap = Integer.parseInt(newCapacityStr);
                if (newCap > 0) local.setCapacity(newCap);
                else ConsoleFormatter.printWarning("La capacidad debe ser mayor que 0. Se mantiene el valor anterior.");
            } catch (NumberFormatException e) {
                ConsoleFormatter.printWarning("Valor de capacidad inválido. Se mantiene el valor anterior.");
            }
        }

        System.out.println("\nNuevos datos:");
        showSummary(local);

        if (InputValidator.getConfirmation("¿Confirma la edición del local?")) {
            if (controller.update(local)) {
                ConsoleFormatter.printSuccess("Local actualizado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo actualizar el local.");
            }
        } else {
            ConsoleFormatter.printInfo("Edición cancelada.");
        }
    }

    private static void deleteLocal() {
        ConsoleFormatter.printCentered(" ELIMINAR LOCAL ", "=");
        System.out.println();

        if (!controller.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del local a eliminar: ");
        Local local = controller.findByCode(code);

        if (local == null) {
            ConsoleFormatter.printError("No se encontró un local con el código: " + code);
            return;
        }

        System.out.println("\nLocal a eliminar:");
        showSummary(local);
        ConsoleFormatter.printWarning("Esta acción no se puede deshacer.");

        if (InputValidator.getConfirmation("¿Está seguro de eliminar este local?")) {
            if (controller.delete(code)) {
                ConsoleFormatter.printSuccess("Local eliminado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo eliminar el local.");
            }
        } else {
            ConsoleFormatter.printInfo("Eliminación cancelada.");
        }
    }

    private static void showDetail() {
        ConsoleFormatter.printCentered(" DETALLE DE LOCAL ", "=");
        System.out.println();

        if (!controller.hasLocals()) {
            ConsoleFormatter.printInfo("No hay locales registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del local: ");
        Local local = controller.findByCode(code);

        if (local == null) {
            ConsoleFormatter.printError("No se encontró un local con el código: " + code);
        } else {
            showSummary(local);
        }
    }

    private static void showSummary(Local local) {
        ConsoleFormatter.printLine("=");
        ConsoleFormatter.printList("Código", local.getCode(), " ");
        ConsoleFormatter.printList("Nombre", local.getName(), " ");
        ConsoleFormatter.printList("Dirección", local.getAddress(), " ");
        ConsoleFormatter.printList("Capacidad", String.valueOf(local.getCapacity()), " ");
        ConsoleFormatter.printLine("=");
    }
}
