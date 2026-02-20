package vulnerable;

import java.net.HttpURLConnection;
import java.net.URL;

public class Vulnerable8 {
    public void connect() throws Exception {
        URL url = new URL("http://example.com");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    }
}