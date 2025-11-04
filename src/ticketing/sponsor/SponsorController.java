package ticketing.sponsor;

import ticketing.utils.ConsoleFormatter;
import java.util.List;

public class SponsorController {

    private final SponsorDAO sponsorDAO = new SponsorDAO();

    public boolean register(Sponsor sponsor) {
        if (sponsor == null || sponsor.getCode() == null || sponsor.getCode().isBlank()) {
            ConsoleFormatter.printError("Código inválido.");
            return false;
        }

        String normalized = normalizeCode(sponsor.getCode());

        if (sponsorDAO.findByCode(normalized) != null) {
            ConsoleFormatter.printWarning("Ya existe un sponsor con ese código.");
            return false;
        }

        Sponsor toSave = new Sponsor(
                normalized,
                sponsor.getName(),
                sponsor.getPhone(),
                sponsor.getAddress()
        );

        boolean result = sponsorDAO.save(toSave);
        if (result)
            ConsoleFormatter.printSuccess("Sponsor registrado correctamente.");
        else
            ConsoleFormatter.printError("Error al registrar sponsor.");

        return result;
    }

    public Sponsor findByCode(String code) {
        if (code == null || code.isBlank()) return null;
        return sponsorDAO.findByCode(normalizeCode(code));
    }

    public List<Sponsor> findAll() {
        return sponsorDAO.findAll();
    }

    public boolean update(Sponsor sponsor) {
        if (sponsor == null || sponsor.getCode() == null || sponsor.getCode().isBlank()) {
            ConsoleFormatter.printError("Código inválido.");
            return false;
        }

        Sponsor toUpdate = new Sponsor(
                normalizeCode(sponsor.getCode()),
                sponsor.getName(),
                sponsor.getPhone(),
                sponsor.getAddress()
        );

        boolean result = sponsorDAO.update(toUpdate);
        if (result)
            ConsoleFormatter.printSuccess("Sponsor actualizado correctamente.");
        else
            ConsoleFormatter.printError("No se pudo actualizar el sponsor.");

        return result;
    }

    public boolean delete(String code) {
        if (code == null || code.isBlank()) {
            ConsoleFormatter.printError("Código inválido.");
            return false;
        }

        boolean result = sponsorDAO.deleteByCode(normalizeCode(code));
        if (result)
            ConsoleFormatter.printSuccess("Sponsor eliminado correctamente.");
        else
            ConsoleFormatter.printError("No se encontró el sponsor para eliminar.");

        return result;
    }

    public boolean exists(String code) {
        if (code == null || code.isBlank()) return false;
        return sponsorDAO.findByCode(normalizeCode(code)) != null;
    }

    public boolean hasSponsors() {
        List<Sponsor> all = sponsorDAO.findAll();
        return all != null && !all.isEmpty();
    }

    private String normalizeCode(String code) {
        return (code == null) ? null : code.trim().toUpperCase();
    }
}
