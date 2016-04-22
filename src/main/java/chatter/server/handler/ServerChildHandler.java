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
    private static Lg logger = new Lg(ServerChildHandler.class);
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
            logger.log("message:" + msg + " receiver connection not found.");
            ChatMessage m = new ChatMessage(0, "server", msg.getSender(), "dest: '" + msg.getReceviver() + "' is not online. Msg dropped.");
            ctx.writeAndFlush(m.getChatMessageProto());
            return;
        }
        channel.writeAndFlush(msg);
    }

    public static void main(String[] args) {
        ChatMessage m = new ChatMessage(0, "server", "client" , "is not online. Msg dropped.");
        ChatMessagePB.ChatMessageProto proto = m.getChatMessageProto();
        System.out.println(proto + " lalalal");
    }
}
