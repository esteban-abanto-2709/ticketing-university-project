package ticketing.sale;

import ticketing.event.Event;
import ticketing.event.EventController;
import ticketing.event.ticketType.TicketType;
import ticketing.event.ticketType.TicketTypeController;
import ticketing.event.zone.Zone;
import ticketing.event.zone.ZoneController;
import ticketing.ticket.Ticket;
import ticketing.ticket.TicketController;
import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

import java.util.List;

public class SaleView {

    private static final EventController eventController = new EventController();
    private static final ZoneController zoneController = new ZoneController();
    private static final TicketTypeController ticketTypeController = new TicketTypeController();
    private static final TicketController ticketController = new TicketController();

    public static void showMenu() {
        int option;

        do {
            ConsoleFormatter.printCentered(" GESTIÓN DE VENTAS ", "=");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[1] Consultar Disponibilidad de Evento");
            ConsoleFormatter.printTabbed("[2] Registrar Venta");
            ConsoleFormatter.printTabbed("[3] Consultar Entradas por ID de Venta");
            ConsoleFormatter.printTabbed("[4] Cancelar Venta");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> consultAvailability();
                case 2 -> registerSale();
                case 3 -> consultSale();
                case 4 -> cancelSale();
                case 9 -> ConsoleFormatter.printLeft("Regresando al menú principal...");
                default -> ConsoleFormatter.printLeft("Opción no válida.");
            }

            if (option != 9) {
                InputValidator.pressEnterToContinue();
            }

        } while (option != 9);
    }

    private static void consultAvailability() {
        ConsoleFormatter.printCentered(" CONSULTAR DISPONIBILIDAD ", "=");
        ConsoleFormatter.printLineBreak();

        if (!eventController.hasEvents()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String eventCode = InputValidator.getCode("Ingrese código del evento: ");
        Event event = eventController.findByCode(eventCode);

        if (event == null) {
            ConsoleFormatter.printError("Evento no encontrado.");
            return;
        }

        // Verificar que tenga entradas generadas
        if (!ticketController.hasTickets(eventCode)) {
            ConsoleFormatter.printWarning("Este evento no tiene entradas generadas aún.");
            return;
        }

        List<Zone> zones = zoneController.findByEvent(eventCode);

        if (zones == null || zones.isEmpty()) {
            ConsoleFormatter.printInfo("Este evento no tiene zonas configuradas.");
            return;
        }

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printCentered(" " + event.getName() + " ", "=");
        ConsoleFormatter.printList("Fecha del evento", event.getDate().toString(), " ");
        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printLeft("ZONAS DISPONIBLES:");
        ConsoleFormatter.printLine("-");

        for (Zone zone : zones) {
            int total = zone.getCapacity();
            int available = ticketController.countAvailableByZone(eventCode, zone.getName());
            int sold = total - available;

            String priceStr = "S/ " + String.format("%.2f", zone.getPrice());
            String availabilityStr = available + "/" + total + " disponibles";

            ConsoleFormatter.printList(zone.getName() + " (" + priceStr + ")", availabilityStr, " ");
            ConsoleFormatter.printTabbed("  Vendidas: " + sold);
        }

        ConsoleFormatter.printLine("=");
    }

    private static void registerSale() {
        ConsoleFormatter.printCentered(" REGISTRAR VENTA ", "=");
        ConsoleFormatter.printLineBreak();

        if (!eventController.hasEvents()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        // Seleccionar evento
        String eventCode = InputValidator.getCode("Ingrese código del evento: ");
        Event event = eventController.findByCode(eventCode);

        if (event == null) {
            ConsoleFormatter.printError("Evento no encontrado.");
            return;
        }

        if (!ticketController.hasTickets(eventCode)) {
            ConsoleFormatter.printWarning("Este evento no tiene entradas generadas.");
            return;
        }

        // Mostrar zonas disponibles
        List<Zone> zones = zoneController.findByEvent(eventCode);
        if (zones == null || zones.isEmpty()) {
            ConsoleFormatter.printError("Este evento no tiene zonas configuradas.");
            return;
        }

        ConsoleFormatter.printInfo("Zonas disponibles:");
        for (Zone zone : zones) {
            int available = ticketController.countAvailableByZone(eventCode, zone.getName());
            ConsoleFormatter.printTabbed("- " + zone.getName() + " (S/ " +
                    String.format("%.2f", zone.getPrice()) + ") → " + available + " disponibles");
        }

        ConsoleFormatter.printLineBreak();

        // Seleccionar zona
        String zoneName = InputValidator.getNonEmptyString("Ingrese nombre de la zona: ");
        Zone selectedZone = null;

        for (Zone z : zones) {
            if (z.getName().equalsIgnoreCase(zoneName)) {
                selectedZone = z;
                break;
            }
        }

        if (selectedZone == null) {
            ConsoleFormatter.printError("Zona no encontrada.");
            return;
        }

        int available = ticketController.countAvailableByZone(eventCode, selectedZone.getName());
        if (available == 0) {
            ConsoleFormatter.printError("No hay entradas disponibles en esta zona.");
            return;
        }

        // Mostrar tipos de entrada
        List<TicketType> ticketTypes = ticketTypeController.findByEvent(eventCode);
        if (ticketTypes == null || ticketTypes.isEmpty()) {
            ConsoleFormatter.printError("Este evento no tiene tipos de entrada configurados.");
            return;
        }

        ConsoleFormatter.printInfo("Tipos de entrada disponibles:");
        for (TicketType tt : ticketTypes) {
            ConsoleFormatter.printTabbed("- " + tt.getName() + " (+S/ " +
                    String.format("%.2f", tt.getPrice()) + ")");
        }

        ConsoleFormatter.printLineBreak();

        // Seleccionar tipo
        String ticketTypeName = InputValidator.getNonEmptyString("Ingrese tipo de entrada: ");
        TicketType selectedType = null;

        for (TicketType tt : ticketTypes) {
            if (tt.getName().equalsIgnoreCase(ticketTypeName)) {
                selectedType = tt;
                break;
            }
        }

        if (selectedType == null) {
            ConsoleFormatter.printError("Tipo de entrada no encontrado.");
            return;
        }

        // Cantidad
        int quantity = InputValidator.getIntInRange("Cantidad de entradas (máx " + available + "): ", 1, available);

        // Calcular precio
        double basePrice = selectedZone.getPrice();
        double typePrice = selectedType.getPrice();
        double unitPrice = basePrice + typePrice;
        double totalPrice = unitPrice * quantity;

        // Resumen
        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printCentered(" RESUMEN DE COMPRA ", "=");
        ConsoleFormatter.printList("Evento", event.getName(), " ");
        ConsoleFormatter.printList("Fecha", event.getDate().toString(), " ");
        ConsoleFormatter.printList("Zona", selectedZone.getName(), " ");
        ConsoleFormatter.printList("Tipo", selectedType.getName(), " ");
        ConsoleFormatter.printList("Cantidad", String.valueOf(quantity), " ");
        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printList("Precio base zona", "S/ " + String.format("%.2f", basePrice), " ");
        ConsoleFormatter.printList("Precio tipo entrada", "+ S/ " + String.format("%.2f", typePrice), " ");
        ConsoleFormatter.printList("Precio unitario", "S/ " + String.format("%.2f", unitPrice), " ");
        ConsoleFormatter.printLine("-");
        ConsoleFormatter.printList("TOTAL A PAGAR", "S/ " + String.format("%.2f", totalPrice), " ");
        ConsoleFormatter.printLine("=");

        ConsoleFormatter.printLineBreak();

        if (!InputValidator.getConfirmation("¿Confirmar venta?")) {
            ConsoleFormatter.printInfo("Venta cancelada.");
            return;
        }

        // ID de venta (puede ser DNI del cliente)
        int saleId = InputValidator.getIntMin("Ingrese ID de venta (DNI del cliente u otro identificador): ", 1);

        // Procesar venta
        if (ticketController.sellTickets(eventCode, selectedZone.getName(), selectedType.getName(), quantity, saleId)) {
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printCentered(" VENTA EXITOSA ", "=");
            ConsoleFormatter.printList("ID de Venta", String.valueOf(saleId), " ");
            ConsoleFormatter.printList("Total pagado", "S/ " + String.format("%.2f", totalPrice), " ");

            // Mostrar entradas vendidas
            List<Ticket> soldTickets = ticketController.findBySaleId(saleId);
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printLeft("Entradas vendidas:");
            for (Ticket ticket : soldTickets) {
                ConsoleFormatter.printTabbed("- " + ticket.toString() + " | " + ticket.getType());
            }
            ConsoleFormatter.printLine("=");
        } else {
            ConsoleFormatter.printError("No se pudo completar la venta.");
        }
    }

    private static void consultSale() {
        ConsoleFormatter.printCentered(" CONSULTAR VENTA ", "=");
        ConsoleFormatter.printLineBreak();

        int saleId = InputValidator.getIntMin("Ingrese ID de venta: ", 1);

        List<Ticket> tickets = ticketController.findBySaleId(saleId);

        if (tickets == null || tickets.isEmpty()) {
            ConsoleFormatter.printInfo("No se encontraron entradas con ese ID de venta.");
            return;
        }

        // Obtener info del evento (asumiendo que todas son del mismo evento)
        String eventCode = tickets.get(0).getEventCode();
        Event event = eventController.findByCode(eventCode);

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printCentered(" ENTRADAS - ID: " + saleId + " ", "=");

        if (event != null) {
            ConsoleFormatter.printList("Evento", event.getName(), " ");
            ConsoleFormatter.printList("Fecha", event.getDate().toString(), " ");
        }

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printLeft("Entradas:");

        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            ConsoleFormatter.printTabbed((i + 1) + ". " + ticket.toString() + " | " + ticket.getType());
        }

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printList("Total de entradas", String.valueOf(tickets.size()), " ");
        ConsoleFormatter.printLine("=");
    }

    private static void cancelSale() {
        ConsoleFormatter.printCentered(" CANCELAR VENTA ", "=");
        ConsoleFormatter.printLineBreak();

        int saleId = InputValidator.getIntMin("Ingrese ID de venta a cancelar: ", 1);

        List<Ticket> tickets = ticketController.findBySaleId(saleId);

        if (tickets == null || tickets.isEmpty()) {
            ConsoleFormatter.printInfo("No se encontraron entradas con ese ID de venta.");
            return;
        }

        ConsoleFormatter.printWarning("Se liberarán " + tickets.size() + " entradas.");
        ConsoleFormatter.printWarning("Esta acción no se puede deshacer.");

        if (InputValidator.getConfirmation("¿Está seguro de cancelar esta venta?")) {
            ticketController.cancelSale(saleId);
        } else {
            ConsoleFormatter.printInfo("Cancelación abortada.");
        }
    }
}