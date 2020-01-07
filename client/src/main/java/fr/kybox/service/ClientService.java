package fr.kybox.service;

import javax.websocket.Session;

public interface ClientService {

    void clearScreen();

    String askUsername();

    Session connect(String username);

    void startChatting(Session session, String username);
}
