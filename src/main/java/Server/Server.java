package Server;

import Logger.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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
            try {
                Socket clientSocket = serverSocket.accept();

                Thread thread = new Thread(() -> {
                    try (PrintWriter outMess = new PrintWriter(clientSocket.getOutputStream(), true)) {
                        User user = new User(clientSocket, outMess);
                        users.put(clientSocket.getPort(), user);
                        waitMessAndSend(clientSocket, user);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sendMessToAll(String mess) {
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            entry.getValue().sendMsg(mess);
        }
    }

    public void waitMessAndSend(Socket clientSocket, User user) {
        try (Scanner inMess = new Scanner(clientSocket.getInputStream())) {
            while (true) {
                if (inMess.hasNext()) {
                    String mess = inMess.nextLine();
                    if (mess.startsWith("/name:")){
                        user.setName(mess.split(":")[1]);
                        logger.write("Установлено имя " + user.getName() + " для клиента " + clientSocket.getPort());
                        user.sendMsg("Привет, " + user.getName() + " продолжай общаться в чатике)))");
                    } else if (mess.equalsIgnoreCase(EXITCHAT)) {
                        logger.write("Клиент покинул чат - имя: " + user.getName() + ", порт: " + clientSocket.getPort());
                        user.sendMsg("Пока-пока, возвращайся скорее!!!");
                        users.remove(clientSocket.getPort());
                        break;
                    } else {
                        if (user.getName()==null){
                            logger.write("Сообщение: " + mess + " - получено на сервер от " + clientSocket.getPort());
                            sendMessToAll(clientSocket.getPort() + ": " + mess);
                            logger.write("Сообщение: " + mess + " - отправлено всем пользователям");
                        } else {
                            logger.write("Сообщение: " + mess + " - получено на сервер от " + clientSocket.getPort() + ", имя: " + user.getName());
                            sendMessToAll(user.getName() + ": " + mess);
                            logger.write("Сообщение: " + mess + " - отправлено всем пользователям");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registration(Socket clientSocket, User user) {
        try (Scanner inMess = new Scanner(clientSocket.getInputStream())) {
            user.sendMsg("Напиши свой никнейм");
            String name = inMess.nextLine();
            user.setName(name);
            user.sendMsg("Привет, " + user.getName() + ", приятного общения!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

