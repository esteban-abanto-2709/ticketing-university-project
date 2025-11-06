package ticketing.event.ticketType;

import ticketing.event.Event;
import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

import java.util.List;

public class TicketTypeView {

    private static final TicketTypeController controller = new TicketTypeController();

    public static void showMenu(Event event) {

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printCentered(" GESTIÓN DE TIPOS DE ENTRADAS [" + event.getCode() + "] ", "=");
        ConsoleFormatter.printLineBreak();

        int option;

        do {
            ConsoleFormatter.printTabbed("[1] Agregar Tipo de Entrada");
            ConsoleFormatter.printTabbed("[2] Editar Tipo de Entrada");
            ConsoleFormatter.printTabbed("[3] Eliminar Tipo de Entrada");
            ConsoleFormatter.printTabbed("[4] Listar Tipos de Entrada");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Volver");
            ConsoleFormatter.printLine("-");

            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> addTicketType(event);
                case 2 -> editTicketType(event);
                case 3 -> deleteTicketType(event);
                case 4 -> listTicketTypes(event);
                case 9 -> ConsoleFormatter.printLeft("Regresando al menú de Eventos...");
                default -> ConsoleFormatter.printLeft("Opción no válida.");
            }

            if (option != 9) InputValidator.pressEnterToContinue();

        } while (option != 9);
    }

    private static void addTicketType(Event event) {
        ConsoleFormatter.printLineBreak();

        String name = InputValidator.getNonEmptyString("Nombre del tipo de entrada: ");
        String description = InputValidator.getOptionalString("Descripción (opcional): ");
        double price = InputValidator.getDoubleMin("Precio (S/): ", 0);

        TicketType ticketType = new TicketType(event.getCode(), name, description, price);

        if (controller.register(ticketType))
            ConsoleFormatter.printSuccess("Tipo de entrada agregado exitosamente.");
        else
            ConsoleFormatter.printError("No se pudo agregar el tipo de entrada.");
    }

    private static void editTicketType(Event event) {
        ConsoleFormatter.printLineBreak();

        String name = InputValidator.getNonEmptyString("Nombre del tipo de entrada a editar: ");
        TicketType tt = controller.findTicketType(event.getCode(), name);

        if (tt == null) {
            ConsoleFormatter.printError("Tipo de entrada no encontrado.");
            return;
        }

        ConsoleFormatter.printWarning("El nombre no puede modificarse (es parte del identificador).");

        String newDescription = InputValidator.getOptionalString(
                "Nueva descripción [" + tt.getDescription() + "]: "
        );
        if (newDescription != null && !newDescription.isBlank()) {
            tt.setDescription(newDescription.trim());
        }

        Double newPrice = InputValidator.getOptionalDouble(
                "Nuevo precio [" + tt.getPrice() + "]: "
        );
        if (newPrice != null && newPrice >= 0) {
            tt.setPrice(newPrice);
        }

        if (controller.update(tt))
            ConsoleFormatter.printSuccess("Tipo de entrada actualizado exitosamente.");
        else
            ConsoleFormatter.printError("No se pudo actualizar el tipo de entrada.");
    }

    private static void deleteTicketType(Event event) {
        ConsoleFormatter.printLineBreak();

        String name = InputValidator.getNonEmptyString("Nombre del tipo de entrada a eliminar: ");

        if (controller.deleteTicketType(event.getCode(), name))
            ConsoleFormatter.printSuccess("Tipo de entrada eliminado correctamente.");
        else
            ConsoleFormatter.printError("No se pudo eliminar el tipo de entrada.");
    }

    private static void listTicketTypes(Event event) {
        ConsoleFormatter.printLineBreak();

        List<TicketType> types = controller.findByEvent(event.getCode());

        if (types == null || types.isEmpty()) {
            ConsoleFormatter.printInfo("No hay tipos de entrada registrados para este evento.");
            return;
        }

        ConsoleFormatter.printLeft("Tipos de entrada registrados:");
        for (TicketType tt : types) {
            ConsoleFormatter.printTabbed("- " + tt.getName() + " | S/ " + tt.getPrice() +
                    (tt.getDescription() != null && !tt.getDescription().isBlank() ? " | " + tt.getDescription() : ""));
        }
    }
}
