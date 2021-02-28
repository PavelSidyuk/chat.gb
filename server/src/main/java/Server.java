import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private List<ClientHandler> clients;
    private AtomicInteger number =new AtomicInteger(1);


    public Server(){
        this.clients = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(8189)){
            System.out.println("Сервер готов");

            while (true){
                Socket socket = serverSocket.accept();
                new ClientHandler(number.getAndIncrement(), this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void broadcastMessage ( String message ) {
        for (ClientHandler client: clients){
            client.sendMessage(message);

        }
    }
    public void broadcastClientList(){

        StringBuilder sb = new StringBuilder(15* clients.size());

        sb.append("/clients");
        for (ClientHandler o: clients){
            sb.append(o.getNickName()).append(" ");
        }
        String out = sb.toString();
        broadcastMessage(out);
    }

    public void subscribe (ClientHandler client){
        clients.add(client);
        broadcastClientList();

    }
    public void unSubscribe (ClientHandler client){
        clients.remove(client);
        broadcastClientList();

    }
    public void privateMsg(ClientHandler sender, String receiverNick, String msg) {
        if (sender.getNickName().equals(receiverNick)) {
            sender.sendMessage("Note for myself: " + msg);
            return;
        }
        for (ClientHandler receiver : clients) {
            if (receiver.getNickName().equals(receiverNick)) {
                receiver.sendMessage("from " + sender.getNickName() + ": " + msg);
                sender.sendMessage("for " + receiverNick + ": " + msg);
                return;
            }
        }
        sender.sendMessage("Client " + receiverNick + " is not found");
    }
}
