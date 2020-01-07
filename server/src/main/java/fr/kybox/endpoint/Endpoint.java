package fr.kybox.endpoint;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@ServerEndpoint(value = "/chat/{user}")
public class Endpoint {

    static Map<String, Session> userList = new ConcurrentHashMap<>();

    // Injection failed
    private PrintStream printStream = new PrintStream(System.out);

    @OnOpen
    public void onOpen(@PathParam("user") String user, Session session) {
        userList.put(user, session);
        this.printStream.println(format("%s join the chat room", user));
        userList.values().forEach(s -> {
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
        this.printStream.println(format("%s1 > %s2", user, message));
        userList.values().forEach(s -> {
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
    public void onClose(@PathParam("user") String user) {
        userList.remove(user);
        this.printStream.println(format("%s left the chat room.", user));
        userList.values().forEach(s -> {
            try {
                s.getBasicRemote().sendText("Server > " + format("%s left the chat room.", user));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnError
    public void onError(Throwable throwable) {
        this.printStream.println("SERVER ERROR : " + throwable.toString());
    }
}
