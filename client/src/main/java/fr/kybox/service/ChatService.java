package fr.kybox.service;

import fr.kybox.endpoint.Endpoint;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.lang.String.format;

public class ChatService {

    @Getter
    @Setter
    private static String userName;

    private Scanner reader;
    private PrintStream writer;
    private Future<Session> session;

    public ChatService() {
        this.start();
    }

    public void start() {

        this.writer = new PrintStream(System.out);
        this.reader = new Scanner(System.in);
        this.clearScreen();
        writer.println(":: Chat initialisation");

        setUserName(this.askUsername());

        if (this.connected()) {
            writer.println("\r:: Connected, you can chat now.");
            writer.println(":: Type 'quit' to disconnect.");
            this.startChatting();
        } else {
            writer.println("Connection error !");
        }
    }

    private String askUsername() {
        writer.println(":: Entrer your name :");
        return this.reader.nextLine();
    }

    @SneakyThrows
    private boolean connected() {
        writer.println(format(":: Hi %s, please wait.", userName));
        ClientManager clientManager = ClientManager.createClient();
        try {
            this.session = clientManager
                    .asyncConnectToServer(Endpoint.class, new URI("ws://localhost:8080/chat/" + userName));
        } catch (DeploymentException | URISyntaxException e) {
            writer.println(format("Error : %s", e.getMessage()));
            return false;
        }

        this.serverConnectionAttempt();
        return !this.session.isCancelled();
    }

    private void startChatting() {
        String msg;
        do {
            writer.print(userName + " > ");
            msg = this.reader.nextLine();
            try {
                session.get().getAsyncRemote().sendText(msg);
            } catch (InterruptedException | ExecutionException e) {
                writer.println(format("Error : %s", e.getMessage()));
            }
        } while (!msg.equalsIgnoreCase("quit"));
    }

    private void clearScreen() {
        try {
            if (System.getProperty("os.name").startsWith("Windows"))
                Runtime.getRuntime().exec("cls");
            else
                Runtime.getRuntime().exec("clear");
        } catch (Exception e) {
            for (int i = 0; i < 100; i++)
                writer.println("");
        }
    }

    private void serverConnectionAttempt() throws InterruptedException {
        boolean loading = true;
        while (loading) {
            writer.print("\r[.  ] Server connection attempt.");
            Thread.sleep(300);
            writer.print("\r[ . ] Server connection attempt.");
            Thread.sleep(300);
            writer.print("\r[  .] Server connection attempt.");
            Thread.sleep(300);
            if (this.session.isCancelled() || this.session.isDone())
                loading = false;
        }
    }
}
