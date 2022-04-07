package top.zxl.test;

import top.zxl.rpc.api.HelloService;
import top.zxl.rpc.provider.ServiceProviderImpl;
import top.zxl.rpc.provider.ServiceProvider;
import top.zxl.rpc.transport.netty.server.NettyServer;

/**
 * @Author zxl
 * @Date 2022/4/5 14:11
 * @Version 1.0
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
//        ServiceProvider registry = new ServiceProviderImpl();
        //注册服务，服务注册表是类变量，是唯一的
//        registry.addServiceProvider(helloService);
//        NettyServer server = new NettyServer();
//        server.start(9999);

        NettyServer server = new NettyServer("127.0.0.1", 9999, 0);  //序列化方法编号0
        server.publishService(helloService, HelloService.class);

    }
}
