

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    public static void main(String[] args) throws Exception{

        NioEventLoopGroup group = new NioEventLoopGroup();//用于处理网络通信（读写）的线程组

        Bootstrap b = new Bootstrap();//创建客户端辅助类工具
        b.group(group)//绑定线程组
                .channel(NioSocketChannel.class)//设置通信渠道为TCP协议
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientHandler());//这里配置具体数据接收方法的处理
                    }
                });

        /*与8787端口通讯*/
        ChannelFuture cf1 = b.connect("127.0.0.1", 8787).sync();//异步建立连接

        cf1.channel().write(Unpooled.copiedBuffer("hello world".getBytes()));//将“hello world”写到buf缓冲区
        cf1.channel().flush();//这里必须使用flush(),只用冲刷才能将buf缓冲区中的数据传给服务器端

        /*与8686端口通讯*/
        ChannelFuture cf2 = b.connect("127.0.0.1", 8686).sync();
        cf2.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty".getBytes()));

        cf1.channel().closeFuture().sync();//等待关闭，相当于Thread.sleep(Integer.MAX_VALUE)
        cf2.channel().closeFuture().sync();

        group.shutdownGracefully();//关闭线程组
    }
}
