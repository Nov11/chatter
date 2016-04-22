package chatter.server.handler;

import chatter.common.ChatMessagePB;
import chatter.common.Lg;
import chatter.common.handler.HeartBeatHandler;
import chatter.common.handler.MsgDecoder;
import chatter.common.handler.MsgEncoder;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

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
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(new ProtobufDecoder(ChatMessagePB.ChatMessageProto.getDefaultInstance()));
//        pipeline.addLast(new MsgDecoder());
//        pipeline.addLast(new MsgEncoder());

        EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(10);
        pipeline.addLast(executorGroup, new RegClientRouteInboundHandler());
        pipeline.addLast(executorGroup, new ServerChildHandler());
    }
}
