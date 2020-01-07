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
        this.printStream.println(format("%s join the chat room", user));
        session.getOpenSessions().forEach(s -> {
            if (!s.getId().equals(session.getId())) {
                try {
                    s.getAsyncRemote().sendText("Server > " + format("%s join the chat room", user));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnMessage
    public void onMessage(@PathParam("user") String user, String message, Session session) {
        this.printStream.println(format("%s > %s", user, message));
        session.getOpenSessions().forEach(s -> {
            if (!s.getId().equals(session.getId())) {
                try {
                    s.getAsyncRemote().sendText(user + " > " + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClose
    public void onClose(@PathParam("user") String user, Session session) {
        this.printStream.println(format("%s left the chat room.", user));
        session.getOpenSessions().forEach(s -> {
            if (!s.getId().equals(session.getId())) {
                try {
                    s.getAsyncRemote().sendText("Server > " + format("%s left the chat room.", user));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnError
    public void onError(Throwable throwable) {
        this.printStream.println("SERVER ERROR : " + throwable.toString());
    }
}
