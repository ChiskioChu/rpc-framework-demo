package demo.netty.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import demo.dto.RpcRequest;
import demo.dto.RpcResponse;
import demo.enumeration.SerializerCode;
import demo.exception.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * kryo：高性能的序列化/反序列化工具，变长存储特性，且使用了字节码生成机制，较高的运行速度和较小的体积
 * 特点：把对象信息直接写到序列化数据中，反序列化可以精确找到原始类信息，在readXXX时，不用像json一样传入Class或者Type类信息
 * */

public class KryoSerializer implements CommonSerializer{

    // 线程不安全，使用ThreadLocal保证线程安全，或者采用kryo的KryoPool，这里采用前者
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class); // 实际没有注册
        kryo.register(RpcRequest.class);
        kryo.setReferences(true); // 打开对循坏引用的支持，防止栈内存溢出，默认打开
        kryo.setRegistrationRequired(false); // 禁止类注册，因为Class在不同的机器上注册编号不能保证一致
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)){
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            System.out.println("序列化时有错误发生:" + e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return o;
        } catch (Exception e) {
            System.out.println("反序列化时有错误发生:" + e);
            throw new SerializeException("反序列化时有错误发生");
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("KRYO").getCode();
    }
}
