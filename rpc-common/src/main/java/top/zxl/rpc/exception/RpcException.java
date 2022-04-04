package top.zxl.rpc.exception;

import top.zxl.rpc.enumeration.RpcError;

/**
 * RPC调用异常
 * @Author zxl
 * @Date 2022/4/4 13:45
 * @Version 1.0
 */
public class RpcException extends RuntimeException{
    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

//    public RpcException(String message, Throwable cause) {
//        super(message, cause);
//    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }

}
