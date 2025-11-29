package ticketing.event;

import ticketing.artist.Artist;
import ticketing.artist.ArtistController;

import ticketing.local.Local;
import ticketing.local.LocalController;

import ticketing.event.zone.Zone;
import ticketing.event.zone.ZoneController;
import ticketing.event.zone.ZoneView;

import ticketing.event.ticketType.TicketType;
import ticketing.event.ticketType.TicketTypeController;
import ticketing.event.ticketType.TicketTypeView;

import ticketing.ticket.TicketController;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

import java.time.LocalDate;
import java.util.List;

public class EventView {

    private static final EventController eventController = new EventController();
    private static final ArtistController artistController = new ArtistController();
    private static final LocalController localController = new LocalController();
    private static final ZoneController zoneController = new ZoneController();
    private static final TicketTypeController ticketTypeController = new TicketTypeController();
    private static final TicketController ticketController = new TicketController();

    public static void showMenu() {

        if (!localController.hasLocals()) {
            ConsoleFormatter.printError("Debe registrar al menos un LOCAL antes de gestionar eventos.");
            return;
        }

        if (!artistController.hasArtists()) {
            ConsoleFormatter.printError("Debe registrar al menos un ARTISTA antes de gestionar eventos.");
            return;
        }

        int option;
        do {
            ConsoleFormatter.printCentered(" GESTIÓN DE EVENTOS ", "=");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[1] Registrar Evento");
            ConsoleFormatter.printTabbed("[2] Mostrar Eventos");
            ConsoleFormatter.printTabbed("[3] Editar Evento");
            ConsoleFormatter.printTabbed("[4] Ver Detalle de Evento");
            ConsoleFormatter.printTabbed("[5] Eliminar Evento");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[6] Gestión de Zonas");
            ConsoleFormatter.printTabbed("[7] Gestión de Tipos de Entrada");
            ConsoleFormatter.printTabbed("[8] Generar Entradas");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> registerEvent();
                case 2 -> showEvents();
                case 3 -> updateEvent();
                case 4 -> showDetail();
                case 5 -> deleteEvent();
                case 6 -> zoneManagement();
                case 7 -> ticketTypeManagement();
                case 8 -> generateTicketsForEvent();
                case 9 -> ConsoleFormatter.printLeft("Regresando al menú principal...");
                default -> ConsoleFormatter.printLeft("Opción no válida.");
            }

            if (option != 9) InputValidator.pressEnterToContinue();

        } while (option != 9);
    }

    private static void registerEvent() {

        ConsoleFormatter.printCentered(" REGISTRAR NUEVO EVENTO ", "=");
        ConsoleFormatter.printLineBreak();

        String code = InputValidator.getCode("Ingrese código del evento: ");
        if (eventController.exists(code)) {
            ConsoleFormatter.printError("Ya existe un evento con ese código.");
            return;
        }

        String name = InputValidator.getNonEmptyString("Ingrese nombre del evento: ");
        String description = InputValidator.getOptionalString("Ingrese descripción (opcional): ");
        LocalDate date = InputValidator.getLATAMDate("Ingrese fecha del evento (dd/MM/yyyy): ");

        ConsoleFormatter.printInfo("Seleccione el LOCAL del evento:");
        List<Local> locals = localController.findAll();
        locals.forEach(local -> ConsoleFormatter.printTabbed("- " + local));

        String localCode = InputValidator.getCode("Código del local: ");
        Local selectedLocal = localController.findByCode(localCode);

        if (selectedLocal == null) {
            ConsoleFormatter.printError("Local no encontrado.");
            return;
        }

        ConsoleFormatter.printInfo("Seleccione el ARTISTA principal:");
        List<Artist> artists = artistController.findAll();
        artists.forEach(artist -> ConsoleFormatter.printTabbed("- " + artist));

        String artistCode = InputValidator.getCode("Código del artista: ");
        if (!artistController.exists(artistCode)) {
            ConsoleFormatter.printError("Artista no encontrado.");
            return;
        }

        Event event = new Event(code, name, description, date, localCode, artistCode);

        if (!InputValidator.getConfirmation("¿Confirmar registro del evento?")) {
            ConsoleFormatter.printInfo("Registro cancelado.");
            return;
        }

        if (!eventController.register(event)) {
            ConsoleFormatter.printError("No se pudo registrar el evento.");
            return;
        }

        ConsoleFormatter.printSuccess("Evento registrado exitosamente.");
        ConsoleFormatter.printInfo("Recuerde configurar sus zonas desde 'Gestión de Zonas'.");
    }

    private static void showEvents() {

        ConsoleFormatter.printCentered(" LISTA DE EVENTOS ", "=");
        ConsoleFormatter.printLineBreak();

        if (!eventController.hasEvents()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        List<Event> events = eventController.findAll();
        ConsoleFormatter.printList("Total de eventos", String.valueOf(events.size()), " ");
        ConsoleFormatter.printLine("-");

        for (int i = 0; i < events.size(); i++) {
            ConsoleFormatter.printTabbed((i + 1) + ". " + events.get(i));
        }
    }

    private static void updateEvent() {

        ConsoleFormatter.printCentered(" EDITAR EVENTO ", "=");
        ConsoleFormatter.printLineBreak();

        if (!eventController.hasEvents()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del evento: ");
        Event event = eventController.findByCode(code);

        if (event == null) {
            ConsoleFormatter.printError("Evento no encontrado.");
            return;
        }

        ConsoleFormatter.printInfo("Datos actuales:");
        showSummary(event);

        String newName = InputValidator.getOptionalString("Nuevo nombre [" + event.getName() + "]: ");
        if (!newName.isEmpty()) event.setName(newName);

        String newDescription = InputValidator.getOptionalString("Nueva descripcion [" + event.getDescription() + "]: ");
        if (!newName.isEmpty()) event.setDescription(newDescription);

        LocalDate newDate = InputValidator.getLATAMDate("Nueva fecha [" + event.getDate() + "]: ");
        if (newDate != null) event.setDate(newDate);

        ConsoleFormatter.printInfo("Datos actualizados:");
        showSummary(event);

        if (!InputValidator.getConfirmation("¿Confirmar cambios?")) {
            ConsoleFormatter.printInfo("Edición cancelada.");
            return;
        }

        if (eventController.update(event))
            ConsoleFormatter.printSuccess("Evento actualizado exitosamente.");
        else
            ConsoleFormatter.printError("No se pudo actualizar el evento.");
    }

    private static void deleteEvent() {

        ConsoleFormatter.printCentered(" ELIMINAR EVENTO ", "=");
        ConsoleFormatter.printLineBreak();

        if (!eventController.hasEvents()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del evento a eliminar: ");
        Event event = eventController.findByCode(code);

        if (event == null) {
            ConsoleFormatter.printError("Evento no encontrado.");
            return;
        }

        ConsoleFormatter.printInfo("Evento a eliminar:");
        showSummary(event);

        ConsoleFormatter.printWarning("Esta acción eliminará también todas las zonas asociadas.");

        if (!InputValidator.getConfirmation("¿Eliminar este evento?")) {
            ConsoleFormatter.printInfo("Eliminación cancelada.");
            return;
        }

        if (!zoneController.deleteAllByEvent(code)) {
            ConsoleFormatter.printError("No fue posible eliminar las zonas del evento.");
            return;
        }

        if (eventController.delete(code))
            ConsoleFormatter.printSuccess("Evento eliminado exitosamente.");
        else
            ConsoleFormatter.printError("No se pudo eliminar el evento.");
    }

    private static void showDetail() {

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printCentered(" DETALLE DEL EVENTO ", "=");

        if (!eventController.hasEvents()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del evento: ");
        Event event = eventController.findByCode(code);

        if (event == null) {
            ConsoleFormatter.printError("Evento no encontrado.");
            return;
        }

        showSummary(event);
    }

    private static void showSummary(Event event) {

        Artist artist = artistController.findByCode(event.getArtistCode());
        Local local = localController.findByCode(event.getLocalCode());

        List<Zone> zones = zoneController.findByEvent(event.getCode());
        List<TicketType> ticketTypes = ticketTypeController.findByEvent(event.getCode());

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printCentered(" DETALLES DEL EVENTO [" + event.getCode() + "] ", "=");
        ConsoleFormatter.printList(event.getName(), event.getDate().toString(), " ");
        ConsoleFormatter.printTabbed(event.getDescription());

        ConsoleFormatter.printCentered(" ARTISTA ", "=");
        ConsoleFormatter.printList(artist.getCode(), artist.getName(), " ");

        ConsoleFormatter.printCentered(" LOCAL [" + local.getCode() + "] ", "=");
        ConsoleFormatter.printList(local.getName(), String.valueOf(local.getCapacity()), " ");
        ConsoleFormatter.printList(local.getAddress(), "", " ");

        ConsoleFormatter.printCentered(" ZONAS DISPONIBLES ", "=");

        if (zones.isEmpty()) {
            ConsoleFormatter.printTabbed("No hay zonas configuradas.");
        } else {
            ConsoleFormatter.printList("Nombre", "Capacidad", " ");
            ConsoleFormatter.printLine("-");

            int capacity = 0;

            for (Zone zone : zones) {
                capacity += zone.getCapacity();

                String price = String.format("%.2f", zone.getPrice());
                String right = "(S/ " + price + ") " + zone.getCapacity();
                ConsoleFormatter.printList(zone.getName(), right, " ");
            }

            ConsoleFormatter.printLine("-");
            ConsoleFormatter.printList("Total Capacidad:", String.valueOf(capacity), " ");
        }

        ConsoleFormatter.printCentered(" TIPOS DE ENTRADA ", "=");

        if (ticketTypes.isEmpty()) {
            ConsoleFormatter.printTabbed("No hay tipos configurados.");
        } else {
            ConsoleFormatter.printLineBreak();
            for (TicketType zone : ticketTypes) {
                String right = "S/ " + String.format("%.2f", zone.getPrice());
                ConsoleFormatter.printList(zone.getName(), right, " ");
                ConsoleFormatter.printList(zone.getDescription(), "", " ");
                ConsoleFormatter.printLineBreak();
            }
        }

        ConsoleFormatter.printLine("=");
    }

    private static void zoneManagement() {

        Event event = getEventToManagement();
        if (event == null) return;

        ZoneView.showMenu(event);
    }

    private static void ticketTypeManagement() {

        Event event = getEventToManagement();
        if (event == null) return;

        TicketTypeView.showMenu(event);
    }

    private static Event getEventToManagement() {
        ConsoleFormatter.printInfo("Seleccione el Evento a gestionar:");
        List<Event> events = eventController.findAll();
        events.forEach(event -> ConsoleFormatter.printTabbed("- " + event));

        String eventCode = InputValidator.getCode("Código del evento: ");
        Event event = eventController.findByCode(eventCode);

        if (event == null) {
            ConsoleFormatter.printError("Evento no encontrado.");
            return null;
        }
        return event;
    }

    private static void generateTicketsForEvent() {
        ConsoleFormatter.printCentered(" GENERAR ENTRADAS ", "=");
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

        // Verificar si ya tiene entradas generadas
        if (ticketController.hasTickets(eventCode)) {
            int total = ticketController.countByEvent(eventCode);
            ConsoleFormatter.printWarning("Este evento ya tiene " + total + " entradas generadas.");
            return;
        }

        // Verificar que tenga zonas configuradas
        if (!zoneController.hasZones(eventCode)) {
            ConsoleFormatter.printError("Debe configurar al menos una zona antes de generar entradas.");
            return;
        }

        // Mostrar resumen
        List<Zone> zones = zoneController.findByEvent(eventCode);
        int totalCapacity = zoneController.getTotalCapacity(eventCode);

        ConsoleFormatter.printInfo("Evento: " + event.getName());
        ConsoleFormatter.printInfo("Zonas configuradas: " + zones.size());
        ConsoleFormatter.printInfo("Capacidad total: " + totalCapacity + " entradas");
        ConsoleFormatter.printLineBreak();

        for (Zone zone : zones) {
            ConsoleFormatter.printTabbed("- " + zone.getName() + ": " + zone.getCapacity() + " entradas");
        }

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printWarning("Esta acción generará " + totalCapacity + " entradas en la base de datos.");

        if (InputValidator.getConfirmation("¿Desea continuar?")) {
            if (ticketController.generateForEvent(eventCode)) {
                ConsoleFormatter.printSuccess("¡Entradas generadas exitosamente!");
                ConsoleFormatter.printInfo("El evento ahora está listo para la venta de entradas.");
            } else {
                ConsoleFormatter.printError("Ocurrió un error al generar las entradas.");
            }
        } else {
            ConsoleFormatter.printInfo("Generación de entradas cancelada.");
        }
    }
}
