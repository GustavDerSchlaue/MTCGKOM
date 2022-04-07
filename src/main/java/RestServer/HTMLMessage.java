package RestServer;

import java.util.Map;

public class HTMLMessage {

    Map<String,String> Header;
    String Body;

    HTMLMessage(Map<String,String> header, String body)
    {
        Header=header;
        Body=body;

    }
}
