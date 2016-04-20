package chatter.common;

import io.netty.channel.Channel;

import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by c0s on 16-4-20.
 */
public class IdUtils {
    public static String uniqueId(Channel channel, String sender) {
        return uniqueId(channel.remoteAddress(), sender);
    }

    public static String uniqueId(SocketAddress socketAddress, String sender) {
        return sender + "@" + socketAddress.toString();
    }
}
