package Server;
//BufferedReader inMess1 = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())))
import Logger.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
    private String settings = "settings.txt";
    private static int port;
    private static Logger logger = Logger.getInstance();
    private final String EXITCHAT = "/exit";
    private Map<Integer, User> users = new HashMap<>();
    private ServerSocket serverSocket;

    public Server() {
        try (BufferedReader br = new BufferedReader(new FileReader(settings))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.startsWith("port:")) {
                    port = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        logger.write("Сервер начал свою работу");

        while (true) {
            new Thread(()->{
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter outMess = new PrintWriter(clientSocket.getOutputStream(), true);
                 Scanner inMess = new Scanner(clientSocket.getInputStream())) {

                User user = new User(clientSocket, outMess, inMess);
                users.put(clientSocket.getPort(), user);

                    while (true) {
                        if (inMess.hasNext()) {
                            String mess = inMess.nextLine();
                            System.out.println("Получил " + mess + " от " + clientSocket.getPort());
                            sendMessToAll(clientSocket.getPort() + " отправил сообщение: " + mess);
                        }
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
            }).start();
        }
    }

    public synchronized void sendMessToAll(String mess) {
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            entry.getValue().sendMsg(mess);
        }
    }
}

