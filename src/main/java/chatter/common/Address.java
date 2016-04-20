package chatter.common;

import java.net.InetSocketAddress;

/**
 * Created by c0s on 16-4-20.
 */
public interface Address {
   int serverPort = 9999;
   int clientPort = 45001; //no need to call tcpdump every time I start client.
   String serverName = "chatting server";
   InetSocketAddress serverAddress = new InetSocketAddress(serverPort);
}
