import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private String nickName;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;
    private Integer number;

    public ClientHandler (Integer number,Server server ,Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.nickName = "Клиент" + number;
            new Thread(() -> {
                server.subscribe(this);
                boolean continueChat = true;
                while (continueChat) {
                String msg = null;
                try {

                    try {
                        while (continueChat) {
                            msg = in.readUTF();

                            if (msg.startsWith("/")) {
                                if (msg.equalsIgnoreCase("/end")) {
                                    continueChat = false;
                                } else if (msg.startsWith("/w")) {
                                    String[] tokens = msg.split("\\s", 3);
                                    System.out.println(tokens);
                                    if (tokens.length == 3) {
                                        System.out.println(tokens);
                                        server.privateMsg(this, tokens[1], tokens[2]);
                                    }
                                }
                            } else {
                                server.broadcastMessage(nickName + " " + msg);
                            }
                        }

                    } finally {

                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
                    finally{
                        disconnect();
                    }

                }

            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void sendMessage (String message){

       try {
           out.writeUTF(message);
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    public String getNickName () {
        return nickName;
    }

    public void disconnect(){
        server.unSubscribe(this);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
