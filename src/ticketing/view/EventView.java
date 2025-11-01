package ticketing.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ticketing.controller.EventController;
import ticketing.entities.Event;
import ticketing.entities.EventStatus;
import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

public class EventView {

    private static final EventController eventController = new EventController();

    public static void mostrarMenu() {
        if(!eventController.hasLocals()){
            ConsoleFormatter.printError("No existen locales creados.");
            InputValidator.pressEnterToContinue();
            return;
        }

        int opcion;

        do {
            ConsoleFormatter.printCentered(" GESTIÓN DE EVENTOS ", "=");
            System.out.println();
            ConsoleFormatter.printTabbed("[1] Registrar Evento");
            ConsoleFormatter.printTabbed("[2] Mostrar Eventos");
            ConsoleFormatter.printTabbed("[3] Ver Detalle de Evento");
            ConsoleFormatter.printTabbed("[4] Editar Evento");
            ConsoleFormatter.printTabbed("[5] Reprogramar Evento");
            ConsoleFormatter.printTabbed("[6] Cancelar Evento");
            ConsoleFormatter.printTabbed("[7] Finalizar Evento");
            System.out.println();
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            opcion = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (opcion) {
                case 1 -> registrarEvento();
                case 2 -> mostrarEventos();
                case 3 -> verDetalleEvento();
                case 4 -> editarEvento();
                case 5 -> reprogramarEvento();
                case 6 -> cancelarEvento();
                case 7 -> finalizarEvento();
                case 9 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

            if (opcion != 9) {
                InputValidator.pressEnterToContinue();
            }

        } while (opcion != 9);
    }

    private static void registrarEvento() {
        ConsoleFormatter.printCentered(" REGISTRAR NUEVO EVENTO ", "=");
        System.out.println();

        // Pedir código del evento
        String codigo = InputValidator.getCode("Ingrese código del evento: ");

        // Verificar que el código no exista
        if (eventController.isCodeAlreadyExists(codigo)) {
            ConsoleFormatter.printError("Ya existe un evento con el código: " + codigo);
            return;
        }

        // Pedir datos básicos
        String nombre = InputValidator.getNonEmptyString("Ingrese nombre del evento: ");
        String artista = InputValidator.getNonEmptyString("Ingrese nombre del artista: ");

        // Pedir código del local y verificar que existe
        String codigoLocal;
        while (true) {
            codigoLocal = InputValidator.getCode("Ingrese código del local: ");
            if (eventController.localExists(codigoLocal)) {
                break;
            }
            ConsoleFormatter.printError("No existe un local con el código: " + codigoLocal);
        }

        LocalDate fecha = InputValidator.getDate("Ingrese fecha del evento (dd/MM/yyyy): ");
        LocalTime hora = InputValidator.getTime("Ingrese hora del evento (HH:mm): ");

        // Pedir descripción (opcional)
        String descripcion = InputValidator.getOptionalString("Ingrese descripción del evento (opcional): ");

        // Crear evento
        Event evento = new Event(codigo, nombre, artista, codigoLocal, fecha, hora, descripcion);

        // Mostrar resumen
        mostrarResumenEvento(evento);

        // Confirmar registro
        if (InputValidator.getConfirmation("¿Confirma el registro del evento?")) {
            if (eventController.registerEvento(evento)) {
                ConsoleFormatter.printSuccess("Evento registrado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo registrar el evento.");
            }
        } else {
            ConsoleFormatter.printInfo("Registro cancelado.");
        }
    }

    private static void mostrarEventos() {
        ConsoleFormatter.printCentered(" LISTA DE EVENTOS ", "=");
        System.out.println();

        if (!eventController.hasEventos()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        // Opción de filtrado
        System.out.println();
        ConsoleFormatter.printTabbed("[1] Mostrar todos");
        ConsoleFormatter.printTabbed("[2] Filtrar por estado");
        ConsoleFormatter.printTabbed("[3] Buscar por nombre");
        System.out.println();

        int filtro = InputValidator.getIntInRange("Seleccione opción: ", 1, 3);

        List<Event> eventos;

        switch (filtro) {
            case 2 -> {
                EventStatus estado = seleccionarEstado();
                eventos = eventController.getEventosByEstado(estado);
                ConsoleFormatter.printInfo("Mostrando eventos con estado: " + estado.getDescripcion());
            }
            case 3 -> {
                String nombre = InputValidator.getNonEmptyString("Ingrese nombre a buscar: ");
                eventos = eventController.searchEventosByName(nombre);
                ConsoleFormatter.printInfo("Resultados de búsqueda: " + eventos.size());
            }
            default -> eventos = eventController.getAllEventos();
        }

        if (eventos.isEmpty()) {
            ConsoleFormatter.printInfo("No se encontraron eventos con los criterios especificados.");
            return;
        }

        ConsoleFormatter.printList("Total de eventos", String.valueOf(eventos.size()), " ");
        ConsoleFormatter.printLine("-");

        for (int i = 0; i < eventos.size(); i++) {
            Event evento = eventos.get(i);
            System.out.println();
            ConsoleFormatter.printTabbed((i + 1) + ". [" + evento.getCode() + "] " + evento.getName());
            ConsoleFormatter.printList("   Artista", evento.getArtista(), " ", 1);
            ConsoleFormatter.printList("   Fecha", formatearFecha(evento.getFecha()) + " " + formatearHora(evento.getHora()), " ", 1);
            ConsoleFormatter.printList("   Estado", evento.getEstado().getDescripcion(), " ", 1);
        }
    }

    private static void verDetalleEvento() {
        ConsoleFormatter.printCentered(" DETALLE DE EVENTO ", "=");
        System.out.println();

        if (!eventController.hasEventos()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del evento: ");
        Event evento = eventController.getEventByCode(codigo);

        if (evento == null) {
            ConsoleFormatter.printError("No se encontró un evento con el código: " + codigo);
        } else {
            mostrarResumenEvento(evento);
        }
    }

    private static void editarEvento() {
        ConsoleFormatter.printCentered(" EDITAR EVENTO ", "=");
        System.out.println();

        if (!eventController.hasEventos()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del evento a editar: ");
        Event evento = eventController.getEventByCode(codigo);

        if (evento == null) {
            ConsoleFormatter.printError("No se encontró un evento con el código: " + codigo);
            return;
        }

        // Verificar que el evento se pueda editar
        if (evento.getEstado() == EventStatus.REALIZADO) {
            ConsoleFormatter.printError("No se puede editar un evento que ya fue realizado.");
            return;
        }

        if (evento.getEstado() == EventStatus.CANCELADO) {
            ConsoleFormatter.printError("No se puede editar un evento cancelado.");
            return;
        }

        // Mostrar datos actuales
        System.out.println("\nDatos actuales:");
        mostrarResumenEvento(evento);
        System.out.println();

        // Pedir nuevos datos
        ConsoleFormatter.printInfo("Ingrese los nuevos datos (o presione Enter para mantener el actual):");
        System.out.println();

        String nuevoNombre = InputValidator.getOptionalString("Nuevo nombre [" + evento.getName() + "]: ");
        if (!nuevoNombre.isEmpty()) {
            evento.setName(nuevoNombre);
        }

        String nuevoArtista = InputValidator.getOptionalString("Nuevo artista [" + evento.getArtista() + "]: ");
        if (!nuevoArtista.isEmpty()) {
            evento.setArtista(nuevoArtista);
        }

        if (InputValidator.getConfirmation("¿Desea cambiar el local?")) {
            String nuevoLocal;
            while (true) {
                nuevoLocal = InputValidator.getCode("Nuevo código del local: ");
                if (eventController.localExists(nuevoLocal)) {
                    evento.setCodeLocal(nuevoLocal);
                    break;
                }
                ConsoleFormatter.printError("No existe un local con el código: " + nuevoLocal);
            }
        }

        String nuevaDescripcion = InputValidator.getOptionalString("Nueva descripción [" +
                (evento.getDescripcion() != null ? evento.getDescripcion() : "sin descripción") + "]: ");
        if (!nuevaDescripcion.isEmpty()) {
            evento.setDescripcion(nuevaDescripcion);
        }

        // Mostrar resumen de cambios
        System.out.println("\nNuevos datos:");
        mostrarResumenEvento(evento);

        if (InputValidator.getConfirmation("¿Confirma la edición del evento?")) {
            if (eventController.updateEvento(evento)) {
                ConsoleFormatter.printSuccess("Evento actualizado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo actualizar el evento.");
            }
        } else {
            ConsoleFormatter.printInfo("Edición cancelada.");
        }
    }

    private static void reprogramarEvento() {
        ConsoleFormatter.printCentered(" REPROGRAMAR EVENTO ", "=");
        System.out.println();

        if (!eventController.hasEventos()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del evento a reprogramar: ");
        Event evento = eventController.getEventByCode(codigo);

        if (evento == null) {
            ConsoleFormatter.printError("No se encontró un evento con el código: " + codigo);
            return;
        }

        // Verificar que el evento puede ser reprogramado
        if (!evento.puedeSerReprogramado()) {
            ConsoleFormatter.printError("Este evento no puede ser reprogramado (Estado: " +
                    evento.getEstado().getDescripcion() + ")");
            return;
        }

        // Mostrar datos actuales
        System.out.println("\nEvento a reprogramar:");
        mostrarResumenEvento(evento);
        System.out.println();

        // Pedir nueva fecha y hora
        ConsoleFormatter.printInfo("Ingrese la nueva fecha y hora:");
        LocalDate nuevaFecha = InputValidator.getDate("Nueva fecha (dd/MM/yyyy): ");
        LocalTime nuevaHora = InputValidator.getTime("Nueva hora (HH:mm): ");

        // Confirmar
        System.out.println();
        ConsoleFormatter.printInfo("Nueva programación:");
        ConsoleFormatter.printList("  Fecha", formatearFecha(nuevaFecha), " ", 1);
        ConsoleFormatter.printList("  Hora", formatearHora(nuevaHora), " ", 1);
        System.out.println();

        if (InputValidator.getConfirmation("¿Confirma la reprogramación del evento?")) {
            if (eventController.reprogramarEvento(codigo, nuevaFecha, nuevaHora)) {
                ConsoleFormatter.printSuccess("Evento reprogramado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo reprogramar el evento.");
            }
        } else {
            ConsoleFormatter.printInfo("Reprogramación cancelada.");
        }
    }

    private static void cancelarEvento() {
        ConsoleFormatter.printCentered(" CANCELAR EVENTO ", "=");
        System.out.println();

        if (!eventController.hasEventos()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del evento a cancelar: ");
        Event evento = eventController.getEventByCode(codigo);

        if (evento == null) {
            ConsoleFormatter.printError("No se encontró un evento con el código: " + codigo);
            return;
        }

        // Verificar que el evento puede ser cancelado
        if (!evento.puedeSerCancelado()) {
            ConsoleFormatter.printError("Este evento no puede ser cancelado (Estado: " +
                    evento.getEstado().getDescripcion() + ")");
            return;
        }

        // Mostrar datos del evento
        System.out.println("\nEvento a cancelar:");
        mostrarResumenEvento(evento);
        System.out.println();

        ConsoleFormatter.printWarning("Esta acción no se puede deshacer.");

        if (InputValidator.getConfirmation("¿Está seguro de cancelar este evento?")) {
            if (eventController.cancelarEvento(codigo)) {
                ConsoleFormatter.printSuccess("Evento cancelado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo cancelar el evento.");
            }
        } else {
            ConsoleFormatter.printInfo("Cancelación de evento abortada.");
        }
    }

    private static void finalizarEvento() {
        ConsoleFormatter.printCentered(" FINALIZAR EVENTO ", "=");
        System.out.println();

        if (!eventController.hasEventos()) {
            ConsoleFormatter.printInfo("No hay eventos registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del evento a finalizar: ");
        Event evento = eventController.getEventByCode(codigo);

        if (evento == null) {
            ConsoleFormatter.printError("No se encontró un evento con el código: " + codigo);
            return;
        }

        // Verificar que el evento puede ser finalizado
        if (!evento.puedeSerFinalizado()) {
            ConsoleFormatter.printError("Este evento no puede ser finalizado (Estado: " +
                    evento.getEstado().getDescripcion() + ")");
            return;
        }

        // Mostrar datos del evento
        System.out.println("\nEvento a finalizar:");
        mostrarResumenEvento(evento);
        System.out.println();

        if (InputValidator.getConfirmation("¿Confirma que este evento ya fue realizado?")) {
            if (eventController.finalizarEvento(codigo)) {
                ConsoleFormatter.printSuccess("Evento marcado como realizado.");
            } else {
                ConsoleFormatter.printError("No se pudo finalizar el evento.");
            }
        } else {
            ConsoleFormatter.printInfo("Finalización cancelada.");
        }
    }

    // Métodos auxiliares

    private static void mostrarResumenEvento(Event evento) {
        ConsoleFormatter.printLine("=");
        ConsoleFormatter.printList("Código", evento.getCode(), " ");
        ConsoleFormatter.printList("Nombre", evento.getName(), " ");
        ConsoleFormatter.printList("Artista", evento.getArtista(), " ");
        ConsoleFormatter.printList("Local", evento.getCodeLocal(), " ");
        ConsoleFormatter.printList("Fecha", formatearFecha(evento.getFecha()), " ");
        ConsoleFormatter.printList("Hora", formatearHora(evento.getHora()), " ");
        ConsoleFormatter.printList("Estado", evento.getEstado().getDescripcion(), " ");
        if (evento.getDescripcion() != null && !evento.getDescripcion().isEmpty()) {
            ConsoleFormatter.printList("Descripción", evento.getDescripcion(), " ");
        }
        ConsoleFormatter.printLine("=");
    }

    private static String formatearFecha(LocalDate fecha) {
        if (fecha == null) return "Sin fecha";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formatter);
    }

    private static String formatearHora(LocalTime hora) {
        if (hora == null) return "Sin hora";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return hora.format(formatter);
    }

    private static EventStatus seleccionarEstado() {
        System.out.println();
        ConsoleFormatter.printTabbed("[1] Programado");
        ConsoleFormatter.printTabbed("[2] Reprogramado");
        ConsoleFormatter.printTabbed("[3] Cancelado");
        ConsoleFormatter.printTabbed("[4] Realizado");
        System.out.println();

        int opcion = InputValidator.getIntInRange("Seleccione estado: ", 1, 4);

        return switch (opcion) {
            case 1 -> EventStatus.PROGRAMADO;
            case 2 -> EventStatus.REPROGRAMADO;
            case 3 -> EventStatus.CANCELADO;
            case 4 -> EventStatus.REALIZADO;
            default -> EventStatus.PROGRAMADO;
        };
    }
}