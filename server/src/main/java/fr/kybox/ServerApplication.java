package fr.kybox;

import fr.kybox.endpoint.Endpoint;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;
import java.io.PrintStream;
import java.util.Scanner;

public class ServerApplication {

    public final static String PATH_PARAM = "user";

    public static void main(String[] args) {
        PrintStream writer = new PrintStream(System.out);
        Server server = new Server("localhost", 8080, "/", null, Endpoint.class);
        try {
            server.start();
            writer.println("Press any key to stop the server..");
            new Scanner(System.in).nextLine();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
