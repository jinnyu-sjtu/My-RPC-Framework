package top.zxl.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author zxl
 * @Date 2022/4/3 16:27
 * @Version 1.0
 */

/**
 * 作用在其他注解的注解(或者说 元注解)是:
 *
 * @Retention - 标识这个注解怎么保存，是只在代码中，还是编入class文件中，或者是在运行时可以通过反射访问。
 * @Documented - 标记这些注解是否包含在用户文档中。
 * @Target - 标记这个注解应该是哪种 Java 成员。TYPE:用于描述类、接口(包括注解类型) 或enum声明
 * @Inherited - 标记这个注解是继承于哪个注解类(默认 注解并没有继承于任何子类)
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    public String name() default "";

}
