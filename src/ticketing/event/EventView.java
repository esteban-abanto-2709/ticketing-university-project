package ticketing.event;

import ticketing.artist.Artist;
import ticketing.artist.ArtistController;
import ticketing.local.Local;
import ticketing.local.LocalController;
import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

import java.util.List;

public class EventView {

    private static final EventController eventController = new EventController();
    private static final ArtistController artistController = new ArtistController();
    private static final LocalController localController = new LocalController();

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
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> registerEvent();
                case 2 -> showEvents();
                case 3 -> updateEvent();
                case 4 -> showDetail();
                case 5 -> deleteEvent();
                case 9 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

            if (option != 9) {
                InputValidator.pressEnterToContinue();
            }

        } while (option != 9);
    }

    private static void registerEvent() {
        ConsoleFormatter.printCentered(" REGISTRAR NUEVO EVENTO ", "=");
        ConsoleFormatter.printLineBreak();

        String code = InputValidator.getCode("Ingrese código del evento: ");
        if (eventController.exists(code)) {
            ConsoleFormatter.printError("Ya existe un evento con el código: " + code);
            return;
        }

        String name = InputValidator.getNonEmptyString("Ingrese nombre del evento: ");
        String description = InputValidator.getOptionalString("Ingrese descripción (opcional): ");
        String date = InputValidator.getNonEmptyString("Ingrese fecha del evento (ejemplo: 2025-11-20): ");

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printInfo("Seleccione el LOCAL para este evento:");
        List<Local> locals = localController.findAll();
        locals.forEach(local -> ConsoleFormatter.printTabbed("- " + local));
        String localCode = InputValidator.getCode("Ingrese código del local: ");

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printInfo("Seleccione el ARTISTA principal:");
        List<Artist> artists = artistController.findAll();
        artists.forEach(artist -> ConsoleFormatter.printTabbed("- " + artist));
        String artistCode = InputValidator.getCode("Ingrese código del artista: ");

        String status = InputValidator.getNonEmptyString("Ingrese estado del evento (ejemplo: ACTIVO / CANCELADO / FINALIZADO): ");

        Event event = new Event(code, name, description, date, localCode, artistCode, status);

        showSummary(event);

        if (InputValidator.getConfirmation("¿Confirma el registro del evento?")) {
            if (eventController.register(event)) {
                ConsoleFormatter.printSuccess("Evento registrado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo registrar el evento.");
            }
        } else {
            ConsoleFormatter.printInfo("Registro cancelado.");
        }
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
            ConsoleFormatter.printTabbed((i + 1) + ". " + events.get(i).toString());
        }
    }

    private static void updateEvent() {
        ConsoleFormatter.printCentered(" EDITAR EVENTO ", "=");
        ConsoleFormatter.printLineBreak();

        if (!eventController.hasEvents()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del evento a editar: ");
        Event event = eventController.findByCode(code);

        if (event == null) {
            ConsoleFormatter.printError("No se encontró un evento con el código: " + code);
            return;
        }

        System.out.println("\nDatos actuales:");
        showSummary(event);
        ConsoleFormatter.printLineBreak();

        ConsoleFormatter.printInfo("Ingrese los nuevos datos (o presione Enter para mantener el actual):");
        ConsoleFormatter.printLineBreak();

        String newName = InputValidator.getOptionalString("Nuevo nombre [" + event.getName() + "]: ");
        if (!newName.isEmpty()) event.setName(newName);

        String newDate = InputValidator.getOptionalString("Nueva fecha [" + event.getDate() + "]: ");
        if (!newDate.isEmpty()) event.setDate(newDate);

        String newStatus = InputValidator.getOptionalString("Nuevo estado [" + event.getStatus() + "]: ");
        if (!newStatus.isEmpty()) event.setStatus(newStatus);

        System.out.println("\nNuevos datos:");
        showSummary(event);

        if (InputValidator.getConfirmation("¿Confirma la edición del evento?")) {
            if (eventController.update(event)) {
                ConsoleFormatter.printSuccess("Evento actualizado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo actualizar el evento.");
            }
        } else {
            ConsoleFormatter.printInfo("Edición cancelada.");
        }
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
            ConsoleFormatter.printError("No se encontró un evento con el código: " + code);
            return;
        }

        System.out.println("\nEvento a eliminar:");
        showSummary(event);
        ConsoleFormatter.printWarning("Esta acción no se puede deshacer.");

        if (InputValidator.getConfirmation("¿Está seguro de eliminar este evento?")) {
            if (eventController.delete(code)) {
                ConsoleFormatter.printSuccess("Evento eliminado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo eliminar el evento.");
            }
        } else {
            ConsoleFormatter.printInfo("Eliminación cancelada.");
        }
    }

    private static void showDetail() {
        ConsoleFormatter.printCentered(" DETALLE DE EVENTO ", "=");
        ConsoleFormatter.printLineBreak();

        if (!eventController.hasEvents()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del evento: ");
        Event event = eventController.findByCode(code);

        if (event == null) {
            ConsoleFormatter.printError("No se encontró un evento con el código: " + code);
        } else {
            showSummary(event);
        }
    }

    private static void showSummary(Event event) {
        ConsoleFormatter.printLine("=");
        ConsoleFormatter.printList("Código", event.getCode(), " ");
        ConsoleFormatter.printList("Nombre", event.getName(), " ");
        ConsoleFormatter.printList("Fecha", event.getDate(), " ");
        ConsoleFormatter.printList("Local", event.getLocalCode(), " ");
        ConsoleFormatter.printList("Artista", event.getArtistCode(), " ");
        ConsoleFormatter.printList("Estado", event.getStatus(), " ");
        ConsoleFormatter.printList("Descripción", event.getDescription(), " ");
        ConsoleFormatter.printLine("=");
    }
}
