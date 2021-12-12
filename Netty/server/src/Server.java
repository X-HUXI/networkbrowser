
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    public static void main(String[] args) throws Exception {
        //用于处理服务器端接受客户端连接的线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //用于进行网络通讯（读写）的线程组
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        //创建辅助工具类，用于服务器通道的一系列的配置
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup,workGroup)//绑定两个线程组
                .channel(NioServerSocketChannel.class)//指定NIO的网络传输模式为TCP,UDP:NioDatagramChannel
                .option(ChannelOption.SO_BACKLOG,1024)//设置tcp缓冲
                .option(ChannelOption.SO_SNDBUF,32*1024)//设置发送缓冲大小
                .option(ChannelOption.SO_RCVBUF,32*1024)//设置接收缓冲大小
                .option(ChannelOption.SO_KEEPALIVE,true)//保持连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ServerHandler());//这里配置具体数据接收方法的处理
                    }
                });

        ChannelFuture cf1 = sb.bind(8787).sync();//异步的绑定指定的端口
        ChannelFuture cf2 = sb.bind(8686).sync();//netty可以绑定多个端口

        cf1.channel().closeFuture().sync();//等待关闭，相当于Thread.sleep(Integer.MAX_VALUE)
        cf2.channel().closeFuture().sync();

        //关闭线程组
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
