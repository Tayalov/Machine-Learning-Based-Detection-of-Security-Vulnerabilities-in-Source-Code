package vulnerable;

import java.security.MessageDigest;

public class Vulnerable1 {
    public byte[] hash(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(password.getBytes());
    }
}
