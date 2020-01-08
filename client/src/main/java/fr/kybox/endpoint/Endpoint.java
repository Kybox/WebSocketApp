package fr.kybox.endpoint;

import lombok.extern.java.Log;

import javax.websocket.*;

import static java.lang.String.format;

@Log
@ClientEndpoint
public class Endpoint {

    @OnOpen
    public void onOpen() {
        log.info(format("\r:: Connected, you can chat now.%n:: Type 'quit' to disconnect.%n"));
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info(format("\r%s %n%s > ", message, session.getUserProperties().get("user")));
    }

    @OnClose
    public void onClose() {
        log.info(format("Connection closed%n"));
    }

    @OnError
    public void onError(Throwable t) {
        log.info("ERROR : " + t.getMessage());
    }
}
