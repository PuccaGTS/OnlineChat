package Client;

import Logger.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String host;
    private int port;
    private String name;
    Logger logger = Logger.getInstance();
    private String settings = "settings.txt";
    private final String EXITCHAT = "/exit";

    public Client() {
        try (BufferedReader br = new BufferedReader(new FileReader(settings))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.startsWith("port:")) {
                    port = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                }
                if (line.startsWith("host: ")) {
                    host = line.split(" ")[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startChat() {
        try (Socket clientSocket = new Socket(host, port);
             PrintWriter outMess = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader inMess = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                    if (scanner.hasNext()){
                        String mess = scanner.nextLine(); //берем сообщение клиента с консоли
                        outMess.println(mess); // отправляем серверу
                        String messFormServer = inMess.readLine(); //принимаем от сервера сообщение
                        System.out.println(messFormServer); // печатаем сообщение в консоль
                    } else {
                        String messFormServer = inMess.readLine(); //принимаем от сервера сообщение
                        System.out.println(messFormServer); // печатаем сообщение в консоль
                    }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}