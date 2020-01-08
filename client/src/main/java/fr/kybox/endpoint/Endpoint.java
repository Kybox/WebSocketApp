package fr.kybox.endpoint;

import lombok.extern.java.Log;

import javax.websocket.*;
import java.util.Arrays;

import static java.lang.String.format;

@Log
@ClientEndpoint
public class Endpoint {

    @OnOpen
    public void onOpen() {
        log.info("\r:: Connected, you can chat now.\n");
        log.info(":: Type 'quit' to disconnect.\n");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info(format("\r%s %n", message));
        log.info(session.getUserProperties().get("user") + " > ");
    }

    @OnClose
    public void onClose() {
        log.info(format("Connection closed%n"));
    }

    @OnError
    public void onError(Throwable t) {
        log.info("ERROR : " + Arrays.toString(t.getStackTrace()));
    }
}
