package top.zxl.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author zxl
 * @Date 2022/4/3 16:11
 * @Version 1.0
 */

/**
 * @Data 注解的主要作用是提高代码的简洁，
 * 使用这个注解可以省去代码中大量的get()、 set()、 toString()等方法；
 * @AllArgsConstructor 使用后添加一个构造函数，该构造函数含有所有已声明字段属性参数
 */

@Data
@AllArgsConstructor
//注意这个对象需要实现Serializable接口，因为它需要在调用过程中从客户端传递给服务端，需要序列化
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
