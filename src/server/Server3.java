package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class Server3 {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8053);
            Server.sendFile(server, "/home/andrianambinina/IdeaProjects/Transfert/src/other3/");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void send(File target) throws Exception {
        try {
            ServerSocket server = new ServerSocket();
            Socket serv = server.accept();
            DataInputStream input = new DataInputStream(serv.getInputStream());


        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}