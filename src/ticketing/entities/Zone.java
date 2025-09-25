package ticketing.entities;

public class Zone {
    
    private String nombre;
    private int capacidad;

    public Zone(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    @Override
    public String toString() {
        return "Zona: " + nombre + " | Capacidad: " + capacidad;
    }
}
