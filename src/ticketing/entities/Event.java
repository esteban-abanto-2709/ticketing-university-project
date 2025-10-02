package ticketing.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    private final String code;
    private String name;
    private String artista;
    private String codeLocal;
    private LocalDate fecha;
    private LocalTime hora;
    private EventStatus estado;
    private String descripcion;

    public Event(String code) {
        this.code = code;
        this.estado = EventStatus.PROGRAMADO;
    }

    public Event(String code, String name, String artista, String codeLocal,
                 LocalDate fecha, LocalTime hora, String descripcion) {
        this.code = code;
        this.name = name;
        this.artista = artista;
        this.codeLocal = codeLocal;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = EventStatus.PROGRAMADO;
        this.descripcion = descripcion;
    }

    // --- Getters ---
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getArtista() {
        return artista;
    }

    public String getCodeLocal() {
        return codeLocal;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public EventStatus getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // --- Setters (excepto código) ---
    public void setName(String name) {
        this.name = name;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public void setCodeLocal(String codeLocal) {
        this.codeLocal = codeLocal;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setEstado(EventStatus estado) {
        this.estado = estado;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // --- Métodos de lógica ---
    public boolean isActivo() {
        return estado == EventStatus.PROGRAMADO || estado == EventStatus.REPROGRAMADO;
    }

    public boolean puedeSerReprogramado() {
        return estado == EventStatus.PROGRAMADO || estado == EventStatus.REPROGRAMADO;
    }

    public boolean puedeSerCancelado() {
        return estado != EventStatus.REALIZADO && estado != EventStatus.CANCELADO;
    }

    public boolean puedeSerFinalizado() {
        return estado == EventStatus.PROGRAMADO || estado == EventStatus.REPROGRAMADO;
    }
}