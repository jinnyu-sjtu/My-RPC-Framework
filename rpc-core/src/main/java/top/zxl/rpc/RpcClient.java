package top.zxl.rpc;

import top.zxl.rpc.entity.RpcRequest;

/**
 * 为了保证通用性，把 Client 抽象成接口
 * @Author zxl
 * @Date 2022/4/4 17:03
 * @Version 1.0
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
