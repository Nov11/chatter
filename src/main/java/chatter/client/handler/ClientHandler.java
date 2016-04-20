package chatter.client.handler;

import chatter.common.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by c0s on 16-4-20.
 */
public class ClientHandler extends SimpleChannelInboundHandler<ChatMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {
        System.out.println("msg recv: \n" + msg);
    }
}
