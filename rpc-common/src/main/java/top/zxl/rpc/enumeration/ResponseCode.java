package top.zxl.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 方法调用结果的响应状态码
 * @Author zxl
 * @Date 2022/4/3 18:40
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "调用方法成功"),
    FAIL(500, "调用方法失败"),
    METHOD_NOT_FOUND(500, "未找到指定的方法"),
    CLASS_NOT_FOUND(500,"为找到指定类");

    private final int code;
    private final String message;
}
