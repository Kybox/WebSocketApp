package fr.kybox.service.impl;

import fr.kybox.endpoint.Endpoint;
import fr.kybox.service.ServerService;
import org.glassfish.tyrus.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.DeploymentException;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class ServerServiceImpl implements ServerService {

    private final PrintStream printStream;

    @Autowired
    public ServerServiceImpl(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void startTextServer() {
        final Server server = new Server("localhost", 8080, "/", null, Endpoint.class);
        try {
            server.start();
            this.printStream.println("Press any key to stop the server..");
            new Scanner(System.in).nextLine();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
