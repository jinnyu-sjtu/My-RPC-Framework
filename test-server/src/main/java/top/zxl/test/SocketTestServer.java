package top.zxl.test;

import top.zxl.rpc.api.HelloService;
import top.zxl.rpc.provider.ServiceProviderImpl;
import top.zxl.rpc.provider.ServiceProvider;
import top.zxl.rpc.transport.socket.server.SocketServer;

/**
 * @Author zxl
 * @Date 2022/4/3 23:19
 * @Version 1.0
 */
public class SocketTestServer {
    //第一天的最初版
//    public static void main(String[] args) {
//        //这个是要注册的服务
//        HelloService helloService = new HelloServiceImpl();
//        RpcServer rpcServer = new RpcServer();   //启动了一个线程池
//        //把HelloService接口注册到服务器，会不断监听9000端口，
//        // 收到RpcRequest通过反射执行HelloService的方法，然后返回结果
//        rpcServer.register(helloService, 9000);
//    }
    //第二天
    public static void main(String[] args) {
        //服务
        HelloService helloService = new HelloServiceImpl();  //这是创建了一个对象
        //新建一个注册器
        ServiceProvider serviceRegistry = new ServiceProviderImpl();
        //把helloservice注册到注册器
        serviceRegistry.addServiceProvider(helloService);
        //启动一个服务器
        SocketServer rpcServer = new SocketServer(serviceRegistry);
        //开启9000端口进行监听
        rpcServer.start();
    }

}
