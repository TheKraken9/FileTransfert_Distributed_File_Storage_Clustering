package server;

import java.net.ServerSocket;

public class Server2 {
    public static void main(String[] args) throws Exception {
        try {
            ServerSocket server = new ServerSocket(5002);
            Server.send(server, "/home/andrianambinina/IdeaProjects/FileTransfert/Hera/");
        } catch (Exception error) {
            System.out.println(error.getMessage());
            error.printStackTrace();
        }
    }
}
