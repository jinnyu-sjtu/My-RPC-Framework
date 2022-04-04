package top.zxl.rpc.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.entity.RpcRequest;
import top.zxl.rpc.entity.RpcResponse;
import top.zxl.rpc.enumeration.ResponseCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过反射进行方法调用
 * @Author zxl
 * @Date 2022/4/4 14:29
 * @Version 1.0
 */
public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public Object handle(RpcRequest rpcRequest, Object service) {
        Object result = null;
        try {
            result = invokeTargetMethod(rpcRequest, service);
            logger.info("服务:{}成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (IllegalAccessException | InvocationTargetException e ) {
            logger.error("调用或发送时有错误发生", e );
        }

        return result;
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try{
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            logger.info("获取了：{}服务的方法：{}", service.getClass().getName() , method.getName());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND);
        }

        return method.invoke(service, rpcRequest.getParameters());
    }

}
