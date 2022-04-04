package top.zxl.rpc.transport;

import lombok.Data;
import top.zxl.rpc.entity.RpcRequest;
import top.zxl.rpc.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端方面，由于在客户端这一侧我们并没有接口的
 * 具体实现类，就没有办法直接生成实例对象。
 * 这时，我们可以通过动态代理的方式生成实例，
 * 并且调用方法时生成需要的RpcRequest对象
 * 并且发送给服务端。
 * @Author zxl
 * @Date 2022/4/3 19:00
 * @Version 1.0
 */

//通过动态代理实现的

@Data
public class RpcClientProxy implements InvocationHandler {
    private String host;  //主机
    private int port;     //端口

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //使用getProxy()方法来生成代理对象
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //使用builder模式创建一个调用的接口信息对象
        //builder模式：链式编程可以使得代码可读性高，链式编程的原理就是返回一个this对象，就是返回本身，达到链式效果
        RpcRequest rpcRequest =  RpcRequest.builder()
                .InterfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        System.out.println("创建了一个RpcClient...");
        System.out.println(rpcRequest.toString());
        //创建一个rpcClient对象，可以发送信息并收到返回值
        RpcClient rpcClient = new RpcClient();
        //返回服务器响应对象的响应数据
        return ((RpcResponse) rpcClient.sendRequest(rpcRequest, host, port)).getData();
    }

}
