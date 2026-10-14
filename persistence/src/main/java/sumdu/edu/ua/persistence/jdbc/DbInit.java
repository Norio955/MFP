package sumdu.edu.ua.persistence.jdbc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DbInit {

    private DbInit() {}

    public static void init() {
        try (Connection c = (Connection) Db.get();
             Statement st = c.createStatement()) {

            // Читаємо schema.sql з ресурсів
            var in = DbInit.class.getClassLoader().getResourceAsStream("schema.sql");
            if (in == null) {
                throw new IllegalStateException("schema.sql not found in resources");
            }

            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            for (String cmd : sql.split(";")) {
                if (!cmd.isBlank()) {
                    st.execute(cmd);
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("DB schema init failed", e);
        }
    }
}