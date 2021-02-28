import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientNetwork {

    private static Socket socket;
    private  static DataInputStream in;
    private  static DataOutputStream out;
    private static Callback<String> callOnMsgRecieved;
    private static Callback<String> getCallOnChangeClientList;



    public static void connect (){
        try {

            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
             new Thread(() -> {
                boolean goOn = true;
                try {
                    while (goOn){
                        String msg = in.readUTF();
                        if (msg.equalsIgnoreCase("/end")){
                            goOn = false;
                        }
                        else if (msg.startsWith("/clients ")){

                        }
                        else  {
                              callOnMsgRecieved.callback(msg);

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    closeConnect();
                }

            }).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage (String msg){

        try {
            out.writeUTF(msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void closeConnect (){

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

    public void setCallOnMsgRecieved (Callback<String> callOnMsgRecieved) {
        this.callOnMsgRecieved = callOnMsgRecieved;
    }

    public void setGetCallOnChangeClientList (Callback<String> getCallOnChangeClientList) {
        this.getCallOnChangeClientList = getCallOnChangeClientList;
    }
}
