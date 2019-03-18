package csr.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainNetwork {
    public static void main(String[] args) {
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(8189);
            System.out.println("Сервер запущен!");
while(true) {
    socket = server.accept();
}

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
