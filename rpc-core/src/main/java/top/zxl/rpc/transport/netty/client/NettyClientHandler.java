package top.zxl.rpc.transport.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.entity.RpcResponse;

/**
 * Netty客户端侧处理器
 * @Author zxl
 * @Date 2022/4/5 12:23
 * @Version 1.0
 */
public class NettyClientHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            logger.info(String.format("客户端接收到消息: %s", msg));
            //阻塞等待回复的消息
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            //接收传回的消息
            ctx.channel().attr(key).set((RpcResponse) msg);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}
