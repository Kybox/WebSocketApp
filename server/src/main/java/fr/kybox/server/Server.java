package fr.kybox.server;

import fr.kybox.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintStream;

@Component
public class Server {

    private final ServerService serverService;
    private final PrintStream printStream;

    @Autowired
    public Server(ServerService serverService, PrintStream printStream) {
        this.serverService = serverService;
        this.printStream = printStream;
    }

    public void start() {
        this.printStream.println("Server startup...");
        this.serverService.startTextServer();
    }
}
