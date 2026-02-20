package vulnerable;

import java.util.Random;

public class Vulnerable7 {
    public int generateToken() {
        Random rand = new Random();
        return rand.nextInt();
    }
}
