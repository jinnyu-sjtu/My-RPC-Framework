package top.zxl.test;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zxl.rpc.annotation.Service;
import top.zxl.rpc.api.HelloObject;
import top.zxl.rpc.api.HelloService;

/**
 * @Author zxl
 * @Date 2022/4/3 16:20
 * @Version 1.0
 */
@Service
public class HelloServiceImpl implements HelloService{

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收id为{}的消息：{}", object.getId(), object.getMessage());
        return "客户端远程调用成功！";
    }
}
