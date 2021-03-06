package top.zxl.rpc.transport.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.entity.RpcRequest;
import top.zxl.rpc.entity.RpcResponse;
import top.zxl.rpc.handler.RequestHandler;
import top.zxl.rpc.provider.ServiceProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 线程类：处理线程，接受对象等
 * @Author zxl
 * @Date 2022/4/4 14:23
 * @Version 1.0
 */
public class SocketRequestHandlerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(SocketRequestHandlerThread.class);

    private Socket socket;
    //通过反射进行方法调用
    private RequestHandler requestHandler;
    //接口&服务注册表
    private ServiceProvider serviceRegistry;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceProvider serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }



    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            //从流中获取请求对象
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            //获取接口名字
            String interfaceName = rpcRequest.getInterfaceName();
            //在注册表中获取服务器对象
            Object service = serviceRegistry.getService(interfaceName);
            //进行处理并得到返回结果
            logger.info("服务器将处理收到的请求：{}", rpcRequest.toString());
            Object result = requestHandler.handle(rpcRequest, service);
            objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }
}
