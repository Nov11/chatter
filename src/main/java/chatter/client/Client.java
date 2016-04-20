package chatter.client;

import chatter.client.handler.ClientHandlerInitializer;
import chatter.common.Address;
import chatter.common.ChatMessage;
import chatter.common.Lg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

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

    public static void main(String[] args) {
        Client client = new Client("client", "server");
        client.start();
    }

    public Channel start() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandlerInitializer())
                .option(ChannelOption.SO_REUSEADDR, true);
        channel = bootstrap.connect(Address.serverAddress).syncUninterruptibly().channel();
        System.out.println(locateName + ": " + channel.localAddress());
        //prefer command line close than this one below:
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        });

        new Thread() {
            @Override
            public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (line.equals("exit")) {
                            System.exit(0);
                            return;
                        }
                        ChatMessage chatMessage = new ChatMessage(0, locateName, remoteName, line);
                        channel.writeAndFlush(chatMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        ChatMessage chatMessage = new ChatMessage(0, locateName, Address.serverName, "self register");
        channel.writeAndFlush(chatMessage);
        return channel;
    }

    private void shutdown() {
        Lg.log("shutdown hook running");
        long start = System.currentTimeMillis();
        eventLoopGroup.shutdownGracefully().syncUninterruptibly();
        long end = System.currentTimeMillis();
        Lg.log("eventLoopGroup shutdown takes : " + (end - start) / 1000 + "s");
    }
}
