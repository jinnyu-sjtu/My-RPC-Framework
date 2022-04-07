package top.zxl.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.enumeration.RpcError;
import top.zxl.rpc.exception.RpcException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author zxl
 * @Date 2022/4/6 12:56
 * @Version 1.0
 */
public class NacosServiceRegistry implements ServiceRegistry{
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    private static final String SERVER_ADDR = "127.0.0.1:8848";
    private static final NamingService namingService;

    //初始化
    static {
        try{
            namingService = NamingFactory.createNamingService(SERVER_ADDR);
            logger.info("顺利连接到Nacos");
        } catch (NacosException e) {
            logger.info("连接到Nacos时有错误发生：", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            logger.info("注册服务：{}对应的服务器地址：{}！", serviceName, inetSocketAddress.toString());
            namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            // 获取到某个服务的所有提供者列表后，需要选择一个，这里就涉及了负载均衡策略，这里我们先选择第 0 个
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.info("获取服务时有错误发生：", e);
        }
        return null;
    }
}
