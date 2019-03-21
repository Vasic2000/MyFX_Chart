package csr.Server;

import csr.NoClientException;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private MainNetwork server;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public ClientHandler(MainNetwork server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = dis.readUTF();
                            if(str.equalsIgnoreCase("/end")) {
                                dis.close();
                                dos.close();
                                socket.close();
                                break;
                            }
                            server.broadcastMsg(str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) throws NoClientException {
        try {
            dos.writeUTF(msg);
        } catch (IOException e) {
            throw new NoClientException("Больше нет такого адреса");
        }
    }
}