package ExerMate.ExerMate.Frame.Config;

import ExerMate.ExerMate.Biz.Handler.HttpHandler;
import ExerMate.ExerMate.Biz.Handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyServer {

    @Autowired
    private HttpHandler httpHandler;

    @Autowired WebSocketHandler websocketHandler;

    public void startHttp() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(7000)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast("httpAggregator", new HttpObjectAggregator(512 * 1024 * 1024));
                            pipeline.addLast(httpHandler);
                        }
                    });
            ChannelFuture cf = b.bind().sync();
            System.out.println("http시작： " + cf.channel().localAddress());
            cf.channel().closeFuture().sync();
        } finally{
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }

    public void startWebSocket() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            sb.group(group, bossGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(520)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            ch.pipeline().addLast(new HttpObjectAggregator(64*1024));
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));
                            ch.pipeline().addLast(websocketHandler);
                        }
                    });
            ChannelFuture cf = sb.bind().sync(); // 服务器异步创建绑定
            System.out.println("websocket시작： " + cf.channel().localAddress());
            cf.channel().closeFuture().sync(); // 关闭服务器通道
        } finally {
            group.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
        }
    }
}
