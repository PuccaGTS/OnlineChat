package Server;

import Client.Client;
import Logger.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private String settings = "settings.txt";
    private static int port;
    private static Logger logger = Logger.getInstance();
    private final String EXITCHAT = "/exit";
    private Map<Integer, User> users = new HashMap<>();

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
    }

    public void startServer() {
        logger.write("Сервер начал свою работу");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                new Thread(()->{
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter outMess = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader inMess = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())))) {

                    User user = new User(clientSocket, outMess, inMess);
                    users.put(clientSocket.getPort(), user);

                    while (true) {
                        if (inMess.ready()){
                            String mess = inMess.readLine();
                            sendMessToAll(clientSocket.getPort() + " отправил сообщение: " + mess);
                        }
                        //outMess.println("Пользователь отправил: " + mess);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessToAll(String mess) {
        for (Map.Entry<Integer, User> entry : users.entrySet()){
            entry.getValue().sendMsg(mess);
        }
    }
}

