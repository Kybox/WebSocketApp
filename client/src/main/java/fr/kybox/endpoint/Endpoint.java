package fr.kybox.endpoint;

import javax.websocket.*;
import java.io.PrintStream;
import java.util.Arrays;

@ClientEndpoint
public class Endpoint {

    private PrintStream printStream = new PrintStream(System.out);

    @OnOpen
    public void onOpen() {
        this.printStream.println(".. [OK]");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        this.printStream.println("\r" + message);
        this.printStream.print(session.getUserProperties().get("user") + " > ");
    }

    @OnClose
    public void onClose() {
        this.printStream.println("Connection closed");
    }

    @OnError
    public void onError(Throwable t) {
        this.printStream.println("ERROR : " + Arrays.toString(t.getStackTrace()));
    }
}
