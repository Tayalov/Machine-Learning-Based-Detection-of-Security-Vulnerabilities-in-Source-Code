package safe;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
public class Safe53 {
    public byte[] hash(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(data.getBytes(StandardCharsets.UTF_8));
    }
}
