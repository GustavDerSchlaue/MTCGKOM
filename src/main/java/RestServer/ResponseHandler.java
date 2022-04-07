package RestServer;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class ResponseHandler {


    private final Socket client;

    public ResponseHandler(Socket listener) {

        client = listener;

    }

    public void respond(String message, int number) throws IOException {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 " + number + "\r\n");
        response.append("ContentType: plain/text\r\n");
        response.append("Content-Length: " + message.length());
        response.append("\r\n\r\n" + message + "\r\n\r\n");

        BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        buffer.write(response.toString());
        buffer.flush();

    }
}
