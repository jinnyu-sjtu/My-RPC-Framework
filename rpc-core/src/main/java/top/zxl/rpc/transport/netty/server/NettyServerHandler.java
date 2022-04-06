package top.zxl.rpc.transport.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.entity.RpcRequest;
import top.zxl.rpc.entity.RpcResponse;
import top.zxl.rpc.handler.RequestHandler;
import top.zxl.rpc.registry.DefaultServiceRegistry;
import top.zxl.rpc.registry.ServiceRegistry;

/**
 * @Author zxl
 * @Date 2022/4/5 12:35
 * @Version 1.0
 * NettyServerhandler 用于接收 RpcRequest，并且执行调用，将调用结果返回封装成 RpcResponse 发送出去。
 */
public class NettyServerHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    static {
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,  Object msg) throws Exception {
        try {
            logger.info("服务器接收到请求：{}", msg);
            String interfaceName = ((RpcRequest)msg).getInterfaceName();
            //从收到的消息中确定要调用的接口，从注册器中查找到对应的服务
            Object service = serviceRegistry.getService(interfaceName);
            //调用了对应的服务
            Object result = requestHandler.handle((RpcRequest)msg, service);
            //把调用结果，以及报文的id号 发送出去
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result, ((RpcRequest) msg).getRequestId()));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
