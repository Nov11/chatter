package chatter.client.handler;

import chatter.client.Client;
import chatter.common.Lg;
import chatter.common.handler.MsgDecoder;
import chatter.common.handler.MsgEncoder;
import chatter.server.handler.RouterMap;
import io.netty.channel.*;

/**
 * Created by c0s on 16-4-20.
 */
public class ClientHandlerInitializer extends ChannelInitializer<Channel>{

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MsgDecoder());
        pipeline.addLast(new MsgEncoder());
        pipeline.addLast(new ClientHandler());

        ch.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                Lg.log("server " + channel.remoteAddress() + " closed.");
                Lg.log("exiting");
                //todo:how to terminate client in handler?
//                System.exit(0);
            }
        });
    }
}
