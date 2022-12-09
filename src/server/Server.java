package server;// A Java program for a Server
import client.Client;

import java.net.*;
import java.io.*;

public class Server {

    Socket[] servers;
    DataInputStream input;
    DataOutputStream output;
    Client client;
    ServerSocket server;

    public DataInputStream getInput() {
        return input;
    }

    public void setInput(DataInputStream input) {
        this.input = input;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public Server(int port) throws Exception {
        System.out.println("Wait Client...");
        setServer(new ServerSocket(port));
        setClient(new Client(getServer().accept()));
        System.out.println("Connected");
    }

    public String getFileName() throws Exception {
        int lengthName = getClient().getInput().readInt();
        byte[] b = new byte[lengthName];
        getClient().getInput().readFully(b, 0, lengthName);
        return new String(b);
    }

    public void sendFile() throws Exception {
        String file = getFileName();
        byte[] content = new byte[4678];
        int count = getClient().getInput().read(content);
        int divide = count / 3;
        int off = 0;
        DataOutputStream out = null;
        Socket socket = null;
        for (int increment = 1; increment <= 3; increment++, off += divide) {
            socket = new Socket("localhost", 8050 + increment);
            out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(file.getBytes().length);
            out.write(file.getBytes());
            if (increment == 3) divide += count - off - divide;
            out.write(content, off, divide);
            out.close();
            socket.close();
        }
    }

    public void receiveContent(File target) throws Exception{
        FileOutputStream out = new FileOutputStream(new File(target.toURI()));
        byte[] content = new byte[4 * 1024];
        int count;
        while ((count = input.read(content)) > -1)
            out.write(content, 0, count);
    }

    public static void sendFile(ServerSocket server, String folder) throws Exception {
        Socket client = server.accept();
        DataInputStream input = new DataInputStream(client.getInputStream());
        setFile(input, folder);
        client.close();
        server.close();
    }

    public static void setFile(InputStream input, String folder) throws Exception {
        String name = getName(input);
        FileOutputStream out = new FileOutputStream(new File(folder + name));
        byte[] content = new byte[4675];
        int count;
        while ((count = input.read(content)) > -1)
            out.write(content, 0, count);
    }

    public static String getName(InputStream input) throws Exception {
        int lengthName = ((DataInputStream) input).readInt();
        byte[] b = new byte[lengthName];
        ((DataInputStream) input).readFully(b, 0, lengthName);
        return new String(b);
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(8008);
            server.sendFile();
            server.getServer().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}