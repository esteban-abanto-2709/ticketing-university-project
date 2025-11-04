package ticketing.event;

public class Event {
    private final String code;     // identificador único
    private String name;           // nombre del evento
    private String description;    // descripción opcional
    private String date;           // fecha del evento
    private String localCode;      // FK - local donde se realiza
    private String artistCode;     // FK - artista o grupo
    private String status;         // estado del evento (programado, cancelado, etc.)

    public Event(String code, String name, String description, String date,
                 String localCode, String artistCode, String status) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.date = date;
        this.localCode = localCode;
        this.artistCode = artistCode;
        this.status = status;
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

    public String getDate() {
        return date;
    }

    public String getLocalCode() {
        return localCode;
    }

    public String getArtistCode() {
        return artistCode;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public void setArtistCode(String artistCode) {
        this.artistCode = artistCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[" + code + "] " + name + " (" + status + ") - " + date;
    }
}
