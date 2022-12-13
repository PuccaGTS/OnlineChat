package Client;

import Logger.Logger;
import Server.Server;

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

    private Server server;
    private Socket clientSocket = null;
    private BufferedReader inMess;
    private PrintWriter outMess;
    private Scanner scannerConsole;

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
            clientSocket = new Socket(host, port);
            outMess = new PrintWriter(clientSocket.getOutputStream(), true);
            inMess = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            scannerConsole = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startChat() {

        //поток принимающий сообщения от сервера и печатающий в консоль
        new Thread(() -> {
            try {
                while (true) {
                    if (inMess.ready()){
                        String messFormServer = inMess.readLine();
                        System.out.println(messFormServer);
                    }
                }
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }).start();

        //поток отправляет сообщения на сервер
        new Thread(() -> {
            while (true) {
                if (scannerConsole.hasNext()) {
                    String mess = scannerConsole.nextLine(); //берем сообщение клиента с консоли
                    outMess.println(mess); // отправляем серверу
                }
            }
        }).start();
    }
}