package ticketing.event;

import ticketing.artist.Artist;
import ticketing.artist.ArtistController;

import ticketing.event.zone.Zone;
import ticketing.event.zone.ZoneController;
import ticketing.event.zone.ZoneView;

import ticketing.local.Local;
import ticketing.local.LocalController;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

import java.time.LocalDate;
import java.util.List;

public class EventView {

    private static final EventController eventController = new EventController();
    private static final ArtistController artistController = new ArtistController();
    private static final LocalController localController = new LocalController();
    private static final ZoneController zoneController = new ZoneController();

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
                case 9 -> ConsoleFormatter.print("Regresando al menú principal...");
                default -> System.out.println("Opción no válida.");
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

        ConsoleFormatter.printCentered(" DETALLE DEL EVENTO ", "=");
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

        showSummary(event);
    }

    private static void showSummary(Event event) {

        ConsoleFormatter.printLine("=");
        ConsoleFormatter.printList(event.getName(), event.getCode(), " ");
        ConsoleFormatter.printLine("=");

        ConsoleFormatter.printTabbed(event.getDescription());
        ConsoleFormatter.printLine("=");

        ConsoleFormatter.printList("Fecha", event.getDate().toString(), " ");
        ConsoleFormatter.printList("Local", event.getLocalCode(), " ");
        ConsoleFormatter.printList("Artista", event.getArtistCode(), " ");
        ConsoleFormatter.printLine("=");

        List<Zone> zones = zoneController.findByEvent(event.getCode());

        if (zones.isEmpty()) {
            ConsoleFormatter.printTabbed("No hay zonas configuradas.");
        } else {
            for (Zone zone : zones) {
                String price = String.format("%.2f", zone.getBasePrice());
                String right = "(S/ " + price + ") Cap: " + zone.getCapacity();
                ConsoleFormatter.printList(zone.getName(), right, " ");
            }
        }

        ConsoleFormatter.printLine("=");
    }

    private static void zoneManagement() {

        ConsoleFormatter.printInfo("Seleccione el Evento a gestionar:");
        List<Event> events = eventController.findAll();
        events.forEach(event -> ConsoleFormatter.printTabbed("- " + event));

        String eventCode = InputValidator.getCode("Código del evento: ");
        Event event = eventController.findByCode(eventCode);

        if (event == null) {
            ConsoleFormatter.printError("Evento no encontrado.");
            return;
        }

        ZoneView.showMenu(event);
    }
}
