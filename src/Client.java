import java.io.File;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            File file = new File("./compile.sh");
            sendFile(file, new Socket("localhost", 8090));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void sendFile(File file, Socket socket) throws Exception {
        socket.close();
    }
}
