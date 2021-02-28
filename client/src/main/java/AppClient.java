import javax.swing.*;
import java.util.concurrent.Callable;

public class AppClient {
    public static void main (String[] args) {


            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ChatGUI();
                }
            });


    }

}
