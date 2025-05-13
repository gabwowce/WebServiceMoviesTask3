package lt.eif.viko.gtamaseviciute.util;

import lt.eif.viko.gtamaseviciute.config.DataSourceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DaoUtils {

    /**
     * Grąžina paruoštą PreparedStatement objektą su aktyvia DB jungtimi.
     * Jį turi uždaryti pats naudotojas (try-with-resources).
     */
    public static PreparedStatement prepare(String sql) throws SQLException {
        Connection conn = DataSourceProvider.get().getConnection();
        return conn.prepareStatement(sql);
    }

    /**
     * Jei reikia gauti sugeneruotą ID (pvz. auto-increment `id`), naudok šį metodą.
     */
    public static PreparedStatement prepareWithGeneratedKeys(String sql) throws SQLException {
        Connection conn = DataSourceProvider.get().getConnection();
        return conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    }


}
