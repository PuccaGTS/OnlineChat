package Client;

import Logger.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private String host;
    private int port;
    Logger logger = Logger.getInstance();
    private String settings = "settings.txt";
    private final String EXITCHAT = "/exit";

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
        AtomicBoolean flag = new AtomicBoolean(true);

        //поток принимающий сообщения от сервера и печатающий в консоль
        new Thread(() -> {
            try {
                int count = 0;
                while (true) {
                    if (count!=4){
                        String messFormServer = inMess.readLine();
                        System.out.println(messFormServer);
                        count++;
                    }
                    if (flag.get()==false){
                        inMess.close();
                        clientSocket.close();
                        break;
                    }
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
                    if (mess.equalsIgnoreCase(EXITCHAT)){
                        outMess.println(mess);
                        scannerConsole.close();
                        outMess.close();
                        flag.set(false);
                        break;
                    }
                    outMess.println(mess); // отправляем серверу
                }
            }
        }).start();
    }

    public void registration(){

    }
    //public void sendMenu (User user) {
    //        user.sendMsg("Добро пожаловать в чатик");
    //        user.sendMsg("Чтобы выйти напиши: \"/exit\"");
    //        user.sendMsg("Чтобы задать никнейм напиши: \"/name:ТвоеИмя\"");
    //        user.sendMsg("Можешь уже чатиться");
    //    }
}