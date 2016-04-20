package chatter.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by c0s on 16-4-20.
 */

//log connection peer
public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

    }
}
