package chatter.server.handler;

import chatter.common.Address;
import chatter.common.ChatMessage;
import chatter.common.ChatMessagePB;
import chatter.common.Lg;
import io.netty.channel.*;

/**
 * Created by c0s on 16-4-20.
 */

@ChannelHandler.Sharable
public class ServerChildHandler extends SimpleChannelInboundHandler<ChatMessagePB.ChatMessageProto> {
    /**
     * look up router map, find the channel connected with receiver
     * send the message through that channel
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessagePB.ChatMessageProto msg) throws Exception {
        if (msg.getReceviver().equals(Address.serverName)) {
            //register message. ignore
            return;
        }

        Channel channel = RouterMap.findDestinationChannel(msg.getReceviver());
        if (channel == null) {
            Lg.log("message:" + msg + " receiver connection not found.");
            ChatMessage m = new ChatMessage(0, "server", msg.getSender(), "dest: '" + msg.getReceviver() + "' is not online. Msg dropped.");
            ctx.writeAndFlush(m.getChatMessageProto());
            return;
        }
        channel.writeAndFlush(msg);
    }
}
