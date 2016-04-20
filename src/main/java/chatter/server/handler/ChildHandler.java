package chatter.server.handler;

import chatter.common.ChatMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by c0s on 16-4-20.
 */

@ChannelHandler.Sharable
public class ChildHandler extends SimpleChannelInboundHandler<ChatMessage> {

    /**
     * connection map:socket address to channel
     */
    private ConcurrentHashMap<InetSocketAddress, Channel> connectionMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatMessage msg) throws Exception {

    }
}
