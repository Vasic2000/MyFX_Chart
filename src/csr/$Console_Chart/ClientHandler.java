package csr.$Console_Chart;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler {
    DateFormat df = new SimpleDateFormat("hh:mm:ss");

    private ServerSocket server;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public ClientHandler(ServerSocket server, Socket socket) {
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


        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(System.in)));
                try {
                    while (true) {
                        String str = br.readLine();
                        out.writeUTF(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
