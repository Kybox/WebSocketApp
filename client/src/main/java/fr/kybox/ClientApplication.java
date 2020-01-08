package fr.kybox;

import fr.kybox.client.Client;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class ClientApplication {

    private final Client client;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s");
    }

    public ClientApplication(Client client) {
        this.client = client;
    }

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientApplication.class);
        ClientApplication clientApplication = applicationContext.getBean(ClientApplication.class);
        clientApplication.startApplication();
    }

    private void startApplication() {
        this.client.start();
    }
}
