package csr.$Console_Chart;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client_Handler {
    DateFormat df = new SimpleDateFormat("hh:mm:ss");

    private ServerSocket server;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public Client_Handler(ServerSocket server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.equalsIgnoreCase("/end")) {
                                out.writeUTF("/end");
                                in.close();
                                out.close();
                                socket.close();
                                break;
                            }
                            System.out.println(df.format(new Date()) + " : " + str);
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

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {}
    }
}
