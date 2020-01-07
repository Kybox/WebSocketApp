package fr.kybox.client;

import fr.kybox.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.PrintStream;

@Component
public class Client {

    private final User user;
    private final PrintStream printStream;
    private final ClientService clientService;

    @Autowired
    public Client(User user, PrintStream printStream, ClientService clientService) {
        this.user = user;
        this.printStream = printStream;
        this.clientService = clientService;
    }

    public void start() {
        this.clientService.clearScreen();
        this.printStream.println(":: Chat initialisation");
        this.user.setUsername(this.clientService.askUsername());
        Session session = this.clientService.connect(this.user.getUsername());
        if(session != null && session.isOpen())
            this.clientService.startChatting(session, this.user.getUsername());
        else
            this.printStream.println("\nConnection error !");

    }
}
