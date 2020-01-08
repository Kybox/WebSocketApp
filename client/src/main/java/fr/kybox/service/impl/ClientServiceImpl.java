package fr.kybox.service.impl;

import fr.kybox.endpoint.Endpoint;
import fr.kybox.service.ClientService;
import lombok.extern.java.Log;
import org.glassfish.tyrus.client.ClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;
import java.util.concurrent.Future;

import static java.lang.String.format;

@Log
@Service
public class ClientServiceImpl implements ClientService {

    private final Scanner scanner;

    @Autowired
    public ClientServiceImpl(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void clearScreen() {
        try {
            if (System.getProperty("os.name").startsWith("Windows"))
                Runtime.getRuntime().exec("cls");
            else
                Runtime.getRuntime().exec("clear");
        } catch (Exception e) {
            for (int i = 0; i < 100; i++)
                log.info(format("%n"));
        }
    }

    @Override
    public String askUsername() {
        log.info(format(":: Enter your name :%n"));
        return this.scanner.nextLine();
    }

    @Override
    public Session connect(String username) {
        log.info(format(":: Hi %s, please wait.%n", username));
        Future<Session> session;
        ClientManager clientManager = ClientManager.createClient();
        clientManager.setAsyncSendTimeout(10000);
        try {
            session = clientManager.asyncConnectToServer(
                    Endpoint.class,
                    new URI("ws://localhost:8080/chat/" + username));

            this.loading(session);
            session.get().getUserProperties().put("user", username);
            return session.get();

        } catch (Exception e) {
            log.info("\n");
            log.info(format("Error : %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public void startChatting(Session session, String username) {
        String prompt = format("%s > ", username);
        String msg;
        do {
            log.info("\r" + prompt);
            msg = this.scanner.nextLine();
            try {
                session.getAsyncRemote().sendText(msg);
            } catch (Exception e) {
                e.printStackTrace();
                log.info(format("Error : %s", e.getMessage()));
                e.printStackTrace();
                msg = "quit";
            }
        } while (!msg.equalsIgnoreCase("quit"));
        try {
            session.close();
            log.info("Session closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loading(Future<Session> session) {
        boolean loading = true;
        while (loading) {
            try {
                log.info("\r[.  ] Server connection attempt.");
                Thread.sleep(300);
                log.info("\r[ . ] Server connection attempt.");
                Thread.sleep(300);
                log.info("\r[  .] Server connection attempt.");
                Thread.sleep(300);
                log.info("\r");
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                if (session.isDone()) {
                    loading = false;
                }
            }
        }
    }
}
