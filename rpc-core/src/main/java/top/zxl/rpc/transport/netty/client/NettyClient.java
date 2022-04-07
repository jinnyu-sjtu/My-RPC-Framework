package top.zxl.rpc.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.RpcClient;
import top.zxl.rpc.encoder.CommonDecoder;
import top.zxl.rpc.encoder.CommonEncoder;
import top.zxl.rpc.entity.RpcRequest;
import top.zxl.rpc.entity.RpcResponse;
import top.zxl.rpc.enumeration.RpcError;
import top.zxl.rpc.exception.RpcException;
import top.zxl.rpc.registry.NacosServiceRegistry;
import top.zxl.rpc.registry.ServiceRegistry;
import top.zxl.rpc.serializer.CommonSerializer;
import top.zxl.rpc.serializer.JsonSerializer;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * NIO方式消费侧客户端类
 * @Author zxl
 * @Date 2022/4/4 21:56
 * @Version 1.0
 */
public class NettyClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final EventLoopGroup group;
    private static final Bootstrap bootstrap;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    private final CommonSerializer serializer;
    private final ServiceRegistry serviceRegistry = new NacosServiceRegistry();

    public NettyClient() {
        this(DEFAULT_SERIALIZER);
    }

    public NettyClient( Integer serializer) {
        this.serializer = CommonSerializer.getByCode(serializer);
    }

//    //静态代码块直接配置好Netty客户端
//    static {
//
//        bootstrap.group(group)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ChannelPipeline pipeline = ch.pipeline();
//                        //分别就是编码器，解码器和数据处理器。
//                        pipeline.addLast(new CommonDecoder())
//                                .addLast(new CommonEncoder(new JsonSerializer()))
//                                .addLast(new NettyClientHandler());
//                    }
//                });
//    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        AtomicReference<Object> result = new AtomicReference<>(null);
        try{
//            ChannelFuture future = bootstrap.connect(host, port).sync();
//            logger.info("客户端连接到服务器 {}:{}", host, port);
//            Channel channel = future.channel();

            InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);

            if (channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()) {
                        logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                    } else {
                        logger.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
                //注意这里的发送是非阻塞的，所以发送后会立刻返回，而无法得到结果。
                //这里通过 AttributeKey 的方式阻塞获得返回结果：

                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                logger.info("客户端收到的应答报文：{}", rpcResponse);
//                return rpcResponse.getData();
                return rpcResponse;
            }
        } catch (InterruptedException e) {
            logger.error("发送消息时有错误发生: ", e);
        }
        return null;
    }
}
