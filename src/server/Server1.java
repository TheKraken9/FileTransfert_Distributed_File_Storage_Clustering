package server;
import java.net.ServerSocket;

public class Server1 {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8051);
            Server.sendFile(server, "/home/andrianambinina/IdeaProjects/Transfert/src/other1/");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}