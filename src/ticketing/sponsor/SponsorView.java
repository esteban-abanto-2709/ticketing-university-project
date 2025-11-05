package ticketing.sponsor;

import ticketing.utils.ConsoleFormatter;
import ticketing.utils.InputValidator;

import java.util.List;

public class SponsorView {

    private static final SponsorController controller = new SponsorController();

    public static void showMenu() {
        int option;

        do {
            ConsoleFormatter.printCentered(" GESTIÓN DE SPONSORS ", "=");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[1] Registrar Sponsor");
            ConsoleFormatter.printTabbed("[2] Mostrar Sponsors");
            ConsoleFormatter.printTabbed("[3] Editar Sponsor");
            ConsoleFormatter.printTabbed("[4] Ver Detalle de Sponsor");
            ConsoleFormatter.printTabbed("[5] Eliminar Sponsor");
            ConsoleFormatter.printLineBreak();
            ConsoleFormatter.printTabbed("[9] Volver al menú principal");
            ConsoleFormatter.printLine("-");

            option = InputValidator.getIntInRange("Seleccione una opción: ", 1, 9);

            switch (option) {
                case 1 -> registerSponsor();
                case 2 -> showSponsors();
                case 3 -> updateSponsor();
                case 4 -> showDetail();
                case 5 -> deleteSponsor();
                case 9 -> ConsoleFormatter.printRight("Regresando al menú principal...");
                default -> ConsoleFormatter.printRight("Opción no válida.");
            }

            if (option != 9) {
                InputValidator.pressEnterToContinue();
            }

        } while (option != 9);
    }

    private static void registerSponsor() {
        ConsoleFormatter.printCentered(" REGISTRAR NUEVO SPONSOR ", "=");
        ConsoleFormatter.printLineBreak();

        String code = InputValidator.getCode("Ingrese código del sponsor: ");

        if (controller.exists(code)) {
            ConsoleFormatter.printError("Ya existe un sponsor con el código: " + code);
            return;
        }

        String name = InputValidator.getNonEmptyString("Ingrese nombre del sponsor: ");
        String phone = InputValidator.getOptionalString("Ingrese teléfono del sponsor (opcional): ");
        String address = InputValidator.getOptionalString("Ingrese dirección del sponsor (opcional): ");

        Sponsor sponsor = new Sponsor(code, name, phone, address);

        showSummary(sponsor);

        if (InputValidator.getConfirmation("¿Confirma el registro del sponsor?")) {
            if (controller.register(sponsor)) {
                ConsoleFormatter.printSuccess("Sponsor registrado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo registrar el sponsor.");
            }
        } else {
            ConsoleFormatter.printInfo("Registro cancelado.");
        }
    }

    private static void showSponsors() {
        ConsoleFormatter.printCentered(" LISTA DE SPONSORS ", "=");
        ConsoleFormatter.printLineBreak();

        if (!controller.hasSponsors()) {
            ConsoleFormatter.printInfo("No hay sponsors registrados.");
            return;
        }

        List<Sponsor> sponsors = controller.findAll();
        ConsoleFormatter.printList("Total de sponsors", String.valueOf(sponsors.size()), " ");
        ConsoleFormatter.printLine("-");

        for (int i = 0; i < sponsors.size(); i++) {
            ConsoleFormatter.printTabbed((i + 1) + ". " + sponsors.get(i).toString());
        }
    }

    private static void updateSponsor() {
        ConsoleFormatter.printCentered(" EDITAR SPONSOR ", "=");
        ConsoleFormatter.printLineBreak();

        if (!controller.hasSponsors()) {
            ConsoleFormatter.printInfo("No hay sponsors registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del sponsor a editar: ");
        Sponsor sponsor = controller.findByCode(code);

        if (sponsor == null) {
            ConsoleFormatter.printError("No se encontró un sponsor con el código: " + code);
            return;
        }

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printRight("Datos actuales:");
        showSummary(sponsor);

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printInfo("Ingrese los nuevos datos (o presione Enter para mantener el actual):");
        ConsoleFormatter.printLineBreak();

        String newName = InputValidator.getOptionalString("Nuevo nombre [" + sponsor.getName() + "]: ");
        String newPhone = InputValidator.getOptionalString("Nuevo teléfono [" + (sponsor.getPhone() == null ? "-" : sponsor.getPhone()) + "]: ");
        String newAddress = InputValidator.getOptionalString("Nueva dirección [" + (sponsor.getAddress() == null ? "-" : sponsor.getAddress()) + "]: ");

        if (!newName.isEmpty()) sponsor.setName(newName);
        if (!newPhone.isEmpty()) sponsor.setPhone(newPhone);
        if (!newAddress.isEmpty()) sponsor.setAddress(newAddress);

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printRight("Nuevos datos:");
        showSummary(sponsor);

        if (InputValidator.getConfirmation("¿Confirma la edición del sponsor?")) {
            if (controller.update(sponsor)) {
                ConsoleFormatter.printSuccess("Sponsor actualizado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo actualizar el sponsor.");
            }
        } else {
            ConsoleFormatter.printInfo("Edición cancelada.");
        }
    }

    private static void showDetail() {
        ConsoleFormatter.printCentered(" DETALLE DE SPONSOR ", "=");
        ConsoleFormatter.printLineBreak();

        if (!controller.hasSponsors()) {
            ConsoleFormatter.printInfo("No hay sponsors registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del sponsor: ");
        Sponsor sponsor = controller.findByCode(code);

        if (sponsor == null) {
            ConsoleFormatter.printError("No se encontró un sponsor con el código: " + code);
        } else {
            showSummary(sponsor);
        }
    }

    private static void deleteSponsor() {
        ConsoleFormatter.printCentered(" ELIMINAR SPONSOR ", "=");
        ConsoleFormatter.printLineBreak();

        if (!controller.hasSponsors()) {
            ConsoleFormatter.printInfo("No hay sponsors registrados.");
            return;
        }

        String code = InputValidator.getCode("Ingrese código del sponsor a eliminar: ");
        Sponsor sponsor = controller.findByCode(code);

        if (sponsor == null) {
            ConsoleFormatter.printError("No se encontró un sponsor con el código: " + code);
            return;
        }

        ConsoleFormatter.printLineBreak();
        ConsoleFormatter.printRight("Sponsor a eliminar:");
        showSummary(sponsor);

        ConsoleFormatter.printWarning("Esta acción no se puede deshacer.");

        if (InputValidator.getConfirmation("¿Está seguro de eliminar este sponsor?")) {
            if (controller.delete(code)) {
                ConsoleFormatter.printSuccess("Sponsor eliminado exitosamente.");
            } else {
                ConsoleFormatter.printError("No se pudo eliminar el sponsor.");
            }
        } else {
            ConsoleFormatter.printInfo("Eliminación cancelada.");
        }
    }

    private static void showSummary(Sponsor sponsor) {
        ConsoleFormatter.printLine("=");
        ConsoleFormatter.printList("Código", sponsor.getCode(), " ");
        ConsoleFormatter.printList("Nombre", sponsor.getName(), " ");
        ConsoleFormatter.printList("Teléfono", sponsor.getPhone() == null ? "-" : sponsor.getPhone(), " ");
        ConsoleFormatter.printList("Dirección", sponsor.getAddress() == null ? "-" : sponsor.getAddress(), " ");
        ConsoleFormatter.printLine("=");
    }
}
