import top.zxl.rpc.api.HelloObject;
import top.zxl.rpc.api.HelloService;
import top.zxl.rpc.transport.RpcClientProxy;

/**
 * @Author zxl
 * @Date 2022/4/3 23:27
 * @Version 1.0
 */
public class TestClient {
    public static void main(String[] args) {
        //确定要访问的服务器的ip以及端口号，初始化一个客户端代理
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        //通过类名确定生成代理对象
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "我要进行远程调用了");

        //直接通过代理对象调用它的hello方法，这里通过反射实现远程调用
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
