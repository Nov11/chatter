package chatter.server;

import chatter.common.Address;
import chatter.common.Lg;
import chatter.server.handler.ServerChildHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.net.InetSocketAddress;

/**
 * Created by c0s on 16-4-20.
 */
public class Server {

    private EventLoopGroup parent;
    private EventLoopGroup child;
    private ServerBootstrap serverBootstrap;

    public static void main(String[] args) {
        Server server = new Server();
        Channel channel = server.boot(Address.serverAddress);
        channel.closeFuture().syncUninterruptibly();
        Lg.log("main exits");
    }

    public Channel boot(InetSocketAddress inetSocketAddress) {
        parent = new NioEventLoopGroup();
        child = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(parent, child)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ServerChildHandlerInitializer());
        Channel channel = serverBootstrap.bind(inetSocketAddress).syncUninterruptibly().channel();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                Lg.log("shutdown hook running.");
                long start = System.currentTimeMillis();
                channel.close();
                parent.shutdownGracefully();
                child.shutdownGracefully();
                long end = System.currentTimeMillis();
                Lg.log("server shutdown hook took " + (end - start) / 1000 + "s to complete");
            }
        });
        return channel;
    }
}
