package chatter.server.handler;

import chatter.common.handler.MsgDecoder;
import chatter.common.handler.MsgEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * Created by c0s on 16-4-20.
 */
public class ChildHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MsgDecoder());
        pipeline.addLast(new MsgEncoder());
        pipeline.addLast(new ChildHandler());
    }
}
