package safe;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
public class Safe60 {
    public void connect() throws Exception {
        URL url = new URL("https://example.com");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    }
}
