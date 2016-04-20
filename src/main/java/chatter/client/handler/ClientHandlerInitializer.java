package chatter.client.handler;

import chatter.common.handler.MsgDecoder;
import chatter.common.handler.MsgEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

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
    }
}
