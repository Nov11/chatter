package chatter.client;

import chatter.client.handler.ClientHandlerInitializer;
import chatter.common.Address;
import chatter.common.ChatMessage;
import chatter.common.Lg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.*;

/**
 * Created by c0s on 16-4-20.
 */
public class Client {
    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;

    private String locateName;
    private String remoteName;
    private Channel channel;


    public Client(String locateName, String remoteName) {
        this.locateName = locateName;
        this.remoteName = remoteName;
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("client", "server");
        client.start();
    }

    public void start() throws InterruptedException {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandlerInitializer())
                .option(ChannelOption.SO_REUSEADDR, true);
        ChannelFuture future = bootstrap.connect(Address.serverAddress);
        channel = future.sync().channel();
        System.out.println(locateName + ": " + channel.localAddress());
        //prefer command line close than this one below:
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                shutdown();
//            }
//        });

        new Thread() {
            @Override
            public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (line.equals("exit")) {
//                            System.exit(0);
                            channel.close();
                            return;
                        }
                        ChatMessage chatMessage = new ChatMessage(0, locateName, remoteName, line);
                        channel.writeAndFlush(chatMessage.getChatMessageProto());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        ChatMessage chatMessage = new ChatMessage(0, locateName, Address.serverName, "self register");
        channel.writeAndFlush(chatMessage.getChatMessageProto());
        //shutdown netty when server close connection
        //maybe a connection retry will be added in future.
        channel.closeFuture().syncUninterruptibly();
        shutdown();
        //exit the client [workable]
        System.exit(0);
    }

    private void shutdown() {
        Lg.log("shutdown hook running");
        long start = System.currentTimeMillis();
        eventLoopGroup.shutdownGracefully().syncUninterruptibly();
        long end = System.currentTimeMillis();
        Lg.log("eventLoopGroup shutdown takes : " + (end - start) / 1000 + "s");
    }
}
