package ticketing.artist;

public class Artist {

    private final String code;
    private String name;

    public Artist(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[" + code + "] " + name;
    }
}