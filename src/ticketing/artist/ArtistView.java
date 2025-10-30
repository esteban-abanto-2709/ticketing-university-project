package ticketing.artist;

import java.util.List;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

public class ArtistView {

    private static final ArtistController artistController = new ArtistController();

    public static void showMenu() {
        int opcion;
        do {
            ConsoleFormatter.printCentered(" GESTIÓN DE ARTISTAS ", "=");
            System.out.println();
            ConsoleFormatter.printTabbed("[1] Registrar Artista");
            ConsoleFormatter.printTabbed("[2] Mostrar Artistas");
            ConsoleFormatter.printTabbed("[3] Editar Artista");
            ConsoleFormatter.printTabbed("[4] Ver Detalle de Artista");
            ConsoleFormatter.printTabbed("[5] Eliminar Artista");
            System.out.println();
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            opcion = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (opcion) {
                case 1 -> registerArtist();
                case 2 -> mostrarArtistas();
                case 3 -> updateArtist();
                case 4 -> verDetalleArtista();
                case 5 -> deleteArtist();
                case 9 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción no válida.");
            }

            if (opcion != 9) {
                InputValidator.pressEnterToContinue();
            }

        } while (opcion != 9);
    }

    private static void registerArtist() {
        ConsoleFormatter.printCentered(" REGISTRAR NUEVO ARTISTA ", "=");
        System.out.println();

        String code = InputValidator.getCode("Ingrese código del artista: ");

        if (artistController.isCodeAlreadyExists(code)) {
            ConsoleFormatter.printError("Ya existe un artista con el código: " + code);
            return;
        }

        String name = InputValidator.getNonEmptyString("Ingrese nombre del artista: ");

        Artist artist = new Artist(code);
        artist.setName(name);

        mostrarResumenArtista(artist);

        if (InputValidator.getConfirmation("¿Confirma el registro del artista?")) {
            if (artistController.registerArtist(artist)) {
                ConsoleFormatter.printSuccess("Artista registrado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo registrar el artista.");
            }
        } else {
            ConsoleFormatter.printInfo("Registro cancelado.");
        }
    }

    private static void mostrarArtistas() {
        ConsoleFormatter.printCentered(" LISTA DE ARTISTAS ", "=");
        System.out.println();

        if (!artistController.hasArtists()) {
            ConsoleFormatter.printInfo("No hay artistas registrados.");
            return;
        }

        List<Artist> artists = artistController.getAllArtists();
        ConsoleFormatter.printList("Total de artistas", String.valueOf(artists.size()), " ");
        ConsoleFormatter.printLine("-");

        for (int i = 0; i < artists.size(); i++) {
            Artist artist = artists.get(i);
            ConsoleFormatter.printTabbed((i + 1) + ". " + artist.toString());
        }
    }

    private static void updateArtist() {
        ConsoleFormatter.printCentered(" EDITAR ARTISTA ", "=");
        System.out.println();

        if (!artistController.hasArtists()) {
            ConsoleFormatter.printInfo("No hay artistas registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del artista a editar: ");
        Artist artist = artistController.getArtistByCode(code);

        if (artist == null) {
            ConsoleFormatter.printError("No se encontró un artista con el código: " + code);
            return;
        }

        System.out.println("\nDatos actuales:");
        mostrarResumenArtista(artist);
        System.out.println();

        ConsoleFormatter.printInfo("Ingrese los nuevos datos (o presione Enter para mantener el actual):");
        System.out.println();

        String newName = InputValidator.getOptionalString("Nuevo nombre [" + artist.getName() + "]: ");
        if (!newName.isEmpty()) {
            artist.setName(newName);
        }

        System.out.println("\nNuevos datos:");
        mostrarResumenArtista(artist);

        if (InputValidator.getConfirmation("¿Confirma la edición del artista?")) {
            if (artistController.updateArtist(artist)) {
                ConsoleFormatter.printSuccess("Artista actualizado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo actualizar el artista.");
            }
        } else {
            ConsoleFormatter.printInfo("Edición cancelada.");
        }
    }

    private static void deleteArtist() {
        ConsoleFormatter.printCentered(" ELIMINAR ARTISTA ", "=");
        System.out.println();

        if (!artistController.hasArtists()) {
            ConsoleFormatter.printInfo("No hay artistas registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del artista a eliminar: ");
        Artist artist = artistController.getArtistByCode(codigo);

        if (artist == null) {
            ConsoleFormatter.printError("No se encontró un artista con el código: " + codigo);
            return;
        }

        System.out.println("\nArtista a eliminar:");
        mostrarResumenArtista(artist);

        ConsoleFormatter.printWaring("Esta acción no se puede deshacer.");

        if (InputValidator.getConfirmation("¿Está seguro de eliminar este artista?")) {
            if (artistController.deleteArtist(codigo)) {
                ConsoleFormatter.printSuccess("Artista eliminado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo eliminar el artista.");
            }
        } else {
            ConsoleFormatter.printInfo("Eliminación cancelada.");
        }
    }

    private static void verDetalleArtista() {
        ConsoleFormatter.printCentered(" DETALLE DE ARTISTA ", "=");
        System.out.println();

        if (!artistController.hasArtists()) {
            ConsoleFormatter.printInfo("No hay artistas registrados.");
            return;
        }

        String codigo = InputValidator.getCode("Ingrese código del artista: ");
        Artist artist = artistController.getArtistByCode(codigo);

        if (artist == null) {
            ConsoleFormatter.printError("No se encontró un artista con el código: " + codigo);
        } else {
            mostrarResumenArtista(artist);
        }
    }

    private static void mostrarResumenArtista(Artist artist) {
        ConsoleFormatter.printLine("=");
        ConsoleFormatter.printList("Código", artist.getCode(), " ");
        ConsoleFormatter.printList("Nombre", artist.getName(), " ");
        ConsoleFormatter.printLine("=");
    }
}
