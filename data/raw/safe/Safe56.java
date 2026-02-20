package safe;
import java.lang.ProcessBuilder;
public class Safe56 {
    public void run(String cmd, String arg) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(cmd, arg);
        pb.start();
    }
}