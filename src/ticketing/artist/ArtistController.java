package ticketing.artist;

import ticketing.utils.ConsoleFormatter;

import java.util.List;

public class ArtistController {

    private final ArtistDAO artistDAO = new ArtistDAO();

    public boolean register(Artist artist) {
        if (artist == null || artist.getCode() == null || artist.getCode().isBlank()) {
            ConsoleFormatter.printError("Código inválido.");
            return false;
        }

        String normalized = normalizeCode(artist.getCode());

        if (artistDAO.findByCode(normalized) != null) {
            ConsoleFormatter.printWarning("Ya existe un artista con ese código.");
            return false;
        }

        Artist toSave = new Artist(normalized);
        toSave.setName(artist.getName());
        return artistDAO.save(toSave);
    }

    public Artist findByCode(String code) {
        if (code == null || code.isBlank()) return null;
        return artistDAO.findByCode(normalizeCode(code));
    }

    public List<Artist> findAll() {
        return artistDAO.findAll();
    }

    public boolean update(Artist artist) {
        if (artist == null || artist.getCode() == null || artist.getCode().isBlank()) {
            return false;
        }

        Artist toUpdate = new Artist(normalizeCode(artist.getCode()));
        toUpdate.setName(artist.getName());
        return artistDAO.update(toUpdate);
    }

    public boolean delete(String code) {
        if (code == null || code.isBlank()) return false;
        return artistDAO.deleteByCode(normalizeCode(code));
    }

    public boolean exists(String code) {
        if (code == null || code.isBlank()) return false;
        return artistDAO.findByCode(normalizeCode(code)) != null;
    }

    public boolean hasArtists() {
        List<Artist> all = artistDAO.findAll();
        return all != null && !all.isEmpty();
    }

    private String normalizeCode(String code) {
        return (code == null) ? null : code.trim().toUpperCase();
    }
}