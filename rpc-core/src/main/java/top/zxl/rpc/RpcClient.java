package top.zxl.rpc;

import top.zxl.rpc.entity.RpcRequest;
import top.zxl.rpc.serializer.CommonSerializer;

/**
 * 为了保证通用性，把 Client 抽象成接口
 * @Author zxl
 * @Date 2022/4/4 17:03
 * @Version 1.0
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);
}
