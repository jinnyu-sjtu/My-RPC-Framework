package top.zxl.rpc.provider;

/**
 * 服务注册接口
 * @Author zxl
 * @Date 2022/4/4 13:30
 * @Version 1.0
 */
public interface ServiceProvider {
    <T> void addServiceProvider(T service);
    Object getService(String serviceName);
}
