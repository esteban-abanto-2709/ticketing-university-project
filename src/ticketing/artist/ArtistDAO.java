package ticketing.artist;

import java.util.ArrayList;
import ticketing.interfaces.GenericDAO;
import java.util.List;

public class ArtistDAO implements GenericDAO<Artist> {

    // Temporal hasta implementar la base de datos
    private static List<Artist> artists = new ArrayList<>();

    @Override
    public boolean save(Artist entity) {
        Artist artist = findByCode(entity.getCode());
        if (artist == null) {
            return artists.add(entity);
        } else {
            return false;
        }
    }

    @Override
    public Artist findByCode(String code) {
        for (Artist artist : artists) {
            if (artist.getCode().equalsIgnoreCase(code)) {
                return artist;
            }
        }
        return null;
    }

    @Override
    public List<Artist> findAll() {
        return new ArrayList<>(artists);
    }

    @Override
    public boolean update(Artist entity) {
        Artist artist = findByCode(entity.getCode());
        if (artist == null) {
            return false;
        }

        artist.setName(entity.getName());
        return true;
    }

    @Override
    public boolean deleteByCode(String code) {
        return artists.removeIf(artist -> artist.getCode().equalsIgnoreCase(code));
    }
}
