package ticketing.artist;

import ticketing.utils.ConsoleFormatter;

import java.util.List;

public class ArtistController {

    private final ArtistDAO artistDAO = new ArtistDAO();

    public boolean addArtist(String code, String name) {
        if (code == null || code.isBlank()) {
            ConsoleFormatter.printError("El código no puede estar vacío.");
            return false;
        }

        String normalized = normalizeCode(code);
        Artist artist = new Artist(normalized);
        artist.setName(name);

        boolean success = artistDAO.save(artist);
        if (!success) {
            ConsoleFormatter.printWaring("Ya existe un artista con ese código.");
        }
        return success;
    }

    public List<Artist> listArtists() {
        return artistDAO.findAll();
    }

    public Artist getArtist(String code) {
        if (code == null) return null;
        return artistDAO.findByCode(normalizeCode(code));
    }

    public boolean deleteArtist(String code) {
        if (code == null || code.isBlank()) {
            return false;
        }
        return artistDAO.deleteByCode(normalizeCode(code));
    }

    public boolean isCodeAlreadyExists(String code) {
        if (code == null || code.isBlank()) return false;
        return artistDAO.findByCode(normalizeCode(code)) != null;
    }

    public boolean registerArtist(Artist artist) {
        if (artist == null || artist.getCode() == null || artist.getCode().isBlank()) {
            ConsoleFormatter.printError("Código inválido para el artista.");
            return false;
        }
        // Normalizar el código antes de guardar
        String normalized = normalizeCode(artist.getCode());
        Artist existing = artistDAO.findByCode(normalized);
        if (existing != null) {
            ConsoleFormatter.printWaring("Ya existe un artista con ese código.");
            return false;
        }

        // Crear una nueva instancia para evitar aliasing con objetos externos
        Artist toSave = new Artist(normalized);
        toSave.setName(artist.getName());
        return artistDAO.save(toSave);
    }

    public List<Artist> getAllArtists() {
        return artistDAO.findAll();
    }

    public boolean hasArtists() {
        List<Artist> all = artistDAO.findAll();
        return all != null && !all.isEmpty();
    }

    public Artist getArtistByCode(String code) {
        return getArtist(code);
    }

    public boolean updateArtist(Artist artist) {
        if (artist == null || artist.getCode() == null || artist.getCode().isBlank()) {
            return false;
        }
        // Normalizar código antes de actualizar
        Artist toUpdate = new Artist(normalizeCode(artist.getCode()));
        toUpdate.setName(artist.getName());
        return artistDAO.update(toUpdate);
    }

    public boolean deleteArtistByCode(String code) {
        if (code == null || code.isBlank()) return false;
        return artistDAO.deleteByCode(normalizeCode(code));
    }

    private String normalizeCode(String code) {
        return (code == null) ? null : code.trim().toUpperCase();
    }
}