package top.zxl.rpc.transport.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.RpcClient;
import top.zxl.rpc.entity.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author zxl
 * @Date 2022/4/3 19:09
 * @Version 1.0
 */

/**
 * 发送的逻辑我使用了一个RpcClient对象来实现，这个对象的作用，就是将一个对象发过去，并且接受返回的对象。
 */
public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private String host;
    private int port;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     *直接使用Java的序列化方式，通过Socket传输。创建一个Socket，
     * 获取ObjectOutputStream对象，然后把需要发送的对象传进去即可，
     * 接收时获取ObjectInputStream对象，readObject()方法就可以获得一个返回的对象。
     * @param rpcRequest 服务器调用的接口信息
     * @return
     */
    public Object sendRequest(RpcRequest rpcRequest) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时有错误发生：", e);
            return null;
        }
    }
}
