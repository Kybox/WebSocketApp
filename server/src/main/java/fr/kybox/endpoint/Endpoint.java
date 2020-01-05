package fr.kybox.endpoint;

import fr.kybox.ServerApplication;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@ServerEndpoint(value = "/chat/{user}")
public class Endpoint {

    static Map<String, Session> userList = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam(ServerApplication.PATH_PARAM) String user, Session session) {
        userList.put(user, session);
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
    public void onMessage(@PathParam(ServerApplication.PATH_PARAM) String user, String message, Session session) {
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
    public void onClose(@PathParam(ServerApplication.PATH_PARAM) String user) {
        userList.remove(user);
        userList.values().forEach(s -> {
            try {
                s.getBasicRemote().sendText("Server > " + format("%s left the chat room.", user));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnError
    public void onError(Throwable throwable){
        System.out.println("SERVER ERROR : " + throwable.getCause().getMessage());
    }
}
