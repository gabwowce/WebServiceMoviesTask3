package lt.eif.viko.gtamaseviciute.dao;


import lt.eif.viko.gtamaseviciute.config.DataSourceProvider;
import lt.eif.viko.gtamaseviciute.model.Actor;
import lt.eif.viko.gtamaseviciute.model.Movie;
import lt.eif.viko.gtamaseviciute.util.DaoUtils;
import lt.eif.viko.gtamaseviciute.util.OperationResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MovieDao {
    private final ActorDao actorDao = new ActorDao();
    private final MovieActorDao movieActorDao = new MovieActorDao();

    public OperationResult<Movie> findById(int movieId) {
        String sql =
        "SELECT m.id, m.title, m.release_year, m.genre, m.rating," +
              " a.id AS actor_id, a.name, a.birth_year" +
       " FROM movies m" +
       " LEFT JOIN movie_actors ma ON m.id = ma.movie_id "+
       " LEFT JOIN actors a ON ma.actor_id = a.id" +
       " WHERE m.id = ?";

        try (PreparedStatement ps = DaoUtils.prepare(sql)) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                Movie movie = null;
                List<Actor> actorList = new ArrayList<>();
                while (rs.next()) {
                    if (movie == null) {
                        movie = new Movie();
                        movie.setId(rs.getInt("id"));
                        movie.setTitle(rs.getString("title"));
                        movie.setReleaseYear(rs.getInt("release_year"));
                        movie.setGenre(rs.getString("genre"));
                        movie.setRating(rs.getFloat("rating"));
                    }
                    int actorId = rs.getInt("actor_id");
                    if (actorId > 0) {
                        Actor actor = new Actor();
                        actor.setId(actorId);
                        actor.setName(rs.getString("name"));
                        actor.setBirthYear(rs.getInt("birth_year"));
                        actorList.add(actor);
                    }
                }
                if (movie != null) {
                    movie.setActors(actorList);
                    return OperationResult.success(movie, "Movie found.");
                } else {
                    return OperationResult.failure("Movie not found.");
                }
            }
        } catch (SQLException e) {
            return OperationResult.failure("SQL Error: " + e.getMessage());
        }
    }


    public OperationResult<List<Movie>> findAll() {
        String sql =
        "SELECT m.id, m.title, m.release_year, m.genre, m.rating,"+
              " a.id AS actor_id, a.name, a.birth_year"+
        "FROM movies m"+
        "LEFT JOIN movie_actors ma ON m.id = ma.movie_id"+
        "LEFT JOIN actors a ON ma.actor_id = a.id";

        Map<Integer, Movie> movieMap = new LinkedHashMap<>();
        try (PreparedStatement ps = DaoUtils.prepare(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int movieId = rs.getInt("id");
                Movie movie = movieMap.get(movieId);

                if (movie == null) {
                    movie = new Movie();
                    movie.setId(movieId);
                    movie.setTitle(rs.getString("title"));
                    movie.setReleaseYear(rs.getInt("release_year"));
                    movie.setGenre(rs.getString("genre"));
                    movie.setRating(rs.getFloat("rating"));
                    movie.setActors(new ArrayList<>());
                    movieMap.put(movieId, movie);
                }

                int actorId = rs.getInt("actor_id");
                if (actorId > 0) {
                    Actor actor = new Actor();
                    actor.setId(actorId);
                    actor.setName(rs.getString("name"));
                    actor.setBirthYear(rs.getInt("birth_year"));
                    movie.getActors().add(actor);
                }
            }

            return OperationResult.success(new ArrayList<>(movieMap.values()), "Movies retrieved successfully.");

        } catch (SQLException e) {
            return OperationResult.failure("SQL Error: " + e.getMessage());
        }
    }


    public OperationResult<String> create(Movie movie) {
        String sql = "INSERT INTO movies (title, release_year, genre, rating) VALUES (?,?,?,?)";
        try (Connection conn = DataSourceProvider.get().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // insert filmą
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getReleaseYear());
            ps.setString(3, movie.getGenre());
            ps.setFloat(4, movie.getRating());
            ps.executeUpdate();

            int movieId;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) return OperationResult.failure("Movie ID not returned");
                movieId = keys.getInt(1);
            }

            // aktoriai
            if (movie.getActors() != null) {
                for (Actor a : movie.getActors()) {
                    int actorId = actorDao.getOrCreate(a, conn);
                    movieActorDao.link(movieId, actorId, conn);
                }
            }
            return OperationResult.success(null, "Movie & actors saved");
        } catch (SQLException e) {
            return OperationResult.failure("SQL error: " + e.getMessage());
        }
    }


}