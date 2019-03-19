package csr.Server;

import csr.NoClientException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

public class MainNetwork {
    private Vector<ClientHandler> clients;

    public static void main(String[] args) {
        new MainNetwork();
    }


    public MainNetwork() {
        ServerSocket server;
        Socket socket;
        clients = new Vector<>();
        try {
            server = new ServerSocket(8189);
            System.out.println("Сервер запущен!");
            while (true) {
                socket = server.accept();
                System.out.println("Клиент " + socket.toString() + " подключился");
                clients.add(new ClientHandler(this, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMsg(String msg) {
            for (ClientHandler o : clients) {
                try {
                    o.sendMsg(msg);
                } catch (NoClientException no) {
                    for (Iterator<ClientHandler> iterator = clients.iterator(); iterator.hasNext(); )
                        if (iterator.next().equals(o))
                            iterator.remove();
                }
            }

    }
}
