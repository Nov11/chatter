package chatter.server;

import chatter.common.Address;
import chatter.common.Lg;
import chatter.server.handler.ServerChildHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.net.InetSocketAddress;

/**
 * Created by c0s on 16-4-20.
 */
public class Server {
    private static Lg logger = new Lg(Server.class);

    private EventLoopGroup parent;
    private EventLoopGroup child;
    private ServerBootstrap serverBootstrap;

    public static void main(String[] args) {
        Server server = new Server();
        server.boot(Address.serverAddress);
        logger.log("main exits");
    }

    public void boot(InetSocketAddress inetSocketAddress) {
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
                shutdown();
            }
        });
        channel.closeFuture().syncUninterruptibly();
    }

    private void shutdown() {
        logger.log("shutdown hook running.");
        long start = System.currentTimeMillis();
        parent.shutdownGracefully().syncUninterruptibly();
        child.shutdownGracefully().syncUninterruptibly();
        long end = System.currentTimeMillis();
        logger.log("server shutdown hook took " + (end - start) / 1000 + "s to complete");
        //this is clumsy.
        Lg.closeLogger();
        Lg.sync();
    }
}
