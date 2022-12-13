package Server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class User {
    private Socket clientSocket;
    private PrintWriter outMess;
    private Scanner inMess;

    public User(Socket clientSocket, PrintWriter outMess, Scanner inMess) {
        this.clientSocket = clientSocket;
        this.outMess = outMess;
        this.inMess = inMess;
    }

    public void sendMsg(String msg){
        outMess.println(msg);
        outMess.flush();
    }
}
