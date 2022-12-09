package client;
import server.Server;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    Socket socket;
    DataOutputStream output;
    DataInputStream input;

    public DataOutputStream getOutput() {
        return output;
    }

    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    public DataInputStream getInput() {
        return input;
    }

    public void setInput(DataInputStream input) {
        this.input = input;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Client(String host, int port) throws Exception {
        setSocket(new Socket(host, port));
        setInput(new DataInputStream(getSocket().getInputStream()));
        setOutput(new DataOutputStream(getSocket().getOutputStream()));
    }

    public Client(Socket socket) throws Exception {
        setSocket(socket);
        setInput(new DataInputStream(getSocket().getInputStream()));
        setOutput(new DataOutputStream(getSocket().getOutputStream()));
    }

    public void sendFile(File file) throws Exception {
        sendNameFile(file);
        sendContent(file);
    }

    public void sendContent(File file) throws Exception {
        int bytes = 0;
        // Open the File where he located in your pc
        FileInputStream fileInputStream = new FileInputStream(file);
        // Here we break file into chunks
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            // Send the file to Server Socket
            output.write(buffer, 0, bytes);
            output.flush();
        }
        // close the file here
        fileInputStream.close();
    }

    public void sendNameFile(File file) throws Exception {
        int lengthName = file.getName().getBytes().length;
        getOutput().writeInt(lengthName);
        getOutput().write(file.getName().getBytes());
    }

    public static void main(String[] args) {
        try {
            while (true) {
                Scanner sc = new Scanner(System.in);
                String path = "/home/andrianambinina/IdeaProjects/Transfert/src/";
                System.out.println("What do you want to do ?");
                System.out.println("1. Insert a file");
                System.out.println("2. Look up a file");
                System.out.println("3. exit program");
                System.out.println("Enter your option");

                int option = Integer.parseInt(sc.nextLine());

                if(option <2) {
                    System.out.println("Enter the file name : ");
                    String file_name = sc.nextLine();
                    File file = new File(path + file_name);
                    if(file.exists()) {
                        Client client = new Client("localhost", 8008);
                        client.sendFile(new File(""+path+file_name));
                        client.getSocket().close();
                        System.out.println("File Insertion Successful");
                    }
                } else if (option < 3) {
                    System.out.println("Enter the file name : ");
                    String file_name = sc.nextLine();
                    int dot_end = file_name.lastIndexOf('.');
                    String base_file_name = file_name.substring(0, dot_end);
                    Server server = new Server(8008);
                    System.out.println("Forwarding the request...");
                    server.receiveContent(new File(""+path+file_name));
                    server.getServer().close();
                    System.out.println("Download successful :)");
                    path = "/home/andrianambinina/IdeaProjects/Transfert/Downloads/";

                } else if(option < 4) {
                    System.out.println("Program finished");
                    return;
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void makeConnectionToServer(String server_name) throws IOException {
        try {
            InetAddress iAddress = InetAddress.getByName(server_name + "");
            String server_IP = iAddress.getHostAddress();

        } catch (UnknownHostException error) {
            error.printStackTrace();
        }

    }

}