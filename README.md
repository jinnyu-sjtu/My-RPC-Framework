# My-RPC-Framework
自己实现RPC远程调用服务
是照着这个大佬的博客写的：
https://blog.csdn.net/qq_40856284/category_10138756.html

# 第一天
定义了RpcRequest和RpcResponce对象进行信息传递，
定义了通用的RPC调用错误码，
定义了需要调用的接口HelloService，客户端需要访问服务器调用该接口。

把接口服务注册到服务器，
建立了线程池，
线程池的线程循环监听接口处理接收的RpcRequest，
通过反射调用该方法并返回处理结果给客户端。

客户端通过动态代理使用getProxy()方法来生成代理对象，
直接通过代理对象调用它的hello方法，这里通过反射实现远程调用。

# 第二天

实现了一个默认的服务注册器（一个接口只有一个实现类），
**服务名**与**服务的实现类**的对应关系保存在一个 ConcurrentHashMap 中  ，
Set 来保存当前有哪些对象已经被注册。
 
服务器可以直接根据客户端的请求调用不同的服务

# 第三天 使用了Netty传输和通用序列化接口

本节我们会将传统的 BIO 方式传输换成效率更高的 NIO 方式。
当然不会使用 Java 原生的 NIO，而是采用更为简单的 Netty。
本节还会实现一个通用的序列化接口，为多种序列化支持做准备。
并且，本节还会自定义传输的协议。

当前版本使用的是Json序列化方式，这种方式有个注意点是：序列化后的字节流，在反序列化后会丢失类型信息。
所以服务器收到RpcRequest并反序列化后，之中的方法参数字段需要进行序列化再反序列化，以补充类型信息。

Netty 中有一个很重要的设计模式——责任链模式。
责任链上有多个处理器，每个处理器都会对数据进行加工，并将处理后的数据传给下一个处理器。
代码中的 CommonEncoder、CommonDecoder和NettyServerHandler 分别就是编码器，解码器和数据处理器。

自定义了传输协议

![1649341407(1)](https://user-images.githubusercontent.com/63715941/162221858-84bbdbec-f444-4bb3-a53a-7ba29d34ddf0.png)



# 第四天 添加Kryo序列化方法

建立了通用序列化接口，有Json和Kryo两种序列化方式


# 第五天 使用了nacos进行服务器ip和端口号的注册

创建和初始化服务器的时候，把要调用的服务和服务器的ip地址+端口注册到nacos中。

服务器会把服务注册到serviceProvider中

客户端在建立连接的时候，会通过服务名去nacos中查找服务器的ip和端口号。接着建立与服务器的连接，向服务器发送报文，服务器接收到后，通过反射调用服务的相应的方法，并将结果返回给客户端。

客户端收到后进行反序列化并展示。

