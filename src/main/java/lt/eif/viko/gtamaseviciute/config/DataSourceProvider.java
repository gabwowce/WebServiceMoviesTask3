package lt.eif.viko.gtamaseviciute.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceProvider {
    private static final HikariDataSource ds;

    static {
        HikariConfig cfg = new HikariConfig();
        cfg.setDriverClassName("com.mysql.cj.jdbc.Driver");
        cfg.setJdbcUrl("jdbc:mysql://localhost:3306/MoviesDB?useSSL=false&serverTimezone=UTC");
        cfg.setUsername("root");
        cfg.setPassword("12301");
        cfg.setMaximumPoolSize(10);
        ds = new HikariDataSource(cfg);
    }

    public static DataSource get() {
        return ds;
    }
}