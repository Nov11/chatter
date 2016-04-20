package chatter.server.handler;

import chatter.common.Address;
import chatter.common.ChatMessage;
import chatter.common.Lg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by c0s on 16-4-20.
 */

@ChannelHandler.Sharable
public class ServerChildHandler extends SimpleChannelInboundHandler<ChatMessage> {
    /**
     * look up router map, find the channel connected with receiver
     * send the message through that channel
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        if (msg.getReceiver().equals(Address.serverName)) {
            //register message. ignore
            return;
        }

        Channel channel = RouterMap.findDestinationChannel(msg.getReceiver());
        if (channel == null) {
            Lg.log("message:" + msg + " receiver connection not found.");
            ChatMessage m = new ChatMessage(0, "server", msg.getSender(), "dest: '" + msg.getReceiver() + "' is not online. Msg dropped.");
            ctx.writeAndFlush(m);
            return;
        }
        channel.writeAndFlush(msg);
    }
}
