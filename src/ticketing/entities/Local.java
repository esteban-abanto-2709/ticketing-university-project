package ticketing.entities;

import java.util.ArrayList;
import java.util.List;

public class Local {
    
    private String codigo;
    private String nombre;
    private String direccion;
    private List<Zone> zonas;

    public Local(String codigo, String nombre, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.zonas = new ArrayList<>();
    }

    public void agregarZona(Zone zona) {
        zonas.add(zona);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<Zone> getZonas() {
        return zonas;
    }
    
    public int getCapacidadTotal() {
        int total = 0;
        for (int i = 0; i < zonas.size(); i++) {
            total += zonas.get(i).getCapacidad();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Local: ").append(nombre)
          .append(" (Código: ").append(codigo).append(")\n")
          .append("Dirección: ").append(direccion).append("\n")
          .append("Zonas:\n");
        for (Zone z : zonas) {
            sb.append("   - ").append(z.toString()).append("\n");
        }
        return sb.toString();
    }
}
