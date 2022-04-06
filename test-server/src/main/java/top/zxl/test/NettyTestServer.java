package top.zxl.test;

import top.zxl.rpc.RpcServer;
import top.zxl.rpc.api.HelloService;
import top.zxl.rpc.registry.DefaultServiceRegistry;
import top.zxl.rpc.registry.ServiceRegistry;
import top.zxl.rpc.serializer.CommonSerializer;
import top.zxl.rpc.transport.netty.server.NettyServer;

/**
 * @Author zxl
 * @Date 2022/4/5 14:11
 * @Version 1.0
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        //注册服务，服务注册表是类变量，是唯一的
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
