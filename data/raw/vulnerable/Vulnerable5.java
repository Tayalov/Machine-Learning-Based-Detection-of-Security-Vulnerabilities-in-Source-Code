package vulnerable;

import javax.crypto.Cipher;

public class Vulnerable5 {
    public void encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    }
}