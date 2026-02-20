package vulnerable;

import java.security.MessageDigest;

public class Vulnerable2 {
    public byte[] hash(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return md.digest(password.getBytes());
    }
}