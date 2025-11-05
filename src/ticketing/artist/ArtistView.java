package ticketing.artist;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

import java.util.List;

public class ArtistView {

    private static final ArtistController controller = new ArtistController();

    public static void showMenu() {
        int option;

        do {
            ConsoleFormatter.printCentered(" GESTIÓN DE ARTISTAS ", "=");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[1] Registrar Artista");
            ConsoleFormatter.printTabbed("[2] Mostrar Artistas");
            ConsoleFormatter.printTabbed("[3] Editar Artista");
            ConsoleFormatter.printTabbed("[4] Ver Detalle de Artista");
            ConsoleFormatter.printTabbed("[5] Eliminar Artista");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> registerArtist();
                case 2 -> showArtists();
                case 3 -> updateArtist();
                case 4 -> showDetail();
                case 5 -> deleteArtist();
                case 9 -> ConsoleFormatter.printRight("Regresando al menú principal...");
                default -> ConsoleFormatter.printRight("Opción no válida.");
            }

            if (option != 9) {
                InputValidator.pressEnterToContinue();
            }

        } while (option != 9);
    }

    private static void registerArtist() {
        ConsoleFormatter.printCentered(" REGISTRAR NUEVO ARTISTA ", "=");
        ConsoleFormatter.printLineBreak();

        String code = InputValidator.getCode("Ingrese código del artista: ");

        if (controller.exists(code)) {
            ConsoleFormatter.printError("Ya existe un artista con el código: " + code);
            return;
        }

        String name = InputValidator.getNonEmptyString("Ingrese nombre del artista: ");

        Artist artist = new Artist(code);
        artist.setName(name);

        showSummary(artist);

        if (InputValidator.getConfirmation("¿Confirma el registro del artista?")) {
            if (controller.register(artist)) {
                ConsoleFormatter.printSuccess("Artista registrado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo registrar el artista.");
            }
        } else {
            ConsoleFormatter.printInfo("Registro cancelado.");
        }
    }

    private static void showArtists() {
        ConsoleFormatter.printCentered(" LISTA DE ARTISTAS ", "=");
        ConsoleFormatter.printLineBreak();

        if (!controller.hasArtists()) {
            ConsoleFormatter.printInfo("No hay artistas registrados.");
            return;
        }

        List<Artist> artists = controller.findAll();
        ConsoleFormatter.printList("Total de artistas", String.valueOf(artists.size()), " ");
        ConsoleFormatter.printLine("-");

        for (int i = 0; i < artists.size(); i++) {
            ConsoleFormatter.printTabbed((i + 1) + ". " + artists.get(i).toString());
        }
    }

    private static void updateArtist() {
        ConsoleFormatter.printCentered(" EDITAR ARTISTA ", "=");
        ConsoleFormatter.printLineBreak();

        if (!controller.hasArtists()) {
            ConsoleFormatter.printInfo("No hay artistas registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del artista a editar: ");
        Artist artist = controller.findByCode(code);

        if (artist == null) {
            ConsoleFormatter.printError("No se encontró un artista con el código: " + code);
            return;
        }

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printRight("Datos actuales:");
        showSummary(artist);

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printInfo("Ingrese los nuevos datos (o presione Enter para mantener el actual):");
        ConsoleFormatter.printLineBreak();

        String newName = InputValidator.getOptionalString("Nuevo nombre [" + artist.getName() + "]: ");
        if (!newName.isEmpty()) {
            artist.setName(newName);
        }

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printRight("Nuevos datos:");
        showSummary(artist);

        if (InputValidator.getConfirmation("¿Confirma la edición del artista?")) {
            if (controller.update(artist)) {
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
        ConsoleFormatter.printLineBreak();

        if (!controller.hasArtists()) {
            ConsoleFormatter.printInfo("No hay artistas registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del artista a eliminar: ");
        Artist artist = controller.findByCode(code);

        if (artist == null) {
            ConsoleFormatter.printError("No se encontró un artista con el código: " + code);
            return;
        }

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printRight("Artista a eliminar:");
        showSummary(artist);

        ConsoleFormatter.printWarning("Esta acción no se puede deshacer.");

        if (InputValidator.getConfirmation("¿Está seguro de eliminar este artista?")) {
            if (controller.delete(code)) {
                ConsoleFormatter.printSuccess("Artista eliminado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo eliminar el artista.");
            }
        } else {
            ConsoleFormatter.printInfo("Eliminación cancelada.");
        }
    }

    private static void showDetail() {
        ConsoleFormatter.printCentered(" DETALLE DE ARTISTA ", "=");
        ConsoleFormatter.printLineBreak();

        if (!controller.hasArtists()) {
            ConsoleFormatter.printInfo("No hay artistas registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del artista: ");
        Artist artist = controller.findByCode(code);

        if (artist == null) {
            ConsoleFormatter.printError("No se encontró un artista con el código: " + code);
        } else {
            showSummary(artist);
        }
    }

    private static void showSummary(Artist artist) {
        ConsoleFormatter.printLine("=");
        ConsoleFormatter.printList("Código", artist.getCode(), " ");
        ConsoleFormatter.printList("Nombre", artist.getName(), " ");
        ConsoleFormatter.printLine("=");
    }
}