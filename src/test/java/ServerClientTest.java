import Client.Client;
import Server.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerClientTest {
    Server server;
    Client client;

    @BeforeEach
    void server_and_client_init() throws InterruptedException {
        //Thread of Server
        Thread serverThread = new Thread(()->{
            server = new Server();
            server.startServer();
        });

        //Thread of Client
        Thread clientThread1 = new Thread(()->{
            client = new Client();
            client.startChat();
        });

        Thread clientThread2 = new Thread(()->{
            client = new Client();
            client.startChat();
        });
        Thread clientThread3 = new Thread(()->{
            client = new Client();
            client.startChat();
        });

        serverThread.start();
        Thread.sleep(1000);

        clientThread1.start();
        clientThread2.start();
        clientThread3.start();
        Thread.sleep(1000);
    }

    @Test
    void check_getAmountUsers_in_map(){
        int expected = 3;
        int result = Server.getAmountUsers();
        Assertions.assertEquals(expected, result);
    }
}
