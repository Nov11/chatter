package chatter.server.handler;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by c0s on 16-4-20.
 */
public class RouterMap {
    /**
     * connection map: sender to its original channel
     * share with all the ChildHandlers
     *
     * channel to key(a user identifier) is needed when channel is closed by client and no message is sent.
     * I need to remove the connection related to the channel.
     * But here comes the problem:
     * Atomic update on the two map is needed so that a relation exists and disappear consistently.
     * So, set / remove must be synchronized. ConcurrentHashMap is somehow wasted. : (
     * How can I make this better?
     */
    public static ConcurrentHashMap<String, Channel> connectionMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<Channel, String> channelKeyMap = new ConcurrentHashMap<>();

    /**
     * synchronized on container. no need to add 'syn' to function defination.
     */
    public static Channel findDestinationChannel(String key) {
        return connectionMap.get(key);
    }

    public static String findRelatedUser(Channel channel){
        return channelKeyMap.get(channel);}

    public static synchronized void setSourceChannel(String key, Channel channel) {
        connectionMap.put(key, channel);
        channelKeyMap.put(channel, key);
    }

    public static synchronized void removeSourceChannel(String key) {
        Channel c = connectionMap.get(key);
        connectionMap.remove(key);
        channelKeyMap.remove(c);
    }

    public static synchronized void removeSourceChannel(Channel channel) {
        String key = channelKeyMap.get(channel);
        connectionMap.remove(key);
        channelKeyMap.remove(channel);
    }
}
