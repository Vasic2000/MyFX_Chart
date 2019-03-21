package csr.$Console_Chart;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    Vector<Client_Handler> clients;

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public Server() throws IOException {
        this.clients = new Vector<>();
        ServerSocket server = new ServerSocket(8189);
        Socket socket;
        System.out.println("Сервер запущен!");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str;
                BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(System.in)));
                try {
                    while (true) {
                        str = br.readLine();
//                        Костыль принудительного закрытия всех с сервера
                        if(str.equalsIgnoreCase("/end")) {
                            while (str.equalsIgnoreCase("/end")) {
                                System.out.println("Такое сообщение НЕДОПУСТИМО!");
                                str = br.readLine();
                            }
                        }
//                        Конец костыля
                        broadcastMsg(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            socket = server.accept();
            System.out.println("Клиент " + socket.toString() + " подключился");
            clients.add(new Client_Handler(server, socket));
        }
    }

    public void broadcastMsg(String msg) {
        for (Client_Handler o : clients) {
            o.sendMsg(msg);
        }
    }
}