package csr.$Console_Chart;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
    final String IP_ADRESS = "localhost";
    final int PORT = 8189;
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;


    public Controller() throws IOException {
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        this.socket = new Socket(IP_ADRESS, PORT);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String str = dis.readUTF();
                        if((!str.isEmpty()) && (!str.equals("/end")))
                            System.out.println(df.format(new Date()) + " : " + str);
                        if(str.equals("/end"))
                            System.exit(0);
                    }
                } catch (IOException e) {
                    try {
                        dis.close();
                        dos.flush();
                        dos.close();
                        socket.close();
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(System.in)));
                try {
                    while (true) {
                        String str = br.readLine();
                        dos.writeUTF(str);
                    }
                } catch (IOException e) {
                    try {
                        dis.close();
                        dos.flush();
                        dos.close();
                        socket.close();
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
