package ticketing.event.zone;

import ticketing.event.Event;
import ticketing.local.Local;
import ticketing.local.LocalController;
import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

import java.util.List;

public class ZoneView {

    private static final ZoneController controller = new ZoneController();
    private static final LocalController localController = new LocalController();

    public static void showMenu(Event event) {

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printCentered(" GESTIÓN DE ZONAS DEL EVENTO [" + event.getCode() + "] ", "=");
        ConsoleFormatter.printLineBreak();

        int option;

        do {
            ConsoleFormatter.printTabbed("[1] Agregar Zona");
            ConsoleFormatter.printTabbed("[2] Editar Zona");
            ConsoleFormatter.printTabbed("[3] Eliminar Zona");
            ConsoleFormatter.printTabbed("[4] Listar Zonas");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Volver");
            ConsoleFormatter.printLine("-");

            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> addZone(event);
                case 2 -> editZone(event);
                case 3 -> deleteZone(event);
                case 4 -> listZones(event);
                case 9 -> ConsoleFormatter.printInfo("Regresando al menú de Eventos...");
                default -> System.out.println("Opción no válida.");
            }

            if (option != 9) InputValidator.pressEnterToContinue();

        } while (option != 9);
    }

    private static void showRemaining(Event event) {
        Local local = localController.findByCode(event.getLocalCode());
        int used = controller.getTotalCapacity(event.getCode());
        int remaining = local.getCapacity() - used;
        ConsoleFormatter.printInfo("Capacidad restante del local: " + remaining);
    }

    private static void addZone(Event event) {

        Local local = localController.findByCode(event.getLocalCode());
        int used = controller.getTotalCapacity(event.getCode());
        int remaining = local.getCapacity() - used;

        showRemaining(event);

        if (remaining <= 0) {
            ConsoleFormatter.printError("No queda capacidad disponible en el local.");
            return;
        }

        String name = InputValidator.getNonEmptyString("Nombre de la zona: ");
        int cap = InputValidator.getIntInRange("Capacidad (máx " + remaining + "): ", 1, remaining);
        double price = InputValidator.getDoubleMin("Precio base (S/): ", 0);

        Zone z = new Zone(event.getCode(), name, cap, price);

        if (controller.register(z))
            ConsoleFormatter.printSuccess("Zona agregada exitosamente.");
        else
            ConsoleFormatter.printError("No se pudo agregar la zona.");
    }

    private static void editZone(Event event) {

        String name = InputValidator.getNonEmptyString("Nombre de la zona a editar: ");
        Zone z = controller.findZone(event.getCode(), name);

        if (z == null) {
            ConsoleFormatter.printError("Zona no encontrada.");
            return;
        }

        Local local = localController.findByCode(event.getLocalCode());
        int used = controller.getTotalCapacity(event.getCode());
        int available = local.getCapacity() - (used - z.getCapacity());

        showRemaining(event);

        ConsoleFormatter.printWarning("El nombre de la zona no puede cambiarse por ser parte del identificador.");

        Integer newCap = InputValidator.getOptionalInt(
                "Nueva capacidad [" + z.getCapacity() + "] (máx " + available + "): "
        );
        if (newCap != null) {
            if (newCap > 0 && newCap <= available) z.setCapacity(newCap);
            else ConsoleFormatter.printError("La capacidad ingresada no es válida.");
        }

        Double newPrice = InputValidator.getOptionalDouble(
                "Nuevo precio base [" + z.getBasePrice() + "]: "
        );
        if (newPrice != null) {
            if (newPrice >= 0) z.setBasePrice(newPrice);
            else ConsoleFormatter.printError("El precio ingresado no es válido.");
        }

        if (controller.update(z))
            ConsoleFormatter.printSuccess("Zona actualizada exitosamente.");
        else
            ConsoleFormatter.printError("No se pudo actualizar la zona.");
    }

    private static void deleteZone(Event event) {

        String name = InputValidator.getNonEmptyString("Nombre de la zona a eliminar: ");

        if (controller.deleteZone(event.getCode(), name))
            ConsoleFormatter.printSuccess("Zona eliminada.");
        else
            ConsoleFormatter.printError("No se pudo eliminar la zona.");
    }

    private static void listZones(Event event) {

        List<Zone> zones = controller.findByEvent(event.getCode());

        if (zones.isEmpty()) {
            ConsoleFormatter.printInfo("No hay zonas registradas.");
            return;
        }

        ConsoleFormatter.print("Zonas configuradas:");
        for (Zone zone : zones) {
            ConsoleFormatter.printTabbed("- " + zone);
        }

        ConsoleFormatter.printLineBreak();
        showRemaining(event);
    }
}
