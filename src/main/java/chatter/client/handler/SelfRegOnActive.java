package chatter.client.handler;

import chatter.common.Lg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by c0s on 16-4-21.
 */
public class SelfRegOnActive extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Lg.log("channel active called " + ctx.channel());
        super.channelActive(ctx);
    }
}
