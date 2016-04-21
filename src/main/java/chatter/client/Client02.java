package chatter.client;

/**
 * Created by c0s on 16-4-20.
 */
public class Client02 {
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("client02", "client01");
        client.start();
    }
}
