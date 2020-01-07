package fr.kybox.endpoint;

import org.glassfish.tyrus.core.MaxSessions;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.PrintStream;

import static java.lang.String.format;

@MaxSessions(10)
@ServerEndpoint(value = "/chat/{user}")
public class Endpoint {

    // Injection failed
    private PrintStream printStream = new PrintStream(System.out);

    @OnOpen
    public void onOpen(@PathParam("user") String user, Session session) {
        this.sendMessage(session, "join the chat room.", user, true);
    }

    @OnMessage
    public void onMessage(@PathParam("user") String user, String message, Session session) {
        this.sendMessage(session, message, user, false);
    }

    @OnClose
    public void onClose(@PathParam("user") String user, Session session) {
        this.sendMessage(session, "left the chat room.", user, true);
    }

    @OnError
    public void onError(Throwable throwable) {
        this.printStream.println("SERVER ERROR : " + throwable.toString());
    }

    private void sendMessage(Session session, String message, String user, boolean fromServer){
        if(fromServer) this.printStream.println(format("%s %s", user, message));
        else this.printStream.println(format("%s > %s", user, message));
        session.getOpenSessions().forEach(s -> {
            if (!s.getId().equals(session.getId())) {
                try {
                    if(fromServer) {
                        s.getAsyncRemote().sendText(format("Server > %s %s", user, message));
                    } else {
                        s.getAsyncRemote().sendText(format("%s > %s", user, message));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
