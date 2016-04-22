package chatter.client.handler;

import chatter.client.Client;
import chatter.common.ChatMessagePB;
import chatter.common.Lg;
import chatter.common.handler.HeartBeatHandler;
import chatter.common.handler.MsgDecoder;
import chatter.common.handler.MsgEncoder;
import chatter.server.handler.RouterMap;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by c0s on 16-4-20.
 */
public class ClientHandlerInitializer extends ChannelInitializer<Channel>{
    private static Lg logger = new Lg(ClientHandlerInitializer.class);

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HeartBeatHandler());
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(new ProtobufDecoder(ChatMessagePB.ChatMessageProto.getDefaultInstance()));
//        pipeline.addLast(new MsgDecoder());
//        pipeline.addLast(new MsgEncoder());
        pipeline.addLast(new ClientHandler());

        ch.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                logger.log("server " + channel.remoteAddress() + " closed.");
                logger.log("exiting child program");
            }
        });
    }
}
