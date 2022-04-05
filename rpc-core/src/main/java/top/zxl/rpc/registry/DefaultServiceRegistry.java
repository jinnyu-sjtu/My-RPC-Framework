package top.zxl.rpc.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.enumeration.RpcError;
import top.zxl.rpc.exception.RpcException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zxl
 * @Date 2022/4/4 13:31
 * @Version 1.0
 */
public class DefaultServiceRegistry implements ServiceRegistry{
    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    //服务名与提供服务的实现类的对应关系保存在一个 ConcurrentHashMap 中
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    // Set 来保存当前有哪些对象已经被注册
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();


    @Override
    public <T> void register(T service) {
        //getCanonicalName()就是取一个更见名思意的名称
        String serviceName = service.getClass().getCanonicalName();
        //如果服务注册了就直接返回
        if(registeredService.contains(serviceName)) return;
        //注册服务
        registeredService.add(serviceName);
        //查看服务实现了什么接口
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        //注册接口和对应的服务
        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }
        logger.info("向接口: {} 注册服务: {}", interfaces, serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}