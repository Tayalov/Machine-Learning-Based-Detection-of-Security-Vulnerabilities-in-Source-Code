package safe;
import java.nio.file.*;
public class Safe58 {
    public void writeFile(String filename, String data) throws Exception {
        Path path = Paths.get("/tmp/").resolve(filename).normalize();
        Files.write(path, data.getBytes());
    }
}