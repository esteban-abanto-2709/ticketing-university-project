package ticketing.local;

import ticketing.database.DatabaseManager;
import ticketing.interfaces.GenericDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class LocalDAO implements GenericDAO<Local> {

    @Override
    public boolean save(Local local) {
        String sql = "INSERT INTO locals (code, name, address, capacity) VALUES (?, ?, ?, ?)";
        try {
            DatabaseManager.execute(sql,
                    local.getCode(),
                    local.getName(),
                    local.getAddress(),
                    local.getCapacity()
            );
            return true;
        } catch (Exception e) {
            System.err.println("[LocalDAO] Error saving local: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Local findByCode(String code) {
        String sql = "SELECT * FROM locals WHERE code = ?";
        try (ResultSet rs = DatabaseManager.query(sql, code)) {
            if (rs != null && rs.next()) {
                return new Local(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("capacity")
                );
            }
        } catch (SQLException e) {
            System.err.println("[LocalDAO] Error finding local by code: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Local> findAll() {
        String sql = "SELECT * FROM locals";
        List<Local> locales = new ArrayList<>();
        try (ResultSet rs = DatabaseManager.query(sql)) {
            while (rs != null && rs.next()) {
                locales.add(new Local(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("capacity")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[LocalDAO] Error listing locals: " + e.getMessage());
        }
        return locales;
    }

    @Override
    public boolean update(Local local) {
        String sql = "UPDATE locals SET name = ?, address = ?, capacity = ? WHERE code = ?";
        try {
            DatabaseManager.execute(sql,
                    local.getName(),
                    local.getAddress(),
                    local.getCapacity(),
                    local.getCode()
            );
            return true;
        } catch (Exception e) {
            System.err.println("[LocalDAO] Error updating local: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByCode(String code) {
        String sql = "DELETE FROM locals WHERE code = ?";
        try {
            DatabaseManager.execute(sql, code);
            return true;
        } catch (Exception e) {
            System.err.println("[LocalDAO] Error deleting local: " + e.getMessage());
            return false;
        }
    }
}
