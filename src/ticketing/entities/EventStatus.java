package ticketing.entities;

public enum EventStatus {
    PROGRAMADO("Programado"),
    REPROGRAMADO("Reprogramado"),
    CANCELADO("Cancelado"),
    REALIZADO("Realizado");

    private final String descripcion;

    EventStatus(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}