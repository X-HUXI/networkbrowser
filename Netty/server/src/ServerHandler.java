
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {
    /**
     * 重写读数据时处理的方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        //声明字节数组，buf.readableBytes()返回的是buf缓冲中可读的字节数
        byte[] req = new byte[buf.readableBytes()];
        //将buf缓冲区中的字节读取到字节数组req中
        buf.readBytes(req);
        String body = new String(req, "utf-8");
        System.out.println("Server打印接收到的信息：" + body);
        String response = "Server返回给Client的响应信息：" + body;

        //1.ctx.writeAndFlush()方法相当于连续调用了write()和flush()方法，因为write()方法只是将buf写到了渠道的缓冲区中，flush()方法会将缓冲区中的数据传给客户端
        //2.这里Unpooled工具类的作用就是讲字节数组转成netty的ByteBuf对象
        //3.这里使用了writeAndFlush()方法会自动释放buf缓冲区所以不需要想ClientHandler中那样finally中手动释放buf缓冲区了
        //4.addListener()方法：当监听到服务器将数据写给客户端，并且确认客户端已经收到信息后，
        // 服务器端就会主动去关闭跟客户端的连接，因为客户端调用了cf1.channel().closeFuture().sync()方法，所以客户端这里的阻塞就会打开，继续向后执行代码
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
//                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 重写读数据出现异常处理的方法
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
