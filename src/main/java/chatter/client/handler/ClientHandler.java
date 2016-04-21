package chatter.client.handler;

import chatter.common.ChatMessage;
import chatter.common.ChatMessagePB;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by c0s on 16-4-20.
 */
public class ClientHandler extends SimpleChannelInboundHandler<ChatMessagePB.ChatMessageProto> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessagePB.ChatMessageProto msg) throws Exception {
        System.out.println("msg recv: \n" + msg);
    }
}
