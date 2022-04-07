package top.zxl.rpc;

/**
 * 为了保证通用性，把 Server抽象成接口
 * @Author zxl
 * @Date 2022/4/4 17:04
 * @Version 1.0
 */
public interface RpcServer {

    <T> void publishService(Object service, Class<T> serviceClass);

    void start();
}
