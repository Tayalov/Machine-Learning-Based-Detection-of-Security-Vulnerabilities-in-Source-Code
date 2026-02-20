package vulnerable;

public class Vulnerable6 {
    public void ping(String ip) throws Exception {
        Runtime.getRuntime().exec("ping " + ip);
    }
}
