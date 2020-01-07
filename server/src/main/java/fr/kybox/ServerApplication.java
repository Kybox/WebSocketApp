package fr.kybox;

import fr.kybox.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class ServerApplication {

    private final Server server;

    public ServerApplication(Server server) {
        this.server = server;
    }

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServerApplication.class);
        ServerApplication serverApplication = applicationContext.getBean(ServerApplication.class);
        serverApplication.startApplication();
    }

    private void startApplication() {
        this.server.start();
    }
}
