package top.zxl.rpc.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.RpcServer;
import top.zxl.rpc.encoder.CommonDecoder;
import top.zxl.rpc.encoder.CommonEncoder;
import top.zxl.rpc.enumeration.RpcError;
import top.zxl.rpc.exception.RpcException;
import top.zxl.rpc.provider.ServiceProvider;
import top.zxl.rpc.provider.ServiceProviderImpl;
import top.zxl.rpc.registry.NacosServiceRegistry;
import top.zxl.rpc.registry.ServiceRegistry;
import top.zxl.rpc.serializer.CommonSerializer;
import top.zxl.rpc.serializer.KryoSerializer;

import java.net.InetSocketAddress;

/** NIO方式服务提供侧
 * @Author zxl
 * @Date 2022/4/4 20:13
 * @Version 1.0
 */
public class NettyServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private String host;
    private int port;
    private ServiceRegistry serviceRegistry;
    private ServiceProvider serviceProvider;
    private final CommonSerializer serializer;

    public NettyServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();  //Nacos提供服务器注册服务
        serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    @Override
    public void start() {
        //负责处理连接，连接到来，马上按照策略将 SocketChannel 转发给 WorkerEventLoopGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //会由 next 选择其中一个 EventLoop 来将这 个SocketChannel 注册到其维护的 Selector 并对其后续的 IO 事件进行处理。
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求。
            //ServerBootstrap监听的一个端口对应一个boss线程，它们一一对应。
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //Netty 中有一个很重要的设计模式——责任链模式: 责任链上有多个处理器，
            // 每个处理器都会对数据进行加工，并将处理后的数据传给下一个处理器。
            //代码中的 CommonEncoder、CommonDecoder和NettyServerHandler 分别就是编码器，解码器和数据处理器。
            //初始化ServerBootstrap
            serverBootstrap.group(bossGroup, workerGroup)  //两个
                    .channel(NioServerSocketChannel.class) //使用非阻塞IO
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //编码器
                            //pipeline.addLast(new CommonEncoder(new JsonSerializer()));
                            pipeline.addLast(new CommonEncoder(new KryoSerializer()));
                            //解码器
                            pipeline.addLast(new CommonDecoder());
                            //数据处理器
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            //绑定端口
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生：", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
