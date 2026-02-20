package vulnerable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Vulnerable3 {
    public void query(Connection conn, String user) throws Exception {
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM users WHERE username='" + user + "'";
        ResultSet rs = stmt.executeQuery(sql);
    }
}