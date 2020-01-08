package fr.kybox.server;

import fr.kybox.service.ServerService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log
@Component
public class Server {

    private final ServerService serverService;

    @Autowired
    public Server(ServerService serverService) {
        this.serverService = serverService;
    }

    public void start() {
        log.info("Server startup...");
        this.serverService.startTextServer();
    }
}
