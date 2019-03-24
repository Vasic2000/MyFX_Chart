package csr.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler {
    DateFormat db = new SimpleDateFormat("HH:MM:ss");

    private MainNetwork server;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public String getNick() {
        return nick;
    }

    private String nick;

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

                            if (str.startsWith("/auth")) {
                                String[] tokens = str.split(" ");
                                String newNick = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]);
                                if ((newNick != null) && !server.isExist(newNick)) {
                                    sendMsg("/authok");
                                    nick = newNick;
                                    server.subscribe(ClientHandler.this);
                                    break;
                                } else {
                                    if(server.isExist(newNick))
                                        sendMsg("Логин/пароль уже занят");
                                    else
                                        sendMsg("Неверный логин/пароль");
                                }
                            }
                        }

                        while (true) {

                            String str = dis.readUTF();
                            if (str.equals("/end")) {
                                dos.writeUTF("/serverclosed");
                                break;
                            }

                            if (str.startsWith("/w")) {
                                String [] tokens = str.split(" ");
                                String message = str.substring(tokens[1].length() + 4);
                                server.sendDirectMsg(tokens[1], nick, db.format(new Date()) + " <" + nick +  "> : " + message);
                            } else
                                server.broadcastMsg(db.format(new Date()) + " <" + nick +  "> : " + str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            dis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            dos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        server.unsubscribe(ClientHandler.this);
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            dos.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}