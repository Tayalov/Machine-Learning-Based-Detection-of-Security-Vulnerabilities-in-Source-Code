package safe;
import java.security.SecureRandom;
public class Safe54 {
    public int token() throws Exception {
        SecureRandom sr = SecureRandom.getInstanceStrong();
        return sr.nextInt();
    }
}