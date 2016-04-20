package chatter.server.handler;

import chatter.common.ChatMessage;
import chatter.common.Lg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

/**
 * Created by c0s on 16-4-20.
 */
public class RegClientRouteInboundHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!registerClientWithChannel(ctx.channel(), msg)) {
            ByteBuf buf = (ByteBuf)msg;
            buf.release();
            return;
        }
        super.channelRead(ctx, msg);
    }


    private boolean registerClientWithChannel(Channel channel, Object msg) {
//        SocketAddress socketAddress = channel.remoteAddress();
//        InetSocketAddress inetSocketAddress;
//        if (InetSocketAddress.class.isInstance(socketAddress)) {
//            inetSocketAddress = (InetSocketAddress) socketAddress;
//        } else {
//            System.err.println("remote sockaddress is not inetsocketaddress " + socketAddress);
//            return false;
//        }

        String sender;
        if (msg instanceof ChatMessage) {
            ChatMessage c = (ChatMessage) msg;
            sender = c.getSender();
        } else {
            Lg.log(this.getClass().getSimpleName() + " receive msg not of type ChatMessage.");
            return false;
        }
        RouterMap.setSourceChannel(sender, channel);
        return true;
    }
}
