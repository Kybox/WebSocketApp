package fr.kybox.service.impl;

import fr.kybox.endpoint.Endpoint;
import fr.kybox.service.ClientService;
import org.glassfish.tyrus.client.ClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.Scanner;
import java.util.concurrent.Future;

import static java.lang.String.format;

@Service
public class ClientServiceImpl implements ClientService {

    private final PrintStream printStream;
    private final Scanner scanner;

    @Autowired
    public ClientServiceImpl(PrintStream printStream, Scanner scanner) {
        this.printStream = printStream;
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
                this.printStream.println();
        }
    }

    @Override
    public String askUsername() {
        this.printStream.println(":: Entrer your name :");
        return this.scanner.nextLine();
    }

    @Override
    public Session connect(String username) {
        this.printStream.println(format(":: Hi %s, please wait.", username));
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
            this.printStream.println("\n");
            this.printStream.println(format("Error : %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public void startChatting(Session session, String username) {
        this.printStream.println("\r:: Connected, you can chat now.");
        this.printStream.println(":: Type 'quit' to disconnect.");
        String prompt = format("%s > ", username);
        String msg;
        do {
            this.printStream.print(prompt);
            msg = this.scanner.nextLine();
            try {
                session.getAsyncRemote().sendText(msg);
            } catch (Exception e) {
                e.printStackTrace();
                this.printStream.println(format("Error : %s", e.getMessage()));
                e.printStackTrace();
                msg = "quit";
            }
        } while (!msg.equalsIgnoreCase("quit"));
        try {
            session.close();
            this.printStream.println("Session closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loading(Future<Session> session) {
        boolean loading = true;
        while (loading) {
            try {
                printStream.print("\r[.  ] Server connection attempt.");
                Thread.sleep(300);
                printStream.print("\r[ . ] Server connection attempt.");
                Thread.sleep(300);
                printStream.print("\r[  .] Server connection attempt.");
                Thread.sleep(300);
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
