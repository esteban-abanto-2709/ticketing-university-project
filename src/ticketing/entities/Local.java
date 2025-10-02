package ticketing.entities;

public class Local {

    private final String codigo;
    private String nombre;
    private String direccion;
    private Zone[] zonas;

    public Local(String codigo) {
        this.codigo = codigo;
    }

    public Local(String codigo, String nombre, String direccion, Zone[] zonas) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.zonas = zonas;
    }

    // --- Getters ---
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public Zone[] getZonas() {
        return zonas;
    }

    // --- Setters (excepto código) ---
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setZonas(Zone[] zonas) {
        this.zonas = zonas;
    }

    // --- Método de lógica ---
    public int getCapacidadTotal() {
        int total = 0;
        if (zonas != null) {
            for (Zone zona : zonas) {
                if (zona != null) {
                    total += zona.getCapacidad();
                }
            }
        }
        return total;
    }
}
