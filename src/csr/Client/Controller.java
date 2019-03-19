package csr.Client;

import csr.NoClientException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    DateFormat df = new SimpleDateFormat("hh:mm:ss");

    @FXML
    TextArea textArea_ID;

    @FXML
    TextField text_ID;

    @FXML
    Button btn1;

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = dis.readUTF();
                            textArea_ID.appendText(df.format(new Date()) + " : " + str + "\n");
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
                        //e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException io) {
            io.printStackTrace();
        }
    }


    public void sendMsg() {
        try {
            dos.writeUTF(text_ID.getText());
            text_ID.clear();
            text_ID.requestFocus();
        } catch (IOException e) {
            throw new NoClientException();
        }
    }
}
