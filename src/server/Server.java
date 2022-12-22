package server;

import client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    String[] listOfServers = {"Zeus","Hera","Hades"};

    Socket[] servers;

    DataInputStream dataInputStream;

    DataOutputStream dataOutputStream;

    Client client;

    ServerSocket serverSocket;

    public void setDataInputStream(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public void setDataOutputStream(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setServers(Socket[] servers) {
        this.servers = servers;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket[] getServers() {
        return servers;
    }

    public Client getClient() {
        return client;
    }

    public Server(int port) throws Exception {
        System.out.println("Waiting a client ...");
        setServerSocket(new ServerSocket(port));
        setClient(new Client(getServerSocket().accept()));
        System.out.println("Client connected");
    }

    public String getFileName() throws Exception {
        int offset = 0;
        int lenght = getClient().getInputStream().readInt();
        byte[] bytes = new byte[lenght];
        getClient().getInputStream().readFully(bytes, offset, lenght);
        return new String(bytes);
    }

    public void send() throws Exception {
        int part = 3;
        int offset = 0;
        int choice = getClient().getInputStream().readInt();
        if(choice < 2) {
            String file = getFileName();
            byte[] bytes = new byte[4 * 1024];
            int lenght = getClient().getInputStream().read(bytes);
            int portion = lenght / part;
            DataOutputStream out = null;
            Socket socket = null;
            for (int i = 1; i <= part; i++, offset += portion) {
                socket = new Socket("127.0.0.1", 5000 + i);
                out = new DataOutputStream(socket.getOutputStream());
                out.writeInt(choice);
                out.writeInt(file.getBytes().length);
                out.write(file.getBytes());
                if (i == part) {
                    portion += lenght - offset - portion;
                }
                out.write(bytes, offset, portion);
                out.close();
                socket.close();
            }
        } else if (choice < 3) {
            System.out.println("tafiditra choice 2");
            DataOutputStream out = null;
            String file = getFileName();
            for (int i = 1; i <= part; i++) {
                Socket socket = new Socket("127.0.0.1", 5000 + i);
                out = new DataOutputStream(socket.getOutputStream());
                out.writeInt(choice);
                out.writeInt(file.getBytes().length);
                out.write(file.getBytes());
                out.close();
                socket.close();
            }
            System.out.println("choice 2");
        }
    }

    public static void send(ServerSocket serverSocket, String folder) throws Exception {
        Socket client = serverSocket.accept();
        DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
        int choice = dataInputStream.readInt();
        System.out.println(choice + " choice");
        if (choice == 1) {
            setFile(dataInputStream, folder);
        } else if (choice == 2) {
            getFile(dataInputStream, folder);
            System.out.println(choice + " choice");
            client.close();
        } else if (choice == 3) {
            System.out.println("Program exited");
        }
    }

    public static String getFileName(InputStream inputStream) throws Exception {
        int offset = 0;
        int lenght = ((DataInputStream) inputStream).readInt();
        byte[] bytes = new byte[lenght];
        ((DataInputStream) inputStream).readFully(bytes, offset, lenght);
        return new String(bytes);
    }

    public static void setFile(InputStream inputStream, String folder) throws Exception {
        try {
            int offset = 0;
            String fileName = getFileName(inputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(folder + fileName));
            byte[] bytes = new byte[4 * 1024];
            int lenght;
            while ((lenght = inputStream.read(bytes)) > -1) {
                fileOutputStream.write(bytes, offset, lenght);
            }
        }catch (Exception error) {
            error.printStackTrace();
        }
    }
    public static void getFile(InputStream inputStream, String folder) throws Exception {
        try {
            String pathDownload = "/home/andrianambinina/IdeaProjects/FileTransfert/downloads/";
            int offset = 0;
            int lenght = 0;
            String fileName = getFileName(inputStream);
        System.out.println(fileName + " fileName");
            File file = new File(folder + fileName);
            if(file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                FileOutputStream fileOutputStream = new FileOutputStream(new File(pathDownload + fileName), true);
                byte[] bytes = new byte[4 * 1024];
                while ((lenght = fileInputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, offset, lenght);
                    fileOutputStream.flush();
                }
                fileInputStream.close();
            } else {
                System.out.println("File does not exist");
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
    public static void main(String[] args) {
        while (true) {
            try {
                Server server = new Server(4000);
                server.send();
                server.getServerSocket().close();
            } catch (Exception error) {
                System.out.println(error.getMessage());
                error.printStackTrace();
            }
        }
    }
}
