package chatter.server.handler;

import chatter.common.Lg;
import chatter.common.handler.HeartBeatHandler;
import chatter.common.handler.MsgDecoder;
import chatter.common.handler.MsgEncoder;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by c0s on 16-4-20.
 */
public class ServerChildHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                Lg.log("channel related to '" + RouterMap.findRelatedUser(channel) + "' is closed by client: " + channel.remoteAddress() + ".");
                RouterMap.removeSourceChannel(future.channel());
                Lg.log("channel removed.");
            }
        });

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(8, 4, 10));
        pipeline.addLast(new HeartBeatHandler());
        pipeline.addLast(new MsgDecoder());
        pipeline.addLast(new MsgEncoder());
        pipeline.addLast(new RegClientRouteInboundHandler());
        pipeline.addLast(new ServerChildHandler());
    }
}
