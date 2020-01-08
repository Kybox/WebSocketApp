package fr.kybox.client;

import fr.kybox.service.ClientService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

@Log
@Component
public class Client {

    private final ClientService clientService;

    @Autowired
    public Client(ClientService clientService) {
        this.clientService = clientService;
    }

    public void start() {
        this.clientService.clearScreen();
        log.info(":: Chat initialisation\n");
        String username = this.clientService.askUsername();
        Session session = this.clientService.connect(username);
        if (session != null && session.isOpen())
            this.clientService.startChatting(session, username);
        else
            log.info("\nConnection error !");
    }
}
