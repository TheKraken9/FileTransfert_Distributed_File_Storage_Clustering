package server;
import java.net.ServerSocket;

public class Server2 {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8052);
            Server.sendFile(server, "/home/andrianambinina/IdeaProjects/Transfert/src/other2/");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}