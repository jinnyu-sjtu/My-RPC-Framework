package top.zxl.rpc.serializer;

/**
 * @Author zxl
 * @Date 2022/4/4 23:21
 * @Version 1.0
 */
public interface CommonSerializer {
    //序列化
    byte[] serialize(Object obj);

    //反序列化
    Object deserialize(byte[] bytes, Class<?> clazz);

    //获取该序列化器的编号
    int getCode();

    //目前只有一个JSON 序列化器
    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
