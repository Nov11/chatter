package chatter.server.handler;

import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * Created by c0s on 16-4-20.
 */
public class RouteOutboundHandler extends ChannelOutboundHandlerAdapter {
//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        if (msg instanceof ChatMessage) {
//            ChatMessage c = (ChatMessage) msg;
//            Channel channel = RouterMap.connectionMap.get(c.getReceiver());
//            channel.write
//        }
//        super.write(ctx, msg, promise);
//    }
}
