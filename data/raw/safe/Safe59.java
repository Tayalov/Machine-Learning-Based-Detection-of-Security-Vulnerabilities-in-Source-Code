package safe;
import java.sql.*;
public class Safe59 {
    public void query(Connection conn, String user) throws Exception {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=?");
        ps.setString(1, user);
        ResultSet rs = ps.executeQuery();
    }
}
