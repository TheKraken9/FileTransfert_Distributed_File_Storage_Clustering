package client;

import javax.sound.midi.Soundbank;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    Socket socket;
    DataOutputStream outputStream;
    DataInputStream inputStream;

    public void setInputStream(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setOutputStream(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public Client(String host, int port) throws Exception {
        setSocket(new Socket(host, port));
        setInputStream(new DataInputStream(getSocket().getInputStream()));
        setOutputStream(new DataOutputStream(getSocket().getOutputStream()));
    }

    public Client(Socket socket) throws Exception {
        setSocket(socket);
        setInputStream(new DataInputStream(getSocket().getInputStream()));
        setOutputStream(new DataOutputStream(getSocket().getOutputStream()));
    }

    public void sendFileName(File file) throws Exception {
        int length = file.getName().getBytes().length;
        getOutputStream().writeInt(length);
        getOutputStream().write(file.getName().getBytes());
    }

    public void sendContentFile(File file) throws Exception {
        int lenght = 0;
        int offset = 0;
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[4 * 1024];
        while ((lenght = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes, offset, lenght);
            outputStream.flush();
        }
        fileInputStream.close();
    }
    public void sendFile(File file) throws Exception {
        sendFileName(file);
        sendContentFile(file);
    }

    public static void main(String[] args) {
        try{
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String path = "/home/andrianambinina/IdeaProjects/FileTransfert/sources/";
                System.out.println("What do you want to do ?");
                System.out.println("1. Insert a new file");
                System.out.println("2. Download a new File");
                System.out.println("Exit the program");
                System.out.println("Enter your option : ");

                int choice = Integer.parseInt(scanner.nextLine());

                if(choice < 2) {
                    System.out.println("Enter the file name : (Your file must be located in this path /home/andrianambinina/IdeaProjects/FileTransfert/sources)");
                    String fileName = scanner.nextLine();
                    File file = new File(path + fileName);
                    if(file.exists()) {
                        Client client = new Client("127.0.0.1", 4000);
                        client.outputStream.writeInt(choice);
                        client.sendFile(new File(path+fileName));
                        client.getSocket().close();
                        System.out.println("File sent Successfully");
                    } else {
                        System.out.println("File does not exist");
                    }
                } else if (choice < 3) {
                    System.out.println("Enter the file name : (Your file will be located in this path /home/andrianambinina/IdeaProjects/FileTransfert/downloads)");
                    String fileName = scanner.nextLine();
                    Client client = new Client("127.0.0.1", 4000);
                    client.outputStream.writeInt(choice);
                    client.sendFileName(new File(fileName));
                } else if (choice < 4) {
                    System.out.println("Program exited");
                    return;
                }
            }
        } catch (Exception error) {
            System.out.println(error.getMessage());
            error.printStackTrace();
        }
    }
}
