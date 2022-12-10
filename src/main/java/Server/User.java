package Server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {
    private Socket clientSocket;
    private PrintWriter outMess;
    private BufferedReader inMess;

    public User(Socket clientSocket, PrintWriter outMess, BufferedReader inMess) {
        this.clientSocket = clientSocket;
        this.outMess = outMess;
        this.inMess = inMess;
    }

    public void sendMsg(String msg){
        outMess.println(msg);
        outMess.flush();
    }
}
