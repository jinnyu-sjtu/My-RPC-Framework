package top.zxl.test;

import top.zxl.rpc.api.HelloObject;
import top.zxl.rpc.api.HelloService;
import top.zxl.rpc.transport.RpcClientProxy;
import top.zxl.rpc.transport.socket.client.SocketClient;

/**
 * @Author zxl
 * @Date 2022/4/3 23:27
 * @Version 1.0
 */
public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient("127.0.0.1", 9000);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        /*
        //确定要访问的服务器的ip以及端口号，初始化一个客户端代理
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        //通过类名确定生成代理对象
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "我要进行远程调用了");

        //直接通过代理对象调用它的hello方法，这里通过反射实现远程调用
        String res = helloService.hello(object);
        System.out.println(res);
    */

    }

}
