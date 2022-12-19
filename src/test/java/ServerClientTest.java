import Client.Client;
import Server.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerClientTest {
    Server server;
    Client client;
    Thread serverThread;
    Thread clientThread1;
    Thread clientThread2;
    Thread clientThread3;

    @BeforeEach
    void server_and_client_init() throws InterruptedException {
        //Thread of Server
        serverThread = new Thread(()->{
            server = new Server();
            server.startServer();
        });

        //Thread of Client
        clientThread1 = new Thread(()->{
            client = new Client();
            client.startChat();
        });

        clientThread2 = new Thread(()->{
            client = new Client();
            client.startChat();
        });

        clientThread3 = new Thread(()->{
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

    @AfterEach
    void server_and_client_end() throws InterruptedException {
        server = null;
        client = null;
        serverThread = null;
        clientThread1 = null;
        clientThread2 = null;
        clientThread3 = null;
    }

    @Test
    void check_getAmountUsers_in_map(){
        int expected = 3;
        int result = Server.getAmountUsers();
        Assertions.assertEquals(expected, result);
    }
}
