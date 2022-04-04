package top.zxl.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.zxl.rpc.enumeration.ResponseCode;
import java.io.Serializable;

/**
 * @Author zxl
 * @Date 2022/4/3 18:33
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> implements Serializable {

    /**
     * 响应对应的请求号
     */
    private String requestId;

    /**
     * 状态码
     */
    private Integer statusCode;
    /**
     * 响应状态补充信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public static<T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> responce = new RpcResponse<>();
        responce.setRequestId(requestId);
        responce.setStatusCode(ResponseCode.SUCCESS.getCode());
        responce.setData(data);
        return responce;
    }

    public static<T> RpcResponse<T> success(T data) {
        RpcResponse<T> responce = new RpcResponse<>();
        responce.setStatusCode(ResponseCode.SUCCESS.getCode());
        responce.setData(data);
        return responce;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code) {
        RpcResponse<T> response = new RpcResponse<>();
//        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }

}
