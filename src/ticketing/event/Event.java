package ticketing.event;

import java.time.LocalDate;

public class Event {
    private final String code;
    private String name;
    private String description;
    private LocalDate date;
    private String localCode;
    private String artistCode;

    public Event(String code, String name, String description, LocalDate date,
                 String localCode, String artistCode) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.date = date;
        this.localCode = localCode;
        this.artistCode = artistCode;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocalCode() {
        return localCode;
    }

    public String getArtistCode() {
        return artistCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public void setArtistCode(String artistCode) {
        this.artistCode = artistCode;
    }

    @Override
    public String toString() {
        return "[" + code + "] " + name;
    }
}
