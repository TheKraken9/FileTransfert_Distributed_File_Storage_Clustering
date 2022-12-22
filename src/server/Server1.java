package server;

import java.net.ServerSocket;

public class Server1 {
    public static void main(String[] args) throws Exception {
        try {
            ServerSocket server = new ServerSocket(5001);
            Server.send(server, "/home/andrianambinina/IdeaProjects/FileTransfert/Zeus/");
        } catch (Exception error) {
            System.out.println(error.getMessage());
            error.printStackTrace();
        }
    }
}
