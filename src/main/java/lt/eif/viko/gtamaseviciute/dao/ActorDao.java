package lt.eif.viko.gtamaseviciute.dao;

import lt.eif.viko.gtamaseviciute.model.Actor;

import java.sql.*;

public class ActorDao {
    public int getOrCreate(Actor actor, Connection conn) throws SQLException {
        String sel = "SELECT id FROM actors WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sel)) {
            ps.setString(1, actor.getName());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        String ins = "INSERT INTO actors (name, birth_year) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, actor.getName());
            ps.setInt(2, actor.getBirthYear());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Cannot create actor " + actor.getName());
    }
}
