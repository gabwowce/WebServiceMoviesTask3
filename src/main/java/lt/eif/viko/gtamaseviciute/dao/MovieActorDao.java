package lt.eif.viko.gtamaseviciute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieActorDao {
    public void link(int movieId, int actorId, Connection conn) throws SQLException {
        String sql = "INSERT IGNORE INTO movie_actors (movie_id, actor_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setInt(2, actorId);
            ps.executeUpdate();
        }
    }
}
