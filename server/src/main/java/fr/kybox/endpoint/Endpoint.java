package fr.kybox.endpoint;

import lombok.extern.java.Log;
import org.glassfish.tyrus.core.MaxSessions;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import static java.lang.String.format;

@Log
@MaxSessions(10)
@ServerEndpoint(value = "/chat/{user}")
public class Endpoint {

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
        log.info("SERVER ERROR : " + throwable.getMessage());
    }

    private void sendMessage(Session session, String message, String user, boolean fromServer) {
        if (fromServer) log.info(format("%s %s", user, message));
        else log.info(format("%s > %s", user, message));
        session.getOpenSessions().forEach(s -> {
            if (!s.getId().equals(session.getId())) {
                try {
                    if (fromServer) s.getAsyncRemote().sendText(format("Server > %s %s", user, message));
                    else s.getAsyncRemote().sendText(format("%s > %s", user, message));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
