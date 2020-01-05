package fr.kybox.endpoint;

import fr.kybox.service.ChatService;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.io.PrintStream;
import java.util.Arrays;

@ClientEndpoint
public class Endpoint {

    private PrintStream writer = new PrintStream(System.out);

    @OnMessage
    public void onMessage(String message) {
        writer.println("\r" + message);
        writer.print(ChatService.getUserName() + " > ");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        writer.println("ERROR : " + Arrays.toString(t.getStackTrace()));
    }
}
