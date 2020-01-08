package fr.kybox.service.impl;

import fr.kybox.endpoint.Endpoint;
import fr.kybox.service.ServerService;
import lombok.extern.java.Log;
import org.glassfish.tyrus.server.Server;
import org.springframework.stereotype.Service;

import javax.websocket.DeploymentException;
import java.util.Scanner;

@Log
@Service
public class ServerServiceImpl implements ServerService {

    @Override
    public void startTextServer() {
        final Server server = new Server("localhost", 8080, "/", null, Endpoint.class);
        try {
            server.start();
            log.info("Press any key to stop the server..");
            new Scanner(System.in).nextLine();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
