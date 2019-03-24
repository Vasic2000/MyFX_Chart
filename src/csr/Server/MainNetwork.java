package csr.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

public class MainNetwork {
    private Vector<ClientHandler> clients;

    public static void main(String[] args) throws SQLException {
        new MainNetwork();
    }


    public MainNetwork() throws SQLException {
        ServerSocket server = null;
        Socket socket = null;
        clients = new Vector<>();

        try {
            AuthService.connect();
            server = new ServerSocket(8189);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент " + socket.toString() + " подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }


    public boolean isExist(String NIK) {
        for (ClientHandler o : clients) {
            if(NIK.equals(o.getNick())) return true;
        }
        return false;
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
                o.sendMsg(msg);
        }
    }

    public void sendDirectMsg(String nickTo, String nickFrom, String msg) {
        for (ClientHandler o : clients) {
            if((o.getNick().equals(nickTo)) || (o.getNick().equals(nickFrom))) o.sendMsg(msg);
        }
    }
}
