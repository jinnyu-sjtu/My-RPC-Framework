package top.zxl.test;

import top.zxl.rpc.RpcClient;
import top.zxl.rpc.api.HelloObject;
import top.zxl.rpc.api.HelloService;
import top.zxl.rpc.transport.RpcClientProxy;
import top.zxl.rpc.transport.netty.client.NettyClient;

/**
 * @Author zxl
 * @Date 2022/4/5 13:15
 * @Version 1.0
 */
public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
