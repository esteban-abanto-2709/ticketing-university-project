package ticketing.local;

import ticketing.database.DatabaseManager;
import ticketing.interfaces.GenericDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalDAO implements GenericDAO<Local> {

    @Override
    public boolean save(Local entity) {
        String sql = "INSERT INTO locals (code, name, address, capacity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, entity.getCode());
            stmt.setString(2, entity.getName());
            stmt.setString(3, entity.getAddress());
            stmt.setInt(4, entity.getCapacity());
            stmt.executeUpdate();
            System.out.println("[LocalDAO] Local guardado correctamente: " + entity.getName());
            return true;
        } catch (SQLException e) {
            System.err.println("[LocalDAO] Error al guardar local: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Local findByCode(String code) {
        String sql = "SELECT * FROM locals WHERE code = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.closeOnCompletion();
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Local(
                            rs.getString("code"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getInt("capacity")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[LocalDAO] Error al buscar local por código: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Local> findAll() {
        List<Local> locales = new ArrayList<>();
        String sql = "SELECT * FROM locals";
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
            System.err.println("[LocalDAO] Error al listar locales: " + e.getMessage());
        }
        return locales;
    }

    @Override
    public boolean update(Local local) {
        String sql = "UPDATE locals SET name = ?, address = ?, capacity = ? WHERE code = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, local.getName());
            stmt.setString(2, local.getAddress());
            stmt.setInt(3, local.getCapacity());
            stmt.setString(4, local.getCode());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[LocalDAO] Error al actualizar local: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByCode(String code) {
        String sql = "DELETE FROM locals WHERE code = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, code);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[LocalDAO] Error al eliminar local: " + e.getMessage());
            return false;
        }
    }
}
