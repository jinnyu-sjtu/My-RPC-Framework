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
        //这需要客户端知道服务端的ip地址，才能建立连接进行远程调用

        //现在直接不用了，而是从nacos种查找相应的用户ip
        RpcClient client = new NettyClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
