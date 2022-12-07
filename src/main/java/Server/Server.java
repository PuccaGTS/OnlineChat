package Server;

import Logger.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private String settings = "settings.txt";
    private int port;
    private static Logger logger = Logger.getInstance();

    public Server(){
        try (BufferedReader br = new BufferedReader(new FileReader(settings))){
            while (br.ready()){
                String line = br.readLine();
                if (line.startsWith("port:")){
                    port = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void startServer(){
        logger.write("Сервер начал свою работу");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())))) {

                    System.out.println("Новое подключение установлено \nПорт подключения: " + clientSocket.getPort());
                    System.out.println("-------------------------------------------");

                    System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
                    out.println(String.format("Напиши своё имя"));
                    System.out.println("Идет получение имени пользователя ......");
                    final String name = in.readLine();
                    System.out.println("Имя пользователя: " + name);

                    out.println(String.format("Привет %s, а ты ребенок? (да/нет)", name));
                    System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
                    System.out.println("Идет получение информации о возрасте пользователя " + name + "......");


                    boolean flag = true;
                    while (flag){
                        final String isChild = in.readLine();
                        System.out.println("Получен ответ " + isChild);
                        System.out.println("||||||||||||||||||||||||||||||||||||||||||||");

                        if (isChild.equals("да") || isChild.equals("Да") || isChild.equals("ДА")){
                            out.println(String.format("Добро пожаловать на детскую площадку, %s! Давай же скорее играть", name));
                            System.out.println("Сообщение отправлено на ответ \"Да\"");
                            System.out.println("Программа клиента завершилась");
                            System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
                            flag = false;
                        } else if (isChild.equals("нет") || isChild.equals("Нет") || isChild.equals("НЕТ")){
                            out.println(String.format("Добро пожаловать во взрослую зону, %s! Проведите время с пользой", name));
                            System.out.println("Сообщение отправлено на ответ \"Нет\"");
                            System.out.println("Программа клиента завершилась");
                            System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
                            flag = false;
                        } else {
                            out.println(String.format("Перестань шутить, %s, и ответь нормально на вопрос - Да или Нет", name));
                            System.out.println("Сообщение отправлено на ответ \"Абра-кадабра\"");
                            System.out.println("Программа клиента продолжает работу");
                            System.out.println("||||||||||||||||||||||||||||||||||||||||||||");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

