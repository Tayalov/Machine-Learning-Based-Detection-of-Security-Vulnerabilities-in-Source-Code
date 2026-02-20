package vulnerable;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Vulnerable9 {
    public void store(String password) throws Exception {
        Files.write(Paths.get("password.txt"), password.getBytes());
    }
}