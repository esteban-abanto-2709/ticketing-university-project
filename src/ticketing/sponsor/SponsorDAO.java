package ticketing.sponsor;

import ticketing.database.DatabaseManager;
import ticketing.interfaces.GenericDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class SponsorDAO implements GenericDAO<Sponsor> {

    @Override
    public boolean save(Sponsor sponsor) {
        String sql = "INSERT INTO sponsors (code, name, phone, address) VALUES (?, ?, ?, ?)";
        try {
            DatabaseManager.execute(sql,
                    sponsor.getCode(),
                    sponsor.getName(),
                    sponsor.getPhone(),
                    sponsor.getAddress()
            );
            System.out.println("[SponsorDAO] Auspiciador guardado: " + sponsor.getName());
            return true;
        } catch (Exception e) {
            System.err.println("[SponsorDAO] Error al guardar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Sponsor findByCode(String code) {
        String sql = "SELECT * FROM sponsors WHERE code = ?";
        try (ResultSet rs = DatabaseManager.query(sql, code)) {
            if (rs != null && rs.next()) {
                return new Sponsor(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            System.err.println("[SponsorDAO] Error al buscar: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Sponsor> findAll() {
        String sql = "SELECT * FROM sponsors";
        List<Sponsor> sponsors = new ArrayList<>();
        try (ResultSet rs = DatabaseManager.query(sql)) {
            while (rs != null && rs.next()) {
                sponsors.add(new Sponsor(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[SponsorDAO] Error al listar: " + e.getMessage());
        }
        return sponsors;
    }

    @Override
    public boolean update(Sponsor sponsor) {
        String sql = "UPDATE sponsors SET name = ?, phone = ?, address = ? WHERE code = ?";
        try {
            DatabaseManager.execute(sql,
                    sponsor.getName(),
                    sponsor.getPhone(),
                    sponsor.getAddress(),
                    sponsor.getCode()
            );
            return true;
        } catch (Exception e) {
            System.err.println("[SponsorDAO] Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByCode(String code) {
        String sql = "DELETE FROM sponsors WHERE code = ?";
        try {
            DatabaseManager.execute(sql, code);
            return true;
        } catch (Exception e) {
            System.err.println("[SponsorDAO] Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}
